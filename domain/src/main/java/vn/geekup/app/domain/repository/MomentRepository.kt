package vn.geekup.app.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.geekup.app.domain.dto.MomentFeedRequestBody
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.moment.MomentModel

interface MomentRepository {

    suspend fun getPagingTravelFeeds(): Flow<PagingData<MomentModel>>

    suspend fun getPagingLocalTravelFeeds(): Flow<PagingData<MomentModel>>
}
