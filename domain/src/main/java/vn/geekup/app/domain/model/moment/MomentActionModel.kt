package vn.geekup.app.domain.model.moment

import vn.geekup.app.domain.model.general.BaseModel

data class MomentLikeModel(
    val newLikesCount: Int = 0,
    val momentId: Int = 0
) : BaseModel()

class MomentCommentModel(
    val id: Int? = 0,
    val commenterAvatarUrl: String? = "",
    val commenterName: String? = "",
    val content: String? = "",
    val createdAt: String? = ""
//                         val linkPreview :
) : BaseModel()