package vn.geekup.app.domain.usecase

import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import vn.geekup.app.domain.dto.MomentFeedRequestBody
import vn.geekup.app.domain.model.general.BaseModelListResponse
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.domain.repository.MomentRepository

interface MomentUseCase {
    fun getMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Single<BaseModelListResponse<MomentModel>>

    suspend fun getFlowMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Flow<ResultModel<MomentModel>>

    suspend fun getFlowMomentDetail(id: Int): Flow<ResultModel<MomentModel>>

}

class MomentUseCaseImplement(private val momentRepository: MomentRepository) :
    MomentUseCase {

    override fun getMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Single<BaseModelListResponse<MomentModel>> {
        return momentRepository.getMomentFeeds(momentFeedRequestBody)
    }

    override suspend fun getFlowMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Flow<ResultModel<MomentModel>> {
        return momentRepository.getFlowMomentFeeds(momentFeedRequestBody)
    }

    override suspend fun getFlowMomentDetail(id: Int): Flow<ResultModel<MomentModel>> {
        return momentRepository.getFlowMomentDetail(id)
    }
}