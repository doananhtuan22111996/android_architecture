package vn.geekup.app.module.login

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import vn.geekup.app.base.BaseViewModel
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.model.user.RoleAuthority
import vn.geekup.app.domain.throwable.ServerErrorException
import vn.geekup.app.domain.usecase.AuthUseCase
import vn.geekup.app.network.NetworkChange
import javax.inject.Inject

class LoginViewModel @Inject constructor(networkChange: NetworkChange, private val authUseCase: AuthUseCase) :
    BaseViewModel(networkChange) {

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val login: MutableLiveData<Boolean> = MutableLiveData(false)

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

    fun loginOTable(otableToken: String, roleAuthority: String = RoleAuthority.User().roleName) {
        isLoading.value = true
        authUseCase.loginOTable(OTableRequestBody(otableToken, roleAuthority))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                login.value = it
                isLoading.value = false
            }, {
                isLoading.value = false
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }
}