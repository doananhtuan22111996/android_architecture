package vn.geekup.app.module.notification

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import vn.geekup.app.base.BaseFragment
import vn.geekup.app.databinding.FragmentNotificationBinding

class NotificationFragment : BaseFragment<NotificationViewModel, FragmentNotificationBinding>() {

    override fun provideViewModelClass(): Class<NotificationViewModel> =
        NotificationViewModel::class.java

    override fun provideViewBinding(parent: ViewGroup): FragmentNotificationBinding =
        FragmentNotificationBinding.inflate(layoutInflater, parent, true)

    override fun onInitLayout(view: View, savedInstanceState: Bundle?) {
        // Todo handle func
    }
}