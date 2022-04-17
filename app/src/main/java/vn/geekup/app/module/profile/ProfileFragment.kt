package vn.geekup.app.module.profile

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import vn.geekup.app.R
import vn.geekup.app.base.BaseFragment
import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.databinding.FragmentProfileBinding
import vn.geekup.app.module.root.RootActivity
import vn.geekup.app.utils.EndlessRecyclerViewScrollListener
import android.content.Intent
import android.net.Uri
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import vn.geekup.app.model.user.UserEngagementModelV
import vn.geekup.app.module.main.MainFragment
import vn.geekup.app.utils.KEY_ARGUMENT_FRAGMENT
import vn.geekup.app.utils.setAppColorStatusBar

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    override val viewModel: ProfileViewModel by viewModels()

    override fun provideViewBinding(parent: ViewGroup): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater, parent, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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