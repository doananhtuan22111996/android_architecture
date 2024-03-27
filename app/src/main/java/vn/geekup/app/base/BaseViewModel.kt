package vn.geekup.app.base

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber
import vn.geekup.data.Config.ErrorCode.CODE_999
import vn.geekup.domain.model.general.ResultModel
import vn.geekup.app.network.NetworkChange

open class BaseViewModel(
    networkChange: NetworkChange,
//  protected val authUseCase: AuthUseCase,
) : ViewModel() {

    val networkChangeState = networkChange.networkState()
    private val fullScreenLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val fullScreenLoadingState: LiveData<Boolean> = fullScreenLoadingLiveData

    val forceToLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    val errorServerState: MutableLiveData<ResultModel.ServerErrorException?> = MutableLiveData()

    fun fullScreenLoading(isLoading: Boolean) {
        fullScreenLoadingLiveData.postValue(isLoading)
    }

    open fun loadArgumentsBundle(bundle: Bundle?) {
        //Do Nothing, handle in subclass
    }

    open fun loadIntentBundle(intent: Intent?) {
        //Do Nothing, handle in subclass
    }

    fun executingServerErrorException(serverError: ResultModel.ServerErrorException?) {
        if (serverError == null) return
        forceToLogin.value = serverError.code == CODE_999
        errorServerState.value = serverError
        Timber.d("Executed Server Failure: ${serverError.message}")
    }

}