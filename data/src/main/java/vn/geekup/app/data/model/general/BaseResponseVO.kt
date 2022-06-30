package vn.geekup.app.data.model.general

import com.google.gson.annotations.SerializedName

abstract class BaseResponse<BV>(
    val meta: MetaData? = MetaData(),
) {
    data class MetaData(
        val status: String? = "",
        val statusCode: Int? = 0,
        val message: String? = "",
        @SerializedName("next_page")
        val nextPage : Int? = 0,
    )
}

data class ObjectResponse<BV>(val data: BV? = null) : BaseResponse<BV>()

data class ListResponse<BV>(
    @SerializedName("data")
    var items: ArrayList<BV>? = arrayListOf(),
    val metadata: BaseResponse.MetaData? = BaseResponse.MetaData(),
    val limit: Int? = 0,
    val nextCursor: String? = ""
)
