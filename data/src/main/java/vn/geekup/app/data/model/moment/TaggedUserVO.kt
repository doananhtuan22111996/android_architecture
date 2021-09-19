package vn.geekup.app.data.model.moment

import vn.geekup.app.data.model.general.BaseVO
import vn.geekup.app.domain.model.moment.TaggedUserModel

class TaggedUserVO(
    val id: Int? = 0,
    val shortName: String? = "",
    val avatar: String? = "",
    val alias: String? = ""
) : BaseVO<TaggedUserModel>() {

    override fun vo2Model(): TaggedUserModel =
        TaggedUserModel(id = id, shortName = shortName, avatar = avatar, alias = alias)
}