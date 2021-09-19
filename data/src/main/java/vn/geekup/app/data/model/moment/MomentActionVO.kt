package vn.geekup.app.data.model.moment

import vn.geekup.app.data.model.general.BaseVO
import vn.geekup.app.domain.model.moment.MomentCommentModel
import vn.geekup.app.domain.model.moment.MomentLikeModel

data class MomentLikeVO(val newLikesCount: Int = 0, val momentId: Int = 0) :
    BaseVO<MomentLikeModel>() {
    override fun vo2Model(): MomentLikeModel =
        MomentLikeModel(newLikesCount = newLikesCount, momentId = momentId)
}

data class MomentCommentVO(
    val id: Int? = 0,
    val commenterAvatarUrl: String? = "",
    val commenterName: String? = "",
    val content: String? = "",
    val createdAt: String? = ""
) : BaseVO<MomentCommentModel>() {

    override fun vo2Model(): MomentCommentModel = MomentCommentModel(
        id = id,
        commenterAvatarUrl = commenterAvatarUrl,
        commenterName = commenterName,
        content = content,
        createdAt = createdAt
    )
}