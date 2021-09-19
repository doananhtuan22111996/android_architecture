package vn.geekup.app.module.profile

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.base.BaseViewModel
import vn.geekup.app.domain.model.general.SortType
import vn.geekup.app.domain.dto.UserEngagementRequestBody
import vn.geekup.app.domain.throwable.ServerErrorException
import vn.geekup.app.domain.usecase.AuthUseCase
import vn.geekup.app.domain.usecase.UserUseCase
import vn.geekup.app.model.PagingState
import vn.geekup.app.model.user.UserEventModelV
import vn.geekup.app.model.user.UserIndicatorModelV
import vn.geekup.app.model.user.UserInfoModelV
import vn.geekup.app.network.NetworkChange
import vn.geekup.app.utils.KEY_PAGING_LIMIT_20
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    networkChange: NetworkChange,
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase
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

    val userEngagements: MutableLiveData<ArrayList<BaseViewItem>> = MutableLiveData()
    val pagingState: MutableLiveData<PagingState> = MutableLiveData()
    val logout: MutableLiveData<Boolean> = MutableLiveData()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var nextCursorUserEngagement: String? = null
    private val localUserEngagements: ArrayList<BaseViewItem> = arrayListOf()

    fun getUserInfoServer() {
        isLoading.value = true
        userUseCase.getUserInfoServer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // Add User Info to List
                localUserEngagements.add(UserInfoModelV().model2ModelV(it))
                getUserIndicator()
            }, {
                executingServerErrorException(it as? ServerErrorException)

            }).push()
    }

    fun getUserEngagements() {
        if (nextCursorUserEngagement?.isEmpty() == true) return
        pagingState.value =
            if (nextCursorUserEngagement != null) PagingState.LoadingMore else PagingState.Loaded
        userUseCase.getUserEngagements(
            UserEngagementRequestBody(
                cursor = nextCursorUserEngagement,
                limit = KEY_PAGING_LIMIT_20,
                sort = SortType.DESC()
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                nextCursorUserEngagement = it.nextCursor
                it.items?.toArrayUserEngagementModelV { results ->
                    // Add User Engagement to List
                    localUserEngagements.addAll(results)
                    userEngagements.value = localUserEngagements
                }
                pagingState.value = PagingState.Loaded
                isLoading.value = false
            }, {
                pagingState.value = PagingState.Loaded
                isLoading.value = false
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

    fun reloadProfileInfo() {
        localUserEngagements.clear()
        nextCursorUserEngagement = null
    }

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
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

    fun eventUserIndicatorActive(
        position: Int = 0,
        executingFunc: ((UserEventModelV, position: Int) -> Unit)? = null
    ) {
        val userEventModel = localUserEngagements[position] as? UserEventModelV
        userEventModel?.isEnableIndicator = when (userEventModel?.isEnableIndicator) {
            true -> false
            else -> true
        }
        localUserEngagements[position] = userEventModel as BaseViewItem
        executingFunc?.invoke(userEventModel, position)
    }

    private fun getUserIndicator() {
        userUseCase.getUserIndicator()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // Add User Indicator to List
                localUserEngagements.add(UserIndicatorModelV().model2ModelV(it))
                getUserIndicatorNewFeedsActive()
            }, {
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

    private fun getUserIndicatorNewFeedsActive() {
        authUseCase.getUserIndicatorActive()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // Add User Event to List
                localUserEngagements.add(UserEventModelV(isEnableIndicator = it))
                getUserEngagements()
            }, {
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }
}