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

sealed class ResultModel<out BM> {
    data class ResultListObj<BM>(
        var items: ArrayList<BM>? = arrayListOf(),
        val limit: Int? = 0,
        val nextCursor: String? = ""
    ) : ResultModel<BM>()

    data class ResultObj<out BM>(val data: BM? = null) : ResultModel<BM>()

    data class ServerErrorException(val code: Int? = 0, val message: String? = "") :
        ResultModel<Nothing>()
}