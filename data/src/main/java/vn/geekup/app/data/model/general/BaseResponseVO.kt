package vn.geekup.app.data.model.general

data class MetaData(
    val status: String? = "",
    val statusCode: Int? = 0,
    val message: String? = "",
)

data class ListResponseVO<BV>(
    var items: ArrayList<BV>? = arrayListOf(),
    val limit: Int? = 0,
    val nextCursor: String? = ""
)

data class BaseResponseVO<BV>(
    val meta: MetaData? = MetaData(),
    val limit: Int? = 0,
    val nextCursor: String? = "",
    var items: ArrayList<BV>? = arrayListOf(),
    val data: BV? = null
)
