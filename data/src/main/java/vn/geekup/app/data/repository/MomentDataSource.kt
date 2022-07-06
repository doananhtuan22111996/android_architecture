package vn.geekup.app.data.repository

import androidx.paging.*
import androidx.room.withTransaction
import kotlinx.coroutines.flow.*
import retrofit2.Response
import vn.geekup.app.data.model.moment.MomentVO
import vn.geekup.app.data.remote.PagingByNetworkDataSource
import vn.geekup.app.data.local.AppDatabase
import vn.geekup.app.data.local.PagingByLocalDataSource
import vn.geekup.app.data.model.general.ListResponse
import vn.geekup.app.data.service.AliaApiService
import vn.geekup.app.domain.model.general.RemoteKey
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.domain.repository.MomentRepository

class MomentDataSource(
    private val db: AppDatabase,
    private val aliaApiService: AliaApiService
) : MomentRepository {

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
                    items.addAll(request?.items?.map { it.vo2Model() }?.toList() ?: arrayListOf())
                    return ListResponse(
                        limit = request?.limit,
                        nextCursor = request?.nextCursor,
                        items = items,
                        metadata = request?.metadata
                    )
                }
            }
        }.flow

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPagingLocalTravelFeeds(): Flow<PagingData<MomentModel>> = Pager(
        config = PagingConfig(25),
        remoteMediator = object : PagingByLocalDataSource<MomentVO, MomentModel, RemoteKey>() {
            override suspend fun onApi(nextKey: String?): Response<ListResponse<MomentVO>> =
                aliaApiService.getFlowTravelFeeds(
                    page = nextKey?.toInt() ?: 1,
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

            override suspend fun getInitKey(): RemoteKey? = db.remoteKeyDao()
                .remoteKeysIdAll().lastOrNull()

            override suspend fun getRemoteKey(
                state: PagingState<Int, MomentModel>
            ): RemoteKey? {
                val repoId =
                    if (state.pages.lastOrNull()?.data?.isEmpty() == true) getInitKey()?.repoId else
                        state.pages
                            .lastOrNull()?.data?.lastOrNull()?.id?.toString()
                return db.withTransaction {
                    db.remoteKeyDao().remoteKeysId(repoId ?: "")
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
        },
        pagingSourceFactory = {
            db.travelFeedDao().getPagingTravelFeeds()
        }
    ).flow

}
