package vn.geekup.app.data.model.user

import vn.geekup.app.data.model.general.BaseVO
import vn.geekup.app.domain.model.user.UserEngagementModel
import vn.geekup.app.domain.model.user.UserEngagementType

data class UserEngagementVO(
    val type: String? = "",
    val momentId: Int = 0,
    val momentDescription: String? = "",
    val momentChannelName: String? = "",
    val momentCreatorProfileName: String? = "",
    val aliaPoint: Int = 0,
    val createdAt: String? = ""
) : BaseVO<UserEngagementModel>() {

    override fun vo2Model(): UserEngagementModel = UserEngagementModel(
        type = when (type) {
            UserEngagementType.Like().type -> UserEngagementType.Like()
            UserEngagementType.Comment().type -> UserEngagementType.Comment()
            else -> UserEngagementType.Created()
        },
        momentId = momentId,
        momentDescription = momentDescription,
        momentChannelName = momentChannelName,
        momentCreatorProfileName = momentCreatorProfileName,
        aliaPoint = aliaPoint,
        createdAt = createdAt
    )
}