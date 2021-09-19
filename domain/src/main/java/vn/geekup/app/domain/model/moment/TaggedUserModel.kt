package vn.geekup.app.domain.model.moment

import vn.geekup.app.domain.model.general.BaseModel

data class TaggedUserModel(
    val id: Int? = 0,
    val shortName: String? = "",
    val avatar: String? = "",
    val alias: String? = ""
) : BaseModel()