package vn.geekup.app.domain.repository

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

interface MomentRepository {

    fun getMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Single<BaseModelListResponse<MomentModel>>

    fun putMomentLike(
        momentId: Int = 0,
        momentLikeRequestBody: MomentLikeRequestBody
    ): Single<MomentLikeModel>

    fun shareMomentToNexion(momentId: Int = 0): Single<MetaDataModel>

    fun getMomentDetail(momentId: Int = 0): Single<MomentModel>

    fun getMomentComments(momentCommentRequestBody: MomentCommentRequestBody): Single<BaseModelListResponse<MomentCommentModel>>

    fun postMomentComments(momentId: Int = 0, momentPostCommentRequestBody: MomentPostCommentRequestBody): Single<MomentCommentModel>
}