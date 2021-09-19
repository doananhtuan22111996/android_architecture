package vn.geekup.app.module.notification

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.geekup.app.base.BaseFragment
import vn.geekup.app.databinding.FragmentNotificationBinding

@AndroidEntryPoint
class NotificationFragment : BaseFragment<NotificationViewModel, FragmentNotificationBinding>() {

    override val viewModel: NotificationViewModel by viewModels()

    override fun provideViewBinding(parent: ViewGroup): FragmentNotificationBinding =
        FragmentNotificationBinding.inflate(layoutInflater, parent, true)

    override fun onInitLayout(view: View, savedInstanceState: Bundle?) {
        // Todo handle func
    }
}