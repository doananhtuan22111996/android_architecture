package vn.geekup.app.domain.model.moment

import vn.geekup.app.domain.model.general.BaseModel

data class UrlModel(
    val x1: String? = "",
    val x2: String? = "",
    val x3: String? = ""
) : BaseModel()

class ImgUrlModel(
    val original: String? = "",
    val hero: UrlModel? = UrlModel(),
    val square: UrlModel? = UrlModel(),
    val landscape: UrlModel? = UrlModel()
) : BaseModel()