package vn.geekup.app.domain.model.moment

import vn.geekup.app.domain.model.general.BaseModel

enum class MomentImagePosition {
    ALL_LEFT,
    ALL_RIGHT,
    ALL_BOTTOM,
    ALL_TOP,
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT
}

data class MomentModel(
    val id: Int? = 0,
    val channelName: String? = "",
    val userId: Int? = 0,
    val posterName: String? = "",
    val avatar: String? = "",
    val creatorProfileId: Int? = 0,
    val creatorProfileName: String? = "",
    val createdAt: String? = "",
    val content: String? = "",
    var taggedUsers: ArrayList<TaggedUserModel>? = arrayListOf(),
    var imgUrls: ArrayList<ImgUrlModel>? = arrayListOf(),
    val commentCount: Int? = 0,
    val thisUserCommented: Boolean = false,
    val likeCount: Int? = 0,
    val thisUserLiked: Boolean = false,
    val likedUserIds: ArrayList<Int>? = arrayListOf()
) : BaseModel()
