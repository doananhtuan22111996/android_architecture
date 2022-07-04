package vn.geekup.app.data.repository

import androidx.paging.*
import androidx.room.withTransaction
import kotlinx.coroutines.flow.*
import retrofit2.Response
import timber.log.Timber
import vn.geekup.app.data.model.moment.MomentVO
import vn.geekup.app.data.remote.PagingByNetworkDataSource
import vn.geekup.app.data.local.AppDatabase
import vn.geekup.app.data.local.LocalBoundResource
import vn.geekup.app.data.local.PagingByLocalDataSource
import vn.geekup.app.data.model.general.ListResponse
import vn.geekup.app.data.model.general.ObjectResponse
import vn.geekup.app.data.service.AliaApiService
import vn.geekup.app.data.remote.NetworkBoundService
import vn.geekup.app.domain.dto.*
import vn.geekup.app.domain.model.general.RemoteKey
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.domain.repository.MomentRepository

class MomentDataSource(
    private val db: AppDatabase,
    private val aliaApiService: AliaApiService
) : MomentRepository {

    override suspend fun getFlowMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Flow<ResultModel<ArrayList<MomentModel>>> =
        object :
            NetworkBoundService<ListResponse<MomentVO>, ArrayList<MomentModel>>() {

            override suspend fun onApi(): Response<ObjectResponse<ListResponse<MomentVO>>> =
                aliaApiService.getFlowMomentFeeds(
                    cursor = momentFeedRequestBody.cursor,
                    sort = when (momentFeedRequestBody.sort) {
                        MomentSort.DESC() -> MomentSort.DESC().sortName
                        else -> MomentSort.ASC().sortName
                    }, dates = momentFeedRequestBody.dates
                )

            override suspend fun processResponse(request: ObjectResponse<ListResponse<MomentVO>>?): ResultModel.Success<ArrayList<MomentModel>> {
                val items: ArrayList<MomentModel> = arrayListOf()
                request?.data?.items?.forEach { item ->
                    items.add(item.vo2Model())
                }
                return ResultModel.Success(
                    limit = request?.data?.limit,
                    nextCursor = request?.data?.nextCursor,
                    data = items
                )
            }

        }.build()

//    override suspend fun getPagingMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Flow<PagingData<MomentModel>> =
//        Pager(
//            config = PagingConfig(13),
//        ) {
//            object : PagingByKeyDataSource<MomentVO, MomentModel>() {
//                override suspend fun onApi(nextKey: String?): Response<ObjectResponse<ListResponse<MomentVO>>> =
//                    aliaApiService.getFlowMomentFeeds(
//                        cursor = nextKey,
//                        sort = when (momentFeedRequestBody.sort) {
//                            MomentSort.DESC() -> MomentSort.DESC().sortName
//                            else -> MomentSort.ASC().sortName
//                        }, dates = momentFeedRequestBody.dates
//                    )
//
//                override suspend fun processResponse(request: ListResponse<MomentVO>?): ListResponse<MomentModel>? {
//                    val items: ArrayList<MomentModel> = arrayListOf()
//                    request?.items?.forEach { item ->
//                        items.add(item.vo2Model())
//                    }
//                    return ListResponse(
//                        limit = request?.limit,
//                        nextCursor = request?.nextCursor,
//                        items = items
//                    )
//                }
//
//            }
//        }.flow

    override suspend fun getPagingTravelFeeds(): Flow<PagingData<MomentModel>> =
        Pager(
            config = PagingConfig(15),
        ) {
            object : PagingByNetworkDataSource<MomentVO, MomentModel>() {
                override suspend fun onApi(nextKey: String?): Response<ListResponse<MomentVO>> =
                    aliaApiService.getFlowTravelFeeds(
                        page = nextKey?.toInt() ?: 1
                    )

                override suspend fun processResponse(request: ListResponse<MomentVO>?): ListResponse<MomentModel> {
                    val items: ArrayList<MomentModel> = arrayListOf()
                    request?.items?.forEach { item ->
                        items.add(item.vo2Model())
                    }
                    db.travelFeedDao().insertAll(items)
                    return ListResponse(
                        limit = request?.limit,
                        nextCursor = request?.nextCursor,
                        items = items,
                        metadata = request?.metadata
                    )
                }
            }
        }.flow


    override suspend fun getFlowLocalTravelFeeds(): Flow<ResultModel<List<MomentModel>>> =
        object : LocalBoundResource<List<MomentModel>, List<MomentModel>>() {
            override suspend fun loadDB(): List<MomentModel> {
                return db.travelFeedDao().getTravelFeeds()
            }

            override suspend fun processResponse(request: List<MomentModel>?): ResultModel.Success<List<MomentModel>> {
                return ResultModel.Success(data = request)
            }
        }.build()

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPagingLocalTravelFeeds(): Flow<PagingData<MomentModel>> = Pager(
        config = PagingConfig(25),
        remoteMediator = object : PagingByLocalDataSource<MomentVO, MomentModel, RemoteKey>() {
            override suspend fun onApi(nextKey: String?): Response<ListResponse<MomentVO>> =
                aliaApiService.getFlowTravelFeeds(
                    page = nextKey?.toInt() ?: 1,
                    perPage = 25
                )

            override suspend fun processResponse(request: ListResponse<MomentVO>?): ListResponse<MomentModel> {
                val items: ArrayList<MomentModel> = arrayListOf()
                request?.items?.forEach { item ->
                    items.add(item.vo2Model())
                }
                return ListResponse(
                    limit = request?.limit,
                    nextCursor = request?.nextCursor,
                    items = items,
                    metadata = request?.metadata
                )
            }

            override suspend fun getRemoteKey(state: PagingState<Int, MomentModel>): RemoteKey? {
                Timber.e(
                    "getRemoteKey: RepoId Last: ${
                        db.remoteKeyDao().remoteKeysIdAll().firstOrNull()?.repoId
                    } --- page: ${
                        state.pages
                            .lastOrNull()?.data?.size
                    }"
                )
                return db.withTransaction {
                    db.remoteKeyDao().remoteKeysId(
                        state.pages
                            .lastOrNull()?.data?.lastOrNull()?.id?.toString() ?: ""
                    )
                }
            }

            override suspend fun onRefresh() {
                db.withTransaction {
                    db.remoteKeyDao().delete()
                    db.travelFeedDao().delete()
                }
            }

            override suspend fun onDB(response: ListResponse<MomentModel>?) {
                db.withTransaction {
                    db.remoteKeyDao().insert(
                        RemoteKey(
                            repoId = response?.items?.lastOrNull()?.id?.toString() ?: "",
                            nextKey = response?.metadata?.nextPage?.toString() ?: ""
                        )
                    )
                    db.travelFeedDao().insertAll(response?.items ?: arrayListOf())
                }
            }
        }
    ) {
        db.travelFeedDao().getPagingTravelFeeds()
    }.flow

}
