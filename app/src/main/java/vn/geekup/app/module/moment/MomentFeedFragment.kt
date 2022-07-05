package vn.geekup.app.module.moment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
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

        lifecycleScope.launchWhenCreated {
            viewModel.fetchPagingLocalFlow().distinctUntilChanged().collectLatest {
                Timber.e("Render Data Moment: ${adapterPaging.itemCount} -- Thread: ${Thread.currentThread().name}")
                adapterPaging.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapterPaging.loadStateFlow.collect { loadStates ->
                // Todo Error: ${loadState.refresh is LoadState.Error}
                if (loadStates.append is LoadState.NotLoading && loadStates.append.endOfPaginationReached) {
                    fragmentBinding.tvEmpty.visible(adapterPaging.itemCount == 0)
                }
                fragmentBinding.swMoments.isRefreshing =
                    loadStates.mediator?.refresh is LoadState.Loading && adapterPaging.itemCount > 0
            }
        }
    }

    private fun initAdapter() {
        adapterPaging = MomentFeedPagingAdapter()
//        adapterPaging.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//                if (positionStart == 0) {
//                    fragmentBinding.rvMoments.scrollToPosition(0)
//                }
//            }
//        })
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        fragmentBinding.rvMoments.layoutManager = layoutManager
        fragmentBinding.rvMoments.adapter =
            adapterPaging.withLoadStateFooter(footer = PagingLoadStateAdapter())
    }

    private fun initRefreshLayout() {
        fragmentBinding.swMoments.setOnRefreshListener {
            adapterPaging.refresh()
        }
    }

}
