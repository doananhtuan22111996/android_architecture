package vn.geekup.app.data.repository.moment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response
import vn.geekup.app.data.Config.ErrorCode.CODE_999
import vn.geekup.app.data.model.moment.MomentVO
import vn.geekup.app.data.remote.auth.AliaApiService
import vn.geekup.app.data.di.remote.NetworkBoundService
import vn.geekup.app.data.di.remote.paging.PagingByKeyDataSource
import vn.geekup.app.data.model.general.ListResponse
import vn.geekup.app.data.model.general.ObjectResponse
import vn.geekup.app.domain.dto.*
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.domain.repository.MomentRepository
import javax.inject.Inject

class MomentRemoteDataSource @Inject constructor(
    private val aliaApiService: AliaApiService,
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

    override suspend fun getPagingTravelFeeds(momentFeedRequestBody: MomentFeedRequestBody): Flow<PagingData<MomentModel>> =
        Pager(
            config = PagingConfig(15),
        ) {
            object : PagingByKeyDataSource<MomentVO, MomentModel>() {
                override suspend fun onApi(nextKey: String?): Response<ListResponse<MomentVO>> =
                    aliaApiService.getFlowTravelFeeds(
                        page = nextKey?.toInt() ?: 1
                    )

                override suspend fun processResponse(request: ListResponse<MomentVO>?): ListResponse<MomentModel>? {
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

            }
        }.flow

    override suspend fun getFlowMomentDetail(id: Int): Flow<ResultModel<MomentModel>> =
        flow {
            emit(
                ResultModel.ServerErrorException(
                    CODE_999,
                    "Force Server Error"
                )
            )
        }.flowOn(Dispatchers.IO)
}
