package vn.geekup.app.data.model.moment

import vn.geekup.app.data.model.general.BaseVO
import vn.geekup.app.domain.model.moment.ImgUrlModel
import vn.geekup.app.domain.model.moment.UrlModel

data class UrlVO(
    val x1: String? = "",
    val x2: String? = "",
    val x3: String? = ""
) : BaseVO<UrlModel>() {

    override fun vo2Model(): UrlModel = UrlModel(x1 = x1, x2 = x2, x3 = x3)
}

data class ImgUrlVO(
    val original: String? = "",
    val hero: UrlVO? = UrlVO(),
    val square: UrlVO? = UrlVO(),
    val landscape: UrlVO? = UrlVO()
) : BaseVO<ImgUrlModel>() {

    override fun vo2Model(): ImgUrlModel = ImgUrlModel(
        original = original,
        hero = hero?.vo2Model(),
        square = square?.vo2Model(),
        landscape = landscape?.vo2Model()
    )
}