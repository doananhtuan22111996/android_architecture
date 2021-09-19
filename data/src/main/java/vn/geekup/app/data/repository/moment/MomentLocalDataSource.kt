package vn.geekup.app.data.repository.moment

import io.reactivex.rxjava3.core.Single
import vn.geekup.app.data.local.PreferenceWrapper
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
import javax.inject.Inject

class MomentLocalDataSource @Inject constructor(private val preferenceWrapper: PreferenceWrapper) :
    MomentRepository {

    override fun getMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Single<BaseModelListResponse<MomentModel>> {
        return Single.never()
    }

    override fun putMomentLike(
        momentId: Int,
        momentLikeRequestBody: MomentLikeRequestBody
    ): Single<MomentLikeModel> {
        return Single.never()
    }

    override fun shareMomentToNexion(momentId: Int): Single<MetaDataModel> {
        return Single.never()
    }

    override fun getMomentDetail(momentId: Int): Single<MomentModel> {
        return Single.never()
    }

    override fun getMomentComments(momentCommentRequestBody: MomentCommentRequestBody): Single<BaseModelListResponse<MomentCommentModel>> {
        return Single.never()
    }

    override fun postMomentComments(
        momentId: Int,
        momentPostCommentRequestBody: MomentPostCommentRequestBody
    ): Single<MomentCommentModel> {
        return Single.never()
    }

}