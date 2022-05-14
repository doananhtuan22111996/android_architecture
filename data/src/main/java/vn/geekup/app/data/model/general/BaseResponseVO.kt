package vn.geekup.app.data.model.general

abstract class BaseResponseVO<BV>(
    val meta: MetaData? = MetaData(),
) {
    data class MetaData(
        val status: String? = "",
        val statusCode: Int? = 0,
        val message: String? = "",
    )
}

data class ObjectResponseVO<BV>(val data: BV? = null) : BaseResponseVO<BV>()

data class ListResponseVO<BV>(
    var items: ArrayList<BV>? = arrayListOf(),
    val limit: Int? = 0,
    val nextCursor: String? = ""
)
