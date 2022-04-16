package vn.geekup.app.module.moment.detail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import vn.geekup.app.R
import vn.geekup.app.base.BaseFragment
import vn.geekup.app.databinding.*
import vn.geekup.app.domain.throwable.ServerErrorException
import vn.geekup.app.model.moment.MomentModelV
import vn.geekup.app.module.moment.MomentViewModel
import vn.geekup.app.module.main.MainFragment
import vn.geekup.app.module.root.RootActivity
import vn.geekup.app.utils.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import vn.geekup.app.model.moment.MomentActionV
import vn.geekup.app.module.moment.toArrayString
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MomentDetailFragment : BaseFragment<MomentViewModel, FragmentMomentDetailBinding>() {

    private lateinit var adapter: MomentCommentAdapter

    override val viewModel: MomentViewModel by activityViewModels()

    override fun initViewModelByActivityLifecycle(): Boolean = true

    override fun provideViewBinding(parent: ViewGroup): FragmentMomentDetailBinding =
        FragmentMomentDetailBinding.inflate(layoutInflater, parent, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMomentCommentAdapter()
    }

    override fun onInitLayout(view: View, savedInstanceState: Bundle?) {
        (baseActivity as? RootActivity)?.setAppColorStatusBar()
        (parentFragment?.parentFragment as? MainFragment)?.bottomNavigationState(false)
        loadingState(true)
        initRecyclerView()
        eventMomentDetail()
        fragmentBinding.layoutInputComment.btnSend.isEnabled = false
        fragmentBinding.userInfo = viewModel.userInfo.value
    }

    override fun bindViewModel() {
        super.bindViewModel()

        viewModel.momentDetail.observe(this) {
            loadingState(false)
            viewModel.updateMomentToMomentFeeds(it, momentAction = MomentActionV.MomentDetail)
            initToolbar(it)
            fragmentBinding.moment = it
            inflateMomentImages(it)
        }

        viewModel.newMomentState.observe(this) {
            // Fist: New Moment, second: Position, third: MomentAction
            val (newMoment, _, momentAction) = it
            if (momentAction == MomentActionV.MomentComment) {
                executingScrollEndScreen()
            }
            fragmentBinding.moment = newMoment

        }

        viewModel.momentComments.observe(this) {
            adapter.addAllItemsWithDiffUtils(it)
            fragmentBinding.tvPreviousComment.visible(viewModel.nextPreviousCommentCursor?.isNotEmpty() == true)
            fragmentBinding.layoutPreviousCommentLoading.rlParentProgressbar.visible(false)
        }

    }

    override fun handleServerErrorState(serverErrorException: ServerErrorException) {
        super.handleServerErrorState(serverErrorException)
        loadingState(false)
    }

    private fun initMomentCommentAdapter() {
        adapter = MomentCommentAdapter()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        fragmentBinding.rvMomentComments.layoutManager = layoutManager
        fragmentBinding.rvMomentComments.adapter = adapter
        fragmentBinding.rvMomentComments.isNestedScrollingEnabled = false
    }

    private fun initToolbar(moment: MomentModelV) {
        fragmentBinding.layoutToolbar.tvTitle.text =
            getString(R.string.moment_detail_title, moment.posterName)
        fragmentBinding.layoutToolbar.ivBack.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun eventMomentDetail() {
        fragmentBinding.layoutMomentFooter.btnLikes.setOnClickListener {
            viewModel.requestMomentLike(fragmentBinding.moment?.id ?: 0)
        }

        fragmentBinding.layoutMomentFooter.btnShares.setOnClickListener {
            if (fragmentBinding.moment?.userId == viewModel.userInfo.value?.id) {
                viewModel.shareMomentToNexion(fragmentBinding.moment?.id ?: 0)
            } else {
                Toast.makeText(
                    context,
                    "Only creator of this moment can share it to Nexion",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        fragmentBinding.tvPreviousComment.setOnClickListener {
            fragmentBinding.layoutPreviousCommentLoading.rlParentProgressbar.visible(true)
            fragmentBinding.tvPreviousComment.visibility = View.INVISIBLE
            viewModel.getPreviousMomentComments(fragmentBinding.moment?.id)
        }

        fragmentBinding.layoutInputComment.btnSend.setOnClickListener {
            viewModel.postMomentComment(
                fragmentBinding.moment?.id ?: 0,
                comment = fragmentBinding.layoutInputComment.edtComment.text?.toString()
            )
            resetInputComment()
        }

        fragmentBinding.layoutInputComment.edtComment.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.postMomentComment(
                    fragmentBinding.moment?.id ?: 0,
                    comment = fragmentBinding.layoutInputComment.edtComment.text?.toString()
                )
                resetInputComment()
            }
            false
        }

        fragmentBinding.layoutInputComment.edtComment.doOnTextChanged { text, _, _, _ ->
            fragmentBinding.layoutInputComment.btnSend.isEnabled = text?.isNotEmpty() == true
        }

        fragmentBinding.frMomentMedia.setOnClickListener {
            fragmentBinding.moment?.imgUrls?.toArrayString {
                navController.navigate(
                    R.id.previewMainFragment,
                    bundleOf(KEY_ARGUMENT_IMAGES to it)
                )
            }
        }
    }

    private fun inflateMomentImages(moment: MomentModelV) {
        if (moment.imgUrls?.size ?: 0 == 0) return
        when {
            moment.imgUrls?.size == 1 -> {
                fragmentBinding.frMomentMedia.inflaterExt<LayoutItemMoment1ImageBinding>(R.layout.layout_item_moment_1_image) {
                    it.url = moment.imgUrls?.get(0)?.original
                }
            }
            moment.imgUrls?.size == 2 -> {
                fragmentBinding.frMomentMedia.inflaterExt<LayoutItemMoment2ImagesBinding>(R.layout.layout_item_moment_2_images) {
                    it.url1 = moment.imgUrls?.get(0)?.square?.x1
                    it.url2 = moment.imgUrls?.get(1)?.square?.x1
                }
            }
            moment.imgUrls?.size == 3 -> {
                fragmentBinding.frMomentMedia.inflaterExt<LayoutItemMoment3ImagesBinding>(R.layout.layout_item_moment_3_images) {
                    it.url1 = moment.imgUrls?.get(0)?.original
                    it.url2 = moment.imgUrls?.get(1)?.square?.x1
                    it.url3 = moment.imgUrls?.get(2)?.square?.x1
                }
            }
            moment.imgUrls?.size ?: 0 > 3 -> {
                fragmentBinding.frMomentMedia.inflaterExt<LayoutItemMomentMoreImagesBinding>(R.layout.layout_item_moment_more_images) {
                    it.url1 = moment.imgUrls?.get(0)?.square?.x1
                    it.url2 = moment.imgUrls?.get(1)?.square?.x1
                    it.url3 = moment.imgUrls?.get(2)?.square?.x1
                    it.url4 = moment.imgUrls?.get(3)?.square?.x1
                    it.numberMore = (moment.imgUrls?.size ?: 0) - 4
                }
            }
        }
    }

    private fun resetInputComment() {
        fragmentBinding.layoutInputComment.edtComment.setText("")
        baseActivity.window.hideSoftKeyboard()
    }

    private fun executingScrollEndScreen() {
        // Scroll End Screen when post new comment
        Observable.timer(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                fragmentBinding.nsvContainer.smoothScrollBy(
                    0,
                    fragmentBinding.nsvContainer.getChildAt(0).height
                )
            }
    }

    private fun loadingState(isLoading: Boolean) {
        fragmentBinding.layoutLoading.rlParentProgressbar.visible(isLoading)
    }

}