package vn.geekup.app.data.model.user

import com.google.gson.annotations.SerializedName
import vn.geekup.app.data.model.general.BaseVO
import vn.geekup.app.domain.model.user.OTableModel
import vn.geekup.app.domain.model.user.RoleAuthority

data class OTableVO(
    val token: String? = "",
    @SerializedName("access_token")
    val accessToken: String? = "",
    val currentAuthority: String? = "",
    val refreshToken: String? = ""
) : BaseVO<OTableModel>() {

    override fun vo2Model(): OTableModel = OTableModel(
        token = if (token?.trim()?.isNotEmpty() == true) token else accessToken,
        currentAuthority = when (currentAuthority) {
            RoleAuthority.User().roleName -> RoleAuthority.User()
            else -> RoleAuthority.Admin()
        },
        refreshToken = refreshToken
    )
}