package vn.geekup.app.model.moment

import com.google.gson.annotations.SerializedName
import vn.geekup.app.R
import vn.geekup.app.base.BaseViewItem
import vn.geekup.domain.model.moment.MomentModel

data class MomentModelV(
    val id: Int? = 0,
    val channelName: String? = "",
    @SerializedName("title")
    val posterName: String? = "",
) : BaseViewItem {

    fun model2ModelV(bm: MomentModel): MomentModelV {
        return MomentModelV().copy(
            id = bm.id,
            channelName = bm.channelName,
            posterName = bm.posterName,
        )
    }

    override fun getViewType(): Int = R.layout.item_moment_feed_1_image

    override fun getItemId(): String = id.toString()

}