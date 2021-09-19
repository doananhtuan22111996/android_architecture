package vn.geekup.app.model.user

import vn.geekup.app.R
import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.domain.model.user.RoleAuthority
import vn.geekup.app.domain.model.user.UserInfoModel

data class UserInfoModelV(
    val id: Int = 0,
    val email: String = "",
    val shortName: String = "",
    val currentAuthority: RoleAuthority = RoleAuthority.User(),
    val currentLevelId: Int = 0,
    val isFormFill: Boolean = false,
    val firstDay: String = "",
    val avatar: String = "",
    val avatarMediumSize: String = "",
    val avatarBigSize: String = "",
    val avatarSmallSize: String = "",
    val permanentAddress: String = "",
    val isWelcomeKitConfirm: Boolean = false,
    val aliaPoint: Int = 0,
    val isCheckAdventureMap: Boolean = false,
    val adventureIcon: String = ""
) : BaseViewItem {

    override fun getViewType(): Int = R.layout.item_user_info

    override fun getItemId(): String = "user_info_id"

    fun model2ModelV(bm: UserInfoModel): UserInfoModelV {
        return UserInfoModelV().copy(
            id = bm.id,
            email = bm.email,
            shortName = bm.shortName,
            currentAuthority = bm.currentAuthority,
            currentLevelId = bm.currentLevelId,
            isFormFill = bm.isFormFill,
            firstDay = bm.firstDay,
            avatar = bm.avatar,
            avatarMediumSize = bm.avatarMediumSize,
            avatarBigSize = bm.avatarBigSize,
            avatarSmallSize = bm.avatarSmallSize,
            permanentAddress = bm.permanentAddress,
            isWelcomeKitConfirm = bm.isWelcomeKitConfirm,
            aliaPoint = bm.aliaPoint,
            isCheckAdventureMap = bm.isCheckAdventureMap,
            adventureIcon = bm.adventureIcon
        )
    }
}