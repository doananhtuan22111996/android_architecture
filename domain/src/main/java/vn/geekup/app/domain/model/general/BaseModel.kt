package vn.geekup.app.domain.model.general

open class BaseModel

data class MetaDataModel(
    val status: String? = "",
    val statusCode: Int? = 0,
    val message: String? = "",
)

data class BaseModelListResponse<BM : BaseModel>(
    var items: ArrayList<BM>? = arrayListOf(),
    val limit: Int? = 0,
    val nextCursor: String? = ""
)