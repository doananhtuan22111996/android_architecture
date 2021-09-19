package vn.geekup.app.data.model.user

import vn.geekup.app.data.model.general.BaseVO
import vn.geekup.app.domain.model.user.RoleAuthority
import vn.geekup.app.domain.model.user.UserInfoModel

data class UserInfoVO(
    val id: Int = 0,
    val email: String? = "",
    val shortName: String? = "",
    val currentAuthority: String? = "",
    val currentLevelId: Int = 0,
    val isFormFill: Int? = 0, //1, 2, 3 , 5 value Enum
    val firstDay: String? = "",
    val avatar: String? = "",
    val avatarMediumSize: String? = "",
    val avatarBigSize: String? = "",
    val avatarSmallSize: String? = "",
    val permanentAddress: String? = "",
    val isWelcomeKitConfirm: Boolean = false,
    val aliaPoint: Int = 0,
    val isCheckAdventureMap: Int? = 0, //1: false, 2: true
    val adventureIcon: String? = ""
) : BaseVO<UserInfoModel>() {

    override fun vo2Model(): UserInfoModel = UserInfoModel(
        id = id,
        email = email ?: "",
        shortName = shortName ?: "",
        currentAuthority = when (currentAuthority) {
            RoleAuthority.User().roleName -> RoleAuthority.User()
            else -> RoleAuthority.Admin()
        },
        currentLevelId = currentLevelId,
        isFormFill = isFormFill == 1,
        firstDay = firstDay ?: "",
        avatar = avatar ?: "",
        avatarMediumSize = avatarMediumSize ?: "",
        avatarBigSize = avatarBigSize ?: "",
        avatarSmallSize = avatarSmallSize ?: "",
        permanentAddress = permanentAddress ?: "",
        isWelcomeKitConfirm = isWelcomeKitConfirm,
        aliaPoint = aliaPoint,
        isCheckAdventureMap = isCheckAdventureMap == 1,
        adventureIcon = adventureIcon ?: ""
    )
}