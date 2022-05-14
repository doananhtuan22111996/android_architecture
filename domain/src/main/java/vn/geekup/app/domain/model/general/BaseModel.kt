package vn.geekup.app.domain.model.general

open class BaseModel

sealed class ResultModel<out R> {
    data class Success<out R>(
        val data: R? = null,
        val limit: Int? = 0,
        val nextCursor: String? = ""
    ) : ResultModel<R>()

    data class ServerErrorException(val code: Int? = 0, val message: String? = "") :
        ResultModel<Nothing>()

    object Loading : ResultModel<Nothing>()

}