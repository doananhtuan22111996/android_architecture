package vn.geekup.app.model.moment

import vn.geekup.app.R
import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.domain.model.moment.ImgUrlModel
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.domain.model.moment.TaggedUserModel

sealed class MomentActionV {
    object MomentDetail : MomentActionV()
    object MomentPreview : MomentActionV()
}

data class MomentModelV(
    val id: Int? = 0,
    val channelName: String? = "",
    val userId: Int? = 0,
    val posterName: String? = "",
    val avatar: String? = "",
    val creatorProfileId: Int? = 0,
    val creatorProfileName: String? = "",
    val createdAt: String? = "",
    val content: String? = "",
    val taggedUsers: ArrayList<TaggedUserModel>? = arrayListOf(),
    var imgUrls: ArrayList<ImgUrlModel>? = arrayListOf(),
    var commentCount: Int? = 0,
    var thisUserCommented: Boolean = false,
    var likeCount: Int? = 0,
    var thisUserLiked: Boolean = false,
    val likedUserIds: ArrayList<Int>? = arrayListOf(),
    var onClickSeeMoreListener: ((MomentModelV?) -> Unit)? = null,
    var onClickLinkListener: ((url: String) -> Unit)? = null
) : BaseViewItem {

    fun model2ModelV(bm: MomentModel): MomentModelV {
        return MomentModelV().copy(
            id = bm.id,
            channelName = bm.channelName,
            userId = bm.userId,
            posterName = bm.posterName,
            avatar = bm.avatar,
            creatorProfileId = bm.creatorProfileId,
            creatorProfileName = bm.creatorProfileName,
            createdAt = bm.createdAt,
            content = bm.content,
            taggedUsers = bm.taggedUsers,
            imgUrls = bm.imgUrls,
            commentCount = bm.commentCount,
            thisUserCommented = bm.thisUserCommented,
            likeCount = bm.likeCount,
            thisUserLiked = bm.thisUserLiked,
            likedUserIds = bm.likedUserIds
        )
    }

    override fun getViewType(): Int = R.layout.item_moment_feed_1_image

    override fun getItemId(): String = id.toString()

}