package vn.geekup.app.data.remote

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import timber.log.Timber
import vn.geekup.app.data.Config.ErrorCode.CODE_200
import vn.geekup.app.data.Config.ErrorCode.CODE_201
import vn.geekup.app.data.Config.ErrorCode.CODE_204
import vn.geekup.app.data.Config.ErrorCode.CODE_999
import vn.geekup.app.data.model.general.ObjectResponse
import vn.geekup.app.domain.model.general.ResultModel

abstract class NetworkBoundService<RequestType, ResultType> {

    private val tag = NetworkBoundService::class.java.name

    private var result: ResultModel<ResultType>? = null

    fun build() = flow {
        emit(ResultModel.Loading)
        emit(
            fetchFromNetwork() ?: ResultModel.ServerErrorException(
                code = CODE_999,
                message = "Somethings wrong!"
            )
        )
    }.flowOn(Dispatchers.IO)

    /**
     * This Func handle response from Network [fetchFromNetwork].
     */
    private suspend fun fetchFromNetwork(): ResultModel<ResultType>? {
        Timber.i(tag, "Fetch data from network")

        val apiResponse = onApi()

        if (apiResponse.isSuccessful) {
            val body = apiResponse.body()
            result = when (apiResponse.code()) {
                CODE_200, CODE_201, CODE_204 -> {
                    processResponse(body)
                }
                else -> {
                    ResultModel.ServerErrorException(
                        code = apiResponse.code(),
                        message = apiResponse.message()
                    )
                }
            }
        } else {
            result = Gson().fromJson(
                apiResponse.errorBody()?.toString(),
                ResultModel.ServerErrorException::class.java
            )
        }
        return result
    }

    abstract suspend fun onApi(): Response<ObjectResponse<RequestType>>

    abstract suspend fun processResponse(request: ObjectResponse<RequestType>?): ResultModel.Success<ResultType>?

}