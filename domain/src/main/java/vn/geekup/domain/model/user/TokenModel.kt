package vn.geekup.domain.model.user

import vn.geekup.domain.model.general.BaseModel

data class TokenModel(val token: String? = "") : BaseModel()