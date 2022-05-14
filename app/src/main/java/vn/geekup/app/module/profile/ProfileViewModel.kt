package vn.geekup.app.module.profile

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import vn.geekup.app.base.BaseViewModel
import vn.geekup.app.domain.usecase.AuthUseCase
import vn.geekup.app.network.NetworkChange
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    networkChange: NetworkChange,
    private val authUseCase: AuthUseCase,
) : BaseViewModel(networkChange) {

    val logout: MutableLiveData<Boolean> = MutableLiveData()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
}
