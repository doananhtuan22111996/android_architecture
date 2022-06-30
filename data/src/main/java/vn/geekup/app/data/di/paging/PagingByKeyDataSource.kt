package vn.geekup.app.data.di.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import vn.geekup.app.data.Config
import vn.geekup.app.data.model.general.ListResponse
import vn.geekup.app.data.model.general.ObjectResponse
import vn.geekup.app.domain.model.general.ResultModel
import java.io.IOException

/**
 * A [PagingSource] that uses the before/after keys returned in page requests.
 *
 * @see PagingByKeyDataSource
 */
abstract class PagingByKeyDataSource<RequestType : Any, ResultType : Any> :
    PagingSource<String, ResultType>() {

    abstract suspend fun onApi(nextKey: String?): Response<ListResponse<RequestType>>
    abstract suspend fun processResponse(request: ListResponse<RequestType>?): ListResponse<ResultType>?

    override fun getRefreshKey(state: PagingState<String, ResultType>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ResultType> {
        return try {
            withContext(Dispatchers.IO) {
                val apiResponse =
                    onApi(nextKey = if (params is LoadParams.Append) params.key else null)
                if (apiResponse.isSuccessful) {
                    val body = apiResponse.body()
                    when (apiResponse.code()) {
                        Config.ErrorCode.CODE_200, Config.ErrorCode.CODE_201, Config.ErrorCode.CODE_204 -> {
                            val data = processResponse(body)
                            Timber.tag("PagingByKeyDataSource").e(Thread.currentThread().name)
                            LoadResult.Page(
                                data = data?.items ?: arrayListOf(),
                                prevKey = null,
//                            nextKey = data?.nextCursor ?: ""
                                nextKey = if (data?.items?.isNotEmpty() == true) data.metadata?.nextPage?.toString() else null
                            )
                        }
                        else -> {

                            val error = ResultModel.ServerErrorException(
                                code = apiResponse.code(),
                                message = apiResponse.message()
                            )
                            LoadResult.Error(Throwable(error.message))
                        }
                    }
                } else {
                    Log.e("PagingByKeyDataSource", "${apiResponse.errorBody()?.toString()}")

//                val result = Gson().fromJson(
//                    apiResponse.errorBody()?.toString(),
//                    ResultModel.ServerErrorException::class.java
//                )
                    LoadResult.Error(Throwable("Api something when wrong"))
                }
            }

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}