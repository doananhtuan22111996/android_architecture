package vn.geekup.app.domain.model.user

import vn.geekup.app.domain.model.general.BaseModel

sealed class RoleAuthority {
    data class Admin(val roleName: String = "admin") : RoleAuthority()
    data class User(val roleName: String = "user") : RoleAuthority()
}

data class OTableModel(
    val token: String? = "",
    val currentAuthority: RoleAuthority? = RoleAuthority.User(),
    val refreshToken: String? = ""
) : BaseModel()