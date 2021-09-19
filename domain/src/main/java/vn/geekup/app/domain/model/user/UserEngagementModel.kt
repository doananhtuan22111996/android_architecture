package vn.geekup.app.domain.model.user

import vn.geekup.app.domain.model.general.BaseModel

sealed class UserEngagementType {
    data class Like(val type: String = "like") : UserEngagementType()
    data class Comment(val type: String = "comment") : UserEngagementType()
    data class Created(val type: String = "created") : UserEngagementType()
}

class UserEngagementModel(
    val type: UserEngagementType = UserEngagementType.Like(),
    val momentId: Int = 0,
    val momentDescription: String? = "",
    val momentChannelName: String? = "",
    val momentCreatorProfileName: String? = "",
    val aliaPoint: Int = 0,
    val createdAt: String? = ""
) : BaseModel()