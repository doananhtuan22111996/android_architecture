package vn.geekup.data.model.user

import vn.geekup.data.model.general.BaseVO
import vn.geekup.domain.model.user.TokenModel

data class TokenVO(val token: String? = "") : BaseVO<TokenModel>() {

    override fun vo2Model(): TokenModel = TokenModel(token = token)
}