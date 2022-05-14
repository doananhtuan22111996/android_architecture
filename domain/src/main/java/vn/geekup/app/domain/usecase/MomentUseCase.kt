package vn.geekup.app.domain.usecase

import kotlinx.coroutines.flow.Flow
import vn.geekup.app.domain.dto.MomentFeedRequestBody
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.domain.repository.MomentRepository

interface MomentUseCase {

    suspend fun getFlowMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Flow<ResultModel<ArrayList<MomentModel>>>

    suspend fun getFlowMomentDetail(id: Int): Flow<ResultModel<MomentModel>>

}

class MomentUseCaseImplement(private val momentRepository: MomentRepository) :
    MomentUseCase {

    override suspend fun getFlowMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Flow<ResultModel<ArrayList<MomentModel>>> {
        return momentRepository.getFlowMomentFeeds(momentFeedRequestBody)
    }

    override suspend fun getFlowMomentDetail(id: Int): Flow<ResultModel<MomentModel>> {
        return momentRepository.getFlowMomentDetail(id)
    }
}