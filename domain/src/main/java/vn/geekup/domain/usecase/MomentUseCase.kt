package vn.geekup.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.geekup.domain.dto.MomentFeedRequestBody
import vn.geekup.domain.model.general.ResultModel
import vn.geekup.domain.model.moment.MomentModel
import vn.geekup.domain.repository.MomentRepository

interface MomentUseCase {

    suspend fun getPagingMomentFeeds(): Flow<PagingData<MomentModel>>

    suspend fun getPagingLocalMomentFeeds(): Flow<PagingData<MomentModel>>

}

class MomentUseCaseImplement(private val momentRepository: MomentRepository) :
    MomentUseCase {

    override suspend fun getPagingMomentFeeds(): Flow<PagingData<MomentModel>> {
        return momentRepository.getPagingTravelFeeds()
    }

    override suspend fun getPagingLocalMomentFeeds(): Flow<PagingData<MomentModel>> {
        return momentRepository.getPagingLocalTravelFeeds()
    }
}