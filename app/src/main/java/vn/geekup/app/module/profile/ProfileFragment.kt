package vn.geekup.app.module.profile

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import vn.geekup.app.R
import vn.geekup.app.base.BaseFragment
import vn.geekup.app.databinding.FragmentProfileBinding
import vn.geekup.app.module.root.RootActivity
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.geekup.app.module.main.MainFragment
import vn.geekup.app.utils.setAppColorStatusBar

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    override val viewModel: ProfileViewModel by viewModels()

    override fun provideViewBinding(parent: ViewGroup): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater, parent, true)

    override fun onInitLayout(view: View, savedInstanceState: Bundle?) {
        baseActivity.setAppColorStatusBar(R.color.color_white)
        (parentFragment?.parentFragment as? MainFragment)?.bottomNavigationState(true)
        fragmentBinding.fragment = this
        fragmentBinding.isUserEngagementEmpty = false
        fragmentBinding.layoutToolbar.tvTitle.text = getString(R.string.your_profile)
    }

    override fun bindViewModel() {
        super.bindViewModel()

        viewModel.logout.observe(this) {
            Toast.makeText(context, "Logout Success: $it", Toast.LENGTH_SHORT).show()
            (baseActivity as? RootActivity)?.popToLogin()
        }

        viewModel.isLoading.observe(this) {
            fragmentBinding.swUser.isRefreshing = it
        }

    }

}