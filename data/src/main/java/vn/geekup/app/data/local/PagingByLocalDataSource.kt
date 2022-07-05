package vn.geekup.app.data.local

import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import vn.geekup.app.data.Config
import vn.geekup.app.data.model.general.ListResponse
import vn.geekup.app.domain.model.general.RemoteKey
import vn.geekup.app.domain.model.general.ResultModel
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
abstract class PagingByLocalDataSource<RequestType : Any, ResultType : Any, remote : RemoteKey> :
    RemoteMediator<Int, ResultType>() {

    abstract suspend fun onApi(nextKey: String?): Response<ListResponse<RequestType>>
    abstract suspend fun processResponse(request: ListResponse<RequestType>?): ListResponse<ResultType>?
    abstract suspend fun getInitKey(): remote?
    abstract suspend fun getRemoteKey(state: PagingState<Int, ResultType>): remote?
    abstract suspend fun onRefresh()
    abstract suspend fun onDB(response: ListResponse<ResultType>?)

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        withContext(Dispatchers.IO) {
            getInitKey()
        }
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ResultType>
    ): MediatorResult {
        try {
            return withContext(Dispatchers.IO) {
                Timber.tag("PagingByLocalDataSource").e(Thread.currentThread().name)

                // Get the closest item from PagingState that we want to load data around.
                val loadKey = when (loadType) {
                    LoadType.REFRESH -> null
                    LoadType.PREPEND -> {
                        return@withContext MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }
                    LoadType.APPEND -> {
                        // Query DB for SubredditRemoteKey for the subreddit.
                        // SubredditRemoteKey is a wrapper object we use to keep track of page keys we
                        // receive from the Reddit API to fetch the next or previous page.
                        val remoteKey = getRemoteKey(state)


                        // We must explicitly check if the page key is null when appending, since the
                        // Reddit API informs the end of the list by returning null for page key, but
                        // passing a null key to Reddit API will fetch the initial page.
                        if (remoteKey?.nextKey.isNullOrEmpty()) {
                            return@withContext MediatorResult.Success(endOfPaginationReached = true)
                        }
                        remoteKey?.nextKey
                    }
                }

                val apiResponse = onApi(loadKey)

                if (apiResponse.isSuccessful) {
                    val body = apiResponse.body()
                    when (apiResponse.code()) {
                        Config.ErrorCode.CODE_200, Config.ErrorCode.CODE_201, Config.ErrorCode.CODE_204 -> {
                            val data = processResponse(body)
                            if (loadType == LoadType.REFRESH) {
                                onRefresh()
                            }
                            onDB(data)
                            MediatorResult.Success(endOfPaginationReached = data?.items?.isEmpty() == true)
                        }
                        else -> {
                            val error = ResultModel.ServerErrorException(
                                code = apiResponse.code(),
                                message = apiResponse.message()
                            )
                            MediatorResult.Error(Throwable(error.message))
                        }
                    }
                } else {
                    Timber.e("PagingByLocalDataSource: ${apiResponse.errorBody()?.toString()}")

                    // Todo Change when know baseResponse
//                val result = Gson().fromJson(
//                    apiResponse.errorBody()?.toString(),
//                    ResultModel.ServerErrorException::class.java
//                )
                    MediatorResult.Error(Throwable("Api somethings wrong"))
                }
            }
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

}