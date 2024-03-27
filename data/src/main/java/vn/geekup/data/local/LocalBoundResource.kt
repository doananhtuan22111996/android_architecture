package vn.geekup.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import vn.geekup.data.Config.ErrorCode.CODE_999
import vn.geekup.domain.model.general.ResultModel

abstract class LocalBoundResource<RequestType, ResultType> {

    private val TAG = LocalBoundResource::class.java.name

    private var result: ResultModel<ResultType>? = null

    fun build() = flow {
        emit(ResultModel.Loading)
        emit(
            fetchFromDatabase() ?: ResultModel.ServerErrorException(
                message = "Somethings wrong",
                code = CODE_999
            )
        )
    }.flowOn(Dispatchers.IO)


    private suspend fun fetchFromDatabase(): ResultModel<ResultType>? {
        result = try {

            val response = loadDB()

            Timber.e("Data fetched from Database ${if (response != null) "Success" else "Failure"}")

            processResponse(response)
        } catch (e: Exception) {
            Timber.e("Data fetched from Database Error: ${e.message}")
            val errorMsg = "Not match in Database"
            ResultModel.ServerErrorException(
                message = errorMsg,
                code = CODE_999
            )
        }
        return result
    }

    abstract suspend fun loadDB(): RequestType

    abstract suspend fun processResponse(request: RequestType?): ResultModel.Success<ResultType>?
}
