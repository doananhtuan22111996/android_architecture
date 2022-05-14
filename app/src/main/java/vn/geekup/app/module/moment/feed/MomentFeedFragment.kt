package vn.geekup.app.module.moment.feed

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import timber.log.Timber
import vn.geekup.app.R
import vn.geekup.app.base.BaseFragment
import vn.geekup.app.databinding.FragmentMomentFeedBinding
import vn.geekup.app.module.calendar.CalendarBottomSheetDialogFragment
import vn.geekup.app.module.main.MainFragment
import vn.geekup.app.module.moment.MomentViewModel
import vn.geekup.app.utils.*
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import androidx.recyclerview.widget.SimpleItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import vn.geekup.app.domain.model.general.ResultModel

@AndroidEntryPoint
class MomentFeedFragment : BaseFragment<MomentViewModel, FragmentMomentFeedBinding>() {

    private lateinit var adapter: MomentFeedsAdapter
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private var isEnableIndicator: Boolean = true
    private var dateFilter: String = ""

    override val viewModel: MomentViewModel by activityViewModels()

    override fun initViewModelByActivityLifecycle(): Boolean = true

    override fun provideViewBinding(parent: ViewGroup): FragmentMomentFeedBinding {
        return FragmentMomentFeedBinding.inflate(layoutInflater, parent, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getFlowMomentFeeds(date = dateFilter)
        initAdapter()
    }

    override fun onInitLayout(view: View, savedInstanceState: Bundle?) {
        baseActivity.setAppColorStatusBar(R.color.color_white)
        (parentFragment?.parentFragment as? MainFragment)?.bottomNavigationState(true)
        initMomentHeader()
        initRecyclerView()
        initRefreshLayout()
        eventView()
    }

    override fun bindViewModel() {
        super.bindViewModel()

        viewModel.moments.observe(this) {
            fragmentBinding.isMomentEmpty = it.isEmpty()
            adapter.addAllItemsWithDiffUtils(it)
            fragmentBinding.swMoments.isRefreshing = false
        }

        viewModel.pagingState.observe(this) {
            Timber.d("Loadmore: $it")
            adapter.setNetworkState(it)
        }
    }

    override fun handleServerErrorState(serverErrorException: ResultModel.ServerErrorException?) {
        super.handleServerErrorState(serverErrorException)
        fragmentBinding.isMomentEmpty = adapter.itemCount == 0
        fragmentBinding.swMoments.isRefreshing = false
    }

    private fun initMomentHeader() {
        fragmentBinding.layoutMomentHeaderBar.day = getCurrentDayName()
        fragmentBinding.layoutMomentHeaderBar.layoutIndicator.root.visible(isEnableIndicator)
        fragmentBinding.layoutMomentHeaderBar.layoutIndicator.tvHide.visible(isEnableIndicator)
    }

    private fun initAdapter() {
        adapter = MomentFeedsAdapter(
            null,
            null,
            this::onClickLinkListener
        )
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(layoutManager) {

                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    viewModel.getFlowMomentFeeds(date = dateFilter)
                }
            }
        fragmentBinding.rvMoments.layoutManager = layoutManager
        fragmentBinding.rvMoments.adapter = adapter
        (fragmentBinding.rvMoments.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        fragmentBinding.rvMoments.addOnScrollListener(endlessRecyclerViewScrollListener)
        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (positionStart == 0) {
                    // When pull to refresh
                    Timber.d("Position: $positionStart")
                    fragmentBinding.rvMoments.smoothScrollToPosition(positionStart)
                }

            }
        })
    }

    private fun initRefreshLayout() {
        fragmentBinding.swMoments.setOnRefreshListener {
            viewModel.getFlowMomentFeeds(date = dateFilter, isReload = true)
        }
        (parentFragment?.parentFragment as? MainFragment)?.setOnChildFragmentListener(object :
            MainFragment.OnChildFragmentListener {
            override fun onAutoReloadMomentFeed() {
                fragmentBinding.rvMoments.smoothScrollToPosition(0)
                executingAutoReloadMomentFeed()
            }
        })
    }

    private fun onClickLinkListener(url: String) {
        baseActivity.openBrowserApp(url)
    }

    private fun eventView() {
        fragmentBinding.layoutMomentHeaderBar.ivCalendar.setOnClickListener {
            openCalendarBottomSheet()
        }
    }

    private val calendarBottomSheetFragment =
        CalendarBottomSheetDialogFragment(
            onDateSelectedListener = { day, month, year ->
                dateFilter = "$year-$month-$day"
                fragmentBinding.swMoments.isRefreshing = true
                viewModel.getFlowMomentFeeds(date = dateFilter, isReload = true)
            },
            onResetSelectedListener = {
                dateFilter = ""
                fragmentBinding.swMoments.isRefreshing = true
                viewModel.getFlowMomentFeeds(date = dateFilter, isReload = true)
            }
        )

    private fun openCalendarBottomSheet() {
        val frCalendar = childFragmentManager.fragments.find {
            it.tag == CalendarBottomSheetDialogFragment.TAG
        }
        if (frCalendar != null) return
        calendarBottomSheetFragment.show(
            childFragmentManager,
            CalendarBottomSheetDialogFragment.TAG
        )
    }

    private fun executingAutoReloadMomentFeed() {
        // Scroll End Screen when post new comment
        Observable.timer(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                fragmentBinding.rvMoments.scrollToPosition(0)
                fragmentBinding.swMoments.isRefreshing = true
                viewModel.getFlowMomentFeeds(date = dateFilter, isReload = true)
            }
    }

}
