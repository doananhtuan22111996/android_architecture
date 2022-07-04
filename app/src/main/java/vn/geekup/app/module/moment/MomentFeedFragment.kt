package vn.geekup.app.module.moment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.geekup.app.R
import vn.geekup.app.base.BaseFragment
import vn.geekup.app.base.list.PagingLoadStateAdapter
import vn.geekup.app.databinding.FragmentMomentFeedBinding
import vn.geekup.app.module.main.MainFragment
import vn.geekup.app.utils.*


class MomentFeedFragment : BaseFragment<MomentViewModel, FragmentMomentFeedBinding>() {

    private lateinit var adapterPaging: MomentFeedPagingAdapter

    override val viewModel: MomentViewModel by viewModel()

    override fun initViewModelByActivityLifecycle(): Boolean = true

    override fun provideViewBinding(parent: ViewGroup): FragmentMomentFeedBinding {
        return FragmentMomentFeedBinding.inflate(layoutInflater, parent, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel.getFlowMomentFeeds(date = dateFilter)
        viewModel.getPagingMomentFeeds()
        initAdapter()
    }

    override fun onInitLayout(view: View, savedInstanceState: Bundle?) {
        baseActivity.setAppColorStatusBar(R.color.color_white)
        (parentFragment?.parentFragment as? MainFragment)?.bottomNavigationState(true)
        initRecyclerView()
        initRefreshLayout()
    }

    override fun bindViewModel() {
        super.bindViewModel()

        viewModel.pagingMoment.observe(this) {
            lifecycleScope.launchWhenCreated {
                adapterPaging.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapterPaging.loadStateFlow.collect { loadStates ->
                fragmentBinding.swMoments.isRefreshing =
                    loadStates.mediator?.refresh is LoadState.Loading
            }
        }

//        viewModel.moments.observe(this) {
//            fragmentBinding.isMomentEmpty = it.isEmpty()
//            adapter.addAllItemsWithDiffUtils(it)
//            fragmentBinding.swMoments.isRefreshing = false
//        }
//
//        viewModel.pagingState.observe(this) {
//            Timber.d("Loadmore: $it")
//            adapter.setNetworkState(it)
//        }
    }

    private fun initAdapter() {
        adapterPaging = MomentFeedPagingAdapter()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        fragmentBinding.rvMoments.layoutManager = layoutManager
        fragmentBinding.rvMoments.itemAnimator = DefaultItemAnimator()
        fragmentBinding.rvMoments.adapter =
            adapterPaging.withLoadStateFooter(footer = PagingLoadStateAdapter())
    }

    private fun initRefreshLayout() {
        fragmentBinding.swMoments.setOnRefreshListener {
            adapterPaging.refresh()
        }
    }

}
