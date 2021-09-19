package vn.geekup.app.module.media

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import vn.geekup.app.R
import vn.geekup.app.base.BaseFragment
import vn.geekup.app.databinding.FragmentMainPreviewBinding
import vn.geekup.app.module.main.MainFragment
import vn.geekup.app.utils.setAppColorStatusBar

@AndroidEntryPoint
class PreviewMainFragment : BaseFragment<PreviewMainViewModel, FragmentMainPreviewBinding>() {

    private lateinit var adapter: PreviewMainViewPagerAdapter

    override val viewModel: PreviewMainViewModel by viewModels()

    override fun provideViewBinding(parent: ViewGroup): FragmentMainPreviewBinding =
        FragmentMainPreviewBinding.inflate(layoutInflater, parent, true)

    override fun onInitLayout(view: View, savedInstanceState: Bundle?) {
        baseActivity.setAppColorStatusBar(R.color.color_3)
        (parentFragment?.parentFragment as? MainFragment)?.bottomNavigationState(false)
        fragmentBinding.fragment = this
        initViewPager()
    }

    override fun bindViewModel() {
        super.bindViewModel()
        viewModel.imagesUrls.observe(this, {
            adapter.setImgUrls(imgUrls = it)
            fragmentBinding.tvTitle.text = getString(R.string.preview_image_title, 1, it.size)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding.vpImages.unregisterOnPageChangeCallback(onPageChangedListener)
    }

    private lateinit var onPageChangedListener: ViewPager2.OnPageChangeCallback

    private fun initViewPager() {
        adapter = PreviewMainViewPagerAdapter(childFragmentManager, lifecycle)
        fragmentBinding.vpImages.adapter = adapter
        onPageChangedListener = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                fragmentBinding.tvTitle.text =
                    getString(R.string.preview_image_title, position + 1, adapter.getImgUrls().size)
            }
        }
        fragmentBinding.vpImages.registerOnPageChangeCallback(onPageChangedListener)
    }

    fun onClickBack() {
        navController.popBackStack()
    }
}