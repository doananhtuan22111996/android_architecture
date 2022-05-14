package vn.geekup.app.data.repository.moment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response
import vn.geekup.app.data.Config.ErrorCode.CODE_999
import vn.geekup.app.data.model.general.ListResponseVO
import vn.geekup.app.data.model.general.ObjectResponseVO
import vn.geekup.app.data.model.moment.MomentVO
import vn.geekup.app.data.remote.auth.AliaApiService
import vn.geekup.app.data.di.remote.NetworkBoundService
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
            NetworkBoundService<ObjectResponseVO<ListResponseVO<MomentVO>>, ArrayList<MomentModel>>() {

            override suspend fun onApi(): Response<ObjectResponseVO<ListResponseVO<MomentVO>>> =
                aliaApiService.getFlowMomentFeeds(
                    cursor = momentFeedRequestBody.cursor,
                    sort = when (momentFeedRequestBody.sort) {
                        MomentSort.DESC() -> MomentSort.DESC().sortName
                        else -> MomentSort.ASC().sortName
                    }, dates = momentFeedRequestBody.dates
                )

            override suspend fun processResponse(request: ObjectResponseVO<ListResponseVO<MomentVO>>?): ResultModel.Success<ArrayList<MomentModel>> {
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
