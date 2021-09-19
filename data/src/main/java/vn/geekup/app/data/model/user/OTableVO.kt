package vn.geekup.app.data.model.user

import vn.geekup.app.data.model.general.BaseVO
import vn.geekup.app.domain.model.user.OTableModel
import vn.geekup.app.domain.model.user.RoleAuthority

data class OTableVO(
    val token: String? = "",
    val currentAuthority: String? = "",
    val refreshToken: String? = ""
) : BaseVO<OTableModel>() {

    override fun vo2Model(): OTableModel = OTableModel(
        token = token, currentAuthority = when (currentAuthority) {
            RoleAuthority.User().roleName -> RoleAuthority.User()
            else -> RoleAuthority.Admin()
        },
        refreshToken = refreshToken
    )
}