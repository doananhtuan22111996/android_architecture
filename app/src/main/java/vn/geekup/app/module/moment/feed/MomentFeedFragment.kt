package vn.geekup.app.module.moment.feed

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import timber.log.Timber
import vn.geekup.app.R
import vn.geekup.app.base.BaseFragment
import vn.geekup.app.databinding.FragmentMomentFeedBinding
import vn.geekup.app.domain.model.user.UserInfoModel
import vn.geekup.app.domain.throwable.ServerErrorException
import vn.geekup.app.model.moment.MomentActionV
import vn.geekup.app.model.moment.MomentModelV
import vn.geekup.app.model.user.UserIndicatorModelV
import vn.geekup.app.module.calendar.CalendarBottomSheetDialogFragment
import vn.geekup.app.module.main.MainFragment
import vn.geekup.app.module.moment.MomentViewModel
import vn.geekup.app.module.moment.toArrayString
import vn.geekup.app.module.root.RootActivity
import vn.geekup.app.utils.*
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import androidx.recyclerview.widget.SimpleItemAnimator

class MomentFeedFragment : BaseFragment<MomentViewModel, FragmentMomentFeedBinding>(),
    RootActivity.OnUserInfoListener {

    private lateinit var adapter: MomentFeedsAdapter
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private var isEnableIndicator: Boolean = true
    private var dateFilter: String = ""

    override fun provideViewModelClass(): Class<MomentViewModel> {
        return MomentViewModel::class.java
    }

    override fun initViewModelByActivityLifecycle(): Boolean = true

    override fun provideViewBinding(parent: ViewGroup): FragmentMomentFeedBinding {
        return FragmentMomentFeedBinding.inflate(layoutInflater, parent, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getMomentFeeds(date = dateFilter)
        (activity as? RootActivity)?.setOnUserInfoListener(this)
        initAdapter()
    }

    override fun onInitLayout(view: View, savedInstanceState: Bundle?) {
        activity.setAppColorStatusBar(R.color.color_white)
        (parentFragment?.parentFragment as? MainFragment)?.bottomNavigationState(true)
        initMomentHeader()
        initRecyclerView()
        initRefreshLayout()
        eventView()
    }

    override fun bindViewModel() {
        super.bindViewModel()

        viewModel.moments.observe(this, {
            fragmentBinding.isMomentEmpty = it.isEmpty()
            adapter.addAllItemsWithDiffUtils(it)
            fragmentBinding.swMoments.isRefreshing = false
        })

        viewModel.newMomentState.observe(this, {
            // Fist: New Moment, second: Position, third: MomentAction
            val (newMoment, position, _) = it
            if (position > adapter.itemCount) return@observe
            adapter.setItemPositionChanged(newMoment, position)
        })

        viewModel.shareMoment.observe(this, {
            Toast.makeText(context, it?.message.toString(), Toast.LENGTH_SHORT).show()
        })

        viewModel.pagingState.observe(this, {
            Timber.d("Loadmore: $it")
            adapter.setNetworkState(it)
        })
    }

    override fun handleServerErrorState(serverErrorException: ServerErrorException) {
        super.handleServerErrorState(serverErrorException)
        fragmentBinding.isMomentEmpty = adapter.itemCount == 0
        fragmentBinding.swMoments.isRefreshing = false
    }

    override fun onUserInfo(userInfo: UserInfoModel) {
        viewModel.userInfo.value = userInfo
        fragmentBinding.layoutMomentHeaderBar.userInfo = userInfo
    }

    override fun onUserIndicator(userIndicator: UserIndicatorModelV) {
        viewModel.userInfoIndicator.value = userIndicator
        fragmentBinding.layoutMomentHeaderBar.userIndicator = userIndicator
    }

    override fun onUserIndicatorMomentFeedActive(isEnable: Boolean) {
        isEnableIndicator = isEnable
        fragmentBinding.layoutMomentHeaderBar.layoutIndicator.root.visible(isEnable)
        fragmentBinding.layoutMomentHeaderBar.layoutIndicator.tvHide.visible(isEnable)
    }

    private fun initMomentHeader() {
        fragmentBinding.layoutMomentHeaderBar.day = getCurrentDayName()
        fragmentBinding.layoutMomentHeaderBar.userInfo = viewModel.userInfo.value
        fragmentBinding.layoutMomentHeaderBar.userIndicator = viewModel.userInfoIndicator.value
        fragmentBinding.layoutMomentHeaderBar.layoutIndicator.root.visible(isEnableIndicator)
        fragmentBinding.layoutMomentHeaderBar.layoutIndicator.tvHide.visible(isEnableIndicator)
        fragmentBinding.layoutMomentHeaderBar.layoutIndicator.tvHide.setOnClickListener {
            (activity as? RootActivity)?.onUserIndicatorMomentFeedActive(isEnable = false)
        }
    }

    private fun initAdapter() {
        adapter = MomentFeedsAdapter(
            this::listenerAdapter,
            this::onClickSeeMoreListener,
            this::onClickLinkListener
        )
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(layoutManager) {

                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    viewModel.getMomentFeeds(date = dateFilter)
                }
            }
        fragmentBinding.rvMoments.layoutManager = layoutManager
        fragmentBinding.rvMoments.adapter = adapter
        (fragmentBinding.rvMoments.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
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
            viewModel.getMomentFeeds(date = dateFilter, isReload = true)
        }
        (parentFragment?.parentFragment as? MainFragment)?.setOnChildFragmentListener(object :
            MainFragment.OnChildFragmentListener {
            override fun onAutoReloadMomentFeed() {
                fragmentBinding.rvMoments.smoothScrollToPosition(0)
                executingAutoReloadMomentFeed()
            }
        })
    }

    private fun listenerAdapter(data: MomentModelV, position: Int, action: MomentActionV) =
        when (action) {
            MomentActionV.MomentLike -> {
                viewModel.requestMomentLike(data.id)
            }
            MomentActionV.MomentShare -> {
                if (data.userId == viewModel.userInfo.value?.id) {
                    viewModel.shareMomentToNexion(data.id)
                } else {
                    Toast.makeText(
                        context,
                        "Only creator of this moment can share it to Nexion",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            MomentActionV.MomentDetail -> {
                redirectMomentDetail(data.id)
            }
            MomentActionV.MomentPreview -> {
                data.imgUrls?.toArrayString {
                    navController.navigate(
                        R.id.previewMainFragment,
                        bundleOf(KEY_ARGUMENT_IMAGES to it)
                    )
                }
            }
            else -> Unit
        }

    private fun onClickSeeMoreListener(data: MomentModelV?) {
        redirectMomentDetail(data?.id)
    }

    private fun onClickLinkListener(url: String) {
        activity.openBrowserApp(url)
    }

    private fun eventView() {
        fragmentBinding.layoutMomentHeaderBar.ivCalendar.setOnClickListener {
            openCalendarBottomSheet()
        }
    }

    private fun redirectMomentDetail(momentId: Int? = 0) {
        navController.navigate(
            R.id.momentDetailFragment,
            bundleOf(KEY_ARGUMENT_FRAGMENT to momentId)
        )
    }

    private val calendarBottomSheetFragment =
        CalendarBottomSheetDialogFragment(
            onDateSelectedListener = { day, month, year ->
                dateFilter = "$year-$month-$day"
                fragmentBinding.swMoments.isRefreshing = true
                viewModel.getMomentFeeds(date = dateFilter, isReload = true)
            },
            onResetSelectedListener = {
                dateFilter = ""
                fragmentBinding.swMoments.isRefreshing = true
                viewModel.getMomentFeeds(date = dateFilter, isReload = true)
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
                viewModel.getMomentFeeds(date = dateFilter, isReload = true)
            }
    }

}
