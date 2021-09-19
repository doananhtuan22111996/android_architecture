package vn.geekup.app.domain.model.user

import vn.geekup.app.domain.model.general.BaseModel

data class TokenModel(val token: String? = "") : BaseModel()