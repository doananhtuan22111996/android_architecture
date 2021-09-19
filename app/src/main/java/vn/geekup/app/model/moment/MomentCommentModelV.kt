package vn.geekup.app.model.moment

import vn.geekup.app.R
import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.domain.model.moment.MomentCommentModel

data class MomentCommentModelV(
    val id: Int? = 0,
    val commenterAvatarUrl: String? = "",
    val commenterName: String? = "",
    val content: String? = "",
    val createdAt: String? = ""
) : BaseViewItem {

    override fun getViewType(): Int = R.layout.item_moment_comment

    override fun getItemId(): String = id.toString()

    fun model2ModelV(bm: MomentCommentModel): MomentCommentModelV {
        return MomentCommentModelV().copy(
            id = bm.id,
            commenterAvatarUrl = bm.commenterAvatarUrl,
            commenterName = bm.commenterName,
            content = bm.content,
            createdAt = bm.createdAt
        )
    }
}