package vn.geekup.app.module.root

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import vn.geekup.app.base.BaseViewModel
import vn.geekup.app.domain.model.user.UserInfoModel
import vn.geekup.app.domain.throwable.ServerErrorException
import vn.geekup.app.domain.usecase.AuthUseCase
import vn.geekup.app.domain.usecase.UserUseCase
import vn.geekup.app.model.user.UserIndicatorModelV
import vn.geekup.app.network.NetworkChange
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    networkChange: NetworkChange,
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase
) : BaseViewModel(networkChange) {

    val isLoggedIn: MutableLiveData<Boolean> = MutableLiveData()
    val userInfo: MutableLiveData<UserInfoModel> = MutableLiveData()
    val userIndicator: MutableLiveData<UserIndicatorModelV> = MutableLiveData()
    val userIndicationActive: MutableLiveData<Boolean> = MutableLiveData()

    private var isRootViewReady: Boolean = false

    fun isRootViewReadyFunc(): Boolean {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                isRootViewReady = true
            }
        return isRootViewReady
    }

    fun getToken() {
        authUseCase.getToken().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Auth Token: $it")
                isLoggedIn.value = it.isNotEmpty()
                if (it.isNotEmpty()) {
                    getUserInfoServer()
                }
            }, {
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

    fun resetUserInfoInShareWrapper() {
        authUseCase.forceToLogin()
    }

    fun getUserInfoServer() {
        userUseCase.getUserInfoServer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                userInfo.value = it
            }, {
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

    fun getUserIndicator() {
        userUseCase.getUserIndicator()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                userIndicator.value = UserIndicatorModelV().model2ModelV(it)
                getUserIndicatorActive()
            }, {
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }


    fun saveUserIndicatorNewFeedsActive(isEnable : Boolean) {
        authUseCase.saveUserIndicatorActive(isEnable)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Save user Indicator Success -> ")
            }, {
                Timber.d("Save user Indicator Failure")
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

    private fun getUserIndicatorActive() {
        authUseCase.getUserIndicatorActive()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                userIndicationActive.value = it
            }, {
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

}