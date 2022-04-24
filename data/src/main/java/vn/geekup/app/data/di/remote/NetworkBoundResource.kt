package vn.geekup.app.data.di.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import vn.geekup.app.domain.model.general.ResultModel

/**
 * Constructs [NetworkBoundResource]
 *
 * @param dispatcher
 *            the Coroutines will lunch at Dispatchers.IO Worker Thread.
 * @param RequestType
 *            the type Input data.
 * @param ResultType
 *            the type Output data.
 */

abstract class NetworkBoundResource<RequestType, ResultType>
constructor(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val TAG = NetworkBoundResource::class.java.name

    private val result = MutableLiveData<ResultModel<ResultType>>()

    /**
     *  Init this abstract class [build].
     */
    fun build(): NetworkBoundResource<RequestType, ResultType> {

        result.value = ResultModel.Loading

        CoroutineScope(dispatcher).launch {
            try {
                fetchFromNetwork()
            } catch (e: Exception) {
                Timber.e("$TAG - An error happened: $e")
                setValue(ResultModel.ServerErrorException(404, e.message ?: ""))
            }

        }
        return this
    }

    private fun setValue(newValue: ResultModel<ResultType>) {
        if (result.value != newValue) {
            result.postValue(newValue)
        }
    }

    /**
     * This Func handle response from Network [fetchFromNetwork].
     */
    private suspend fun fetchFromNetwork() {
        Timber.i("$TAG - Fetch data from network")
        val apiResponse = createCall()
        if (apiResponse.isSuccessful) {
            val body = apiResponse.body()
            when (apiResponse.code()) {
                200, 201, 204 -> {
                    if (body == null) {
                        setValue(ResultModel.Success(null))
                    } else {
                        val result = processResponse(body)
                        setValue(ResultModel.Success(result))
                    }
                }
                else -> {
                    setValue(
                        ResultModel.ServerErrorException(
                            apiResponse.code(),
                            apiResponse.message()
                        )
                    )
                }
            }
        } else {
            val response =
                Gson().fromJson(
                    apiResponse.errorBody()?.toString(),
                    ResultModel.ServerErrorException::class.java
                )
            setValue(ResultModel.ServerErrorException(apiResponse.code(), response?.message))
        }
    }

    /**
     * Return a LiveData [asLiveData].
     */
    fun asLiveData(): LiveData<ResultModel<ResultType>> = result

    /**
     * This Func handle data before return to View [processResponse].
     */
    protected abstract fun processResponse(response: RequestType): ResultType?

    /**
     * Call data from network [createCall] (Required).
     */
    protected abstract suspend fun createCall(): Response<RequestType>
}
