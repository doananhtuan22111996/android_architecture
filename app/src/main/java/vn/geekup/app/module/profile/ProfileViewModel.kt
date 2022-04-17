package vn.geekup.app.module.profile

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
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

    /**
     * @see localUserEngagements -> List User Profile
     * @see pagingState -> Show Indicator Load more List Profile
     * @see isLoading -> Show loading state when executing func
     * @method getUserInfoServer -> Add User Info to List -> position 0
     * @method getUserIndicator -> Add User Indicator to List -> position 1
     * @method getUserIndicatorNewFeedsActive -> Add User Event to List -> position 2
     * @method getUserEngagements -> Add User Engagement to List -> position last list
     */

    val logout: MutableLiveData<Boolean> = MutableLiveData()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun logout() {
        isLoading.value = true
        authUseCase.logout().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Logout Success -> Remove Auth Token")
                logout.value = true
                isLoading.value = false
            }, {
                Timber.d("Logout Failure -> nothing")
                logout.value = false
                isLoading.value = false
//                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }
}