package vn.geekup.app.module.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import vn.geekup.app.base.BaseViewModel
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.user.RoleAuthority
import vn.geekup.app.domain.usecase.AuthUseCase
import vn.geekup.app.network.NetworkChange
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    networkChange: NetworkChange,
    private val authUseCase: AuthUseCase
) :
    BaseViewModel(networkChange) {

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val login: MutableLiveData<Boolean> = MutableLiveData(false)

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

    fun loginOTable(otableToken: String, roleAuthority: String = RoleAuthority.User().roleName) {
        isLoading.value = true
        viewModelScope.launch {
            authUseCase.loginOTable(OTableRequestBody(otableToken, roleAuthority)).collectLatest {
                Timber.e("Thread View Model Collect: ${Thread.currentThread().name}")
                when (it) {
                    is ResultModel.ResultObj -> {
                        login.value = it.data?.token?.isNotEmpty()
                        isLoading.value = false
                    }
                    is ResultModel.ServerErrorException -> {
                        isLoading.value = false
                        executingServerErrorException(it)
                    }
                    else -> {}
                }
            }
        }
    }
}