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
import vn.geekup.app.model.user.UserEventModelV
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

    private lateinit var adapter: ProfileAdapter
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    override val viewModel: ProfileViewModel by viewModels()

    override fun provideViewBinding(parent: ViewGroup): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater, parent, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.reloadProfileInfo()
        viewModel.getUserInfoServer()
        initAdapter()
    }

    override fun onInitLayout(view: View, savedInstanceState: Bundle?) {
        baseActivity.setAppColorStatusBar(R.color.color_white)
        (parentFragment?.parentFragment as? MainFragment)?.bottomNavigationState(true)
        fragmentBinding.fragment = this
        fragmentBinding.isUserEngagementEmpty = false
        fragmentBinding.layoutToolbar.tvTitle.text = getString(R.string.your_profile)
        initRecyclerView()
    }

    override fun bindViewModel() {
        super.bindViewModel()

        viewModel.logout.observe(this, {
            Toast.makeText(context, "Logout Success: $it", Toast.LENGTH_SHORT).show()
            (baseActivity as? RootActivity)?.popToLogin()
        })

        viewModel.userEngagements.observe(this, {
            fragmentBinding.isUserEngagementEmpty = it.size == 0
            adapter.addAllItemsWithDiffUtils(it)
        })

        viewModel.pagingState.observe(this, {
            adapter.setNetworkState(it)
        })

        viewModel.isLoading.observe(this, {
            fragmentBinding.swUser.isRefreshing = it
        })

    }

    private fun initAdapter() {
        adapter = ProfileAdapter(this::onUserItemListener)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    Timber.d("ItemCount: $totalItemsCount")
                    viewModel.getUserEngagements()
                }
            }
        fragmentBinding.rvUsers.layoutManager = layoutManager
        fragmentBinding.rvUsers.adapter = adapter
        fragmentBinding.rvUsers.addOnScrollListener(endlessRecyclerViewScrollListener)
        fragmentBinding.swUser.setOnRefreshListener {
            viewModel.reloadProfileInfo()
            viewModel.getUserInfoServer()
        }
    }

    private fun onUserItemListener(data: BaseViewItem, position: Int, actions: ProfileActions) {
        when (actions) {
            ProfileActions.Logout -> viewModel.logout()
            ProfileActions.IndicatorActive -> viewModel.eventUserIndicatorActive(
                position,
                this::executingFuncIndicator
            )
            ProfileActions.GiveNow -> {
                try {
                    val intent: Intent? =
                        baseActivity.packageManager.getLaunchIntentForPackage(vn.geekup.app.data.BuildConfig.SPARROW_ID)
                    startActivity(intent)
                } catch (e: Exception) {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(vn.geekup.app.data.BuildConfig.SPARROW_URL_STORE))
                    startActivity(browserIntent)
                }

            }
            ProfileActions.MomentDetail -> {
                val userEngagementModelV = data as? UserEngagementModelV
                navController.navigate(
                    R.id.momentDetailFragment,
                    bundleOf(KEY_ARGUMENT_FRAGMENT to userEngagementModelV?.momentId)
                )
            }
            else -> Unit
        }
    }

    private fun executingFuncIndicator(userEventModelV: UserEventModelV, position: Int) {
        (baseActivity as? RootActivity)?.onUserIndicatorMomentFeedActive(userEventModelV.isEnableIndicator)
        adapter.setItemPositionChanged(userEventModelV, position)
    }

}