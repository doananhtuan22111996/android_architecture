package vn.geekup.app.data.model.user

import vn.geekup.app.data.model.general.BaseVO
import vn.geekup.app.domain.model.user.TokenModel

data class TokenVO(val token: String? = "") : BaseVO<TokenModel>() {

    override fun vo2Model(): TokenModel = TokenModel(token = token)
}