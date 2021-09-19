package vn.geekup.app.model.user

import vn.geekup.app.R
import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.domain.model.user.UserEngagementModel
import vn.geekup.app.domain.model.user.UserEngagementType

data class UserEngagementModelV(
    val type: UserEngagementType = UserEngagementType.Like(),
    val momentId: Int = 0,
    val momentDescription: String? = "",
    val momentChannelName: String? = "",
    val momentCreatorProfileName: String? = "",
    val aliaPoint: Int = 0,
    val createdAt: String? = ""
) : BaseViewItem {

    override fun getViewType(): Int = R.layout.item_user_engagement

    override fun getItemId(): String = momentId.toString()

    fun model2ModelV(bm: UserEngagementModel): UserEngagementModelV {
        return UserEngagementModelV().copy(
            type = bm.type,
            momentId = bm.momentId,
            momentDescription = bm.momentDescription,
            momentChannelName = bm.momentChannelName,
            momentCreatorProfileName = bm.momentCreatorProfileName,
            aliaPoint = bm.aliaPoint,
            createdAt = bm.createdAt
        )
    }
}