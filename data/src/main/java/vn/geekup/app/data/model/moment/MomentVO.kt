package vn.geekup.app.data.model.moment

import vn.geekup.app.data.model.general.BaseVO
import vn.geekup.app.domain.model.moment.ImgUrlModel
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.domain.model.moment.TaggedUserModel

data class MomentVO(
    val id: Int? = 0,
    val channelName: String? = "",
    val userId: Int? = 0,
    val posterName: String? = "",
    val avatar: String? = "",
    val creatorProfileId: Int? = 0,
    val creatorProfileName: String? = "",
    val createdAt: String? = "",
    val content: String? = "",
    val taggedUsers: ArrayList<TaggedUserVO>? = arrayListOf(),
    val imgUrls: ArrayList<ImgUrlVO>? = arrayListOf(),
    val commentCount: Int? = 0,
    val thisUserCommented: Boolean = false,
    val likeCount: Int? = 0,
    val thisUserLiked: Boolean = false,
    val likedUserIds: ArrayList<Int>? = arrayListOf()
) : BaseVO<MomentModel>() {

    override fun vo2Model(): MomentModel {
        val momentModel = MomentModel(
            id = id,
            channelName = channelName,
            userId = userId,
            posterName = posterName,
            avatar = avatar,
            creatorProfileId = creatorProfileId,
            creatorProfileName = creatorProfileName,
            createdAt = createdAt,
            content = content,
            commentCount = commentCount,
            thisUserCommented = thisUserCommented,
            likeCount = likeCount,
            thisUserLiked = thisUserLiked,
            likedUserIds = likedUserIds
        )
        val imgUrlModels: ArrayList<ImgUrlModel> = arrayListOf()
        imgUrls?.forEach {
            imgUrlModels.add(it.vo2Model())
        }
        val taggedUserModel: ArrayList<TaggedUserModel> = arrayListOf()
        taggedUsers?.forEach {
            taggedUserModel.add(it.vo2Model())
        }
        momentModel.imgUrls = imgUrlModels
        momentModel.taggedUsers = taggedUserModel
        return momentModel
    }
}
