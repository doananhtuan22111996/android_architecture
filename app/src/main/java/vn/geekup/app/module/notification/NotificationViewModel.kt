package vn.geekup.app.module.notification

import vn.geekup.app.base.BaseViewModel
import vn.geekup.app.domain.usecase.AuthUseCase
import vn.geekup.app.network.NetworkChange
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
    networkChange: NetworkChange,
    //Todo change notification use-case
    authUseCase: AuthUseCase
) : BaseViewModel(networkChange) {
}