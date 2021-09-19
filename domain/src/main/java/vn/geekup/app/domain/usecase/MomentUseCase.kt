package vn.geekup.app.domain.usecase

import io.reactivex.rxjava3.core.Single
import vn.geekup.app.domain.dto.MomentCommentRequestBody
import vn.geekup.app.domain.dto.MomentFeedRequestBody
import vn.geekup.app.domain.dto.MomentLikeRequestBody
import vn.geekup.app.domain.dto.MomentPostCommentRequestBody
import vn.geekup.app.domain.model.general.BaseModelListResponse
import vn.geekup.app.domain.model.general.MetaDataModel
import vn.geekup.app.domain.model.moment.MomentCommentModel
import vn.geekup.app.domain.model.moment.MomentLikeModel
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.domain.repository.MomentRepository

interface MomentUseCase {
    fun getMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Single<BaseModelListResponse<MomentModel>>

    fun putMomentLike(
        momentId: Int = 0,
        momentLikeRequestBody: MomentLikeRequestBody
    ): Single<MomentLikeModel>

    fun shareMomentToNexion(momentId: Int): Single<MetaDataModel>

    fun getMomentDetail(momentId: Int): Single<MomentModel>

    fun getMomentComments(momentCommentRequestBody: MomentCommentRequestBody): Single<BaseModelListResponse<MomentCommentModel>>

    fun postMomentComments(
        momentId: Int,
        momentPostCommentRequestBody: MomentPostCommentRequestBody
    ): Single<MomentCommentModel>
}

class MomentUseCaseImplement(private val momentRepository: MomentRepository) :
    MomentUseCase {

    override fun getMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Single<BaseModelListResponse<MomentModel>> {
        return momentRepository.getMomentFeeds(momentFeedRequestBody)
    }

    override fun putMomentLike(
        momentId: Int,
        momentLikeRequestBody: MomentLikeRequestBody
    ): Single<MomentLikeModel> {
        return momentRepository.putMomentLike(momentId, momentLikeRequestBody)
    }

    override fun shareMomentToNexion(momentId: Int): Single<MetaDataModel> {
        return momentRepository.shareMomentToNexion(momentId)
    }

    override fun getMomentDetail(momentId: Int): Single<MomentModel> {
        return momentRepository.getMomentDetail(momentId)
    }

    override fun getMomentComments(momentCommentRequestBody: MomentCommentRequestBody): Single<BaseModelListResponse<MomentCommentModel>> {
        return momentRepository.getMomentComments(momentCommentRequestBody)
    }

    override fun postMomentComments(
        momentId: Int,
        momentPostCommentRequestBody: MomentPostCommentRequestBody
    ): Single<MomentCommentModel> {
        return momentRepository.postMomentComments(momentId, momentPostCommentRequestBody)
    }

}