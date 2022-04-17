package vn.geekup.app.module.root

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import vn.geekup.app.base.BaseViewModel
import vn.geekup.app.domain.usecase.AuthUseCase
import vn.geekup.app.network.NetworkChange
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    networkChange: NetworkChange,
) : BaseViewModel(networkChange) {

    val isLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    private var isRootViewReady: Boolean = false

    fun isRootViewReadyFunc(): Boolean {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                isRootViewReady = true
            }
        return isRootViewReady
    }

}