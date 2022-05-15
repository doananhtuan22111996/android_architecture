package vn.geekup.app.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.geekup.app.domain.dto.MomentFeedRequestBody
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.moment.MomentModel

interface MomentRepository {

    suspend fun getFlowMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Flow<ResultModel<ArrayList<MomentModel>>>

    suspend fun getPagingMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Flow<PagingData<MomentModel>>

    suspend fun getFlowMomentDetail(id: Int): Flow<ResultModel<MomentModel>>
}