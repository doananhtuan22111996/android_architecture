package vn.geekup.app.data.model.moment

import com.google.gson.annotations.SerializedName
import vn.geekup.app.data.model.general.BaseVO
import vn.geekup.app.domain.model.moment.MomentModel

data class MomentVO(
    val id: Int? = 0,
    @SerializedName("username")
    val channelName: String? = "",
    val posterName: String? = "",

    ) : BaseVO<MomentModel>() {

    override fun vo2Model(): MomentModel {
        return MomentModel(
            id = id ?: 0,
            channelName = channelName ?: "",
            posterName = posterName ?: "",
        )
    }
}
