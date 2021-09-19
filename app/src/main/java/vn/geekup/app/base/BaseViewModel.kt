package vn.geekup.app.base

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import vn.geekup.app.data.Config.ErrorCode.CODE_999
import vn.geekup.app.domain.throwable.ServerErrorException
import vn.geekup.app.network.NetworkChange

open class BaseViewModel(
    networkChange: NetworkChange,
//  protected val authUseCase: AuthUseCase,
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val networkChangeState = networkChange.networkState()
    private val fullScreenLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val fullScreenLoadingState: LiveData<Boolean> = fullScreenLoadingLiveData

    val forceToLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    val errorServerState: MutableLiveData<ServerErrorException> = MutableLiveData()

    fun fullScreenLoading(isLoading: Boolean) {
        fullScreenLoadingLiveData.postValue(isLoading)
    }

    open fun loadArgumentsBundle(bundle: Bundle?) {
        //Do Nothing, handle in subclass
    }

    open fun loadIntentBundle(intent: Intent?) {
        //Do Nothing, handle in subclass
    }

    protected fun Disposable.push() {
        compositeDisposable.addAll(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun executingServerErrorException(serverError: ServerErrorException?) {
        if (serverError == null) return
        forceToLogin.value = serverError.code == CODE_999
        errorServerState.value = serverError
        Timber.d("Executed Server Failure: ${serverError.message}")
    }

}