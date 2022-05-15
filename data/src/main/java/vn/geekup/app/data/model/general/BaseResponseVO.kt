package vn.geekup.app.data.model.general

abstract class BaseResponse<BV>(
    val meta: MetaData? = MetaData(),
) {
    data class MetaData(
        val status: String? = "",
        val statusCode: Int? = 0,
        val message: String? = "",
    )
}

data class ObjectResponse<BV>(val data: BV? = null) : BaseResponse<BV>()

data class ListResponse<BV>(
    var items: ArrayList<BV>? = arrayListOf(),
    val limit: Int? = 0,
    val nextCursor: String? = ""
)
