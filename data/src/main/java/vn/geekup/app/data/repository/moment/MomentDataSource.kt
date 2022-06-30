package vn.geekup.app.data.repository.moment

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.geekup.app.domain.dto.MomentFeedRequestBody
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.domain.repository.MomentRepository

class MomentDataSource constructor(
    private val remote: MomentRepository
) : MomentRepository {

    override suspend fun getFlowMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Flow<ResultModel<ArrayList<MomentModel>>> {
        return remote.getFlowMomentFeeds(momentFeedRequestBody)
    }

    override suspend fun getPagingTravelFeeds(momentFeedRequestBody: MomentFeedRequestBody): Flow<PagingData<MomentModel>> {
        return remote.getPagingTravelFeeds(momentFeedRequestBody)
    }

    override suspend fun getFlowMomentDetail(id: Int): Flow<ResultModel<MomentModel>> {
        return remote.getFlowMomentDetail(id)
    }

}