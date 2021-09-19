package vn.geekup.app.module.media

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PreviewMainViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private var imgUrls: ArrayList<String> = arrayListOf()

    fun setImgUrls(imgUrls: ArrayList<String>) {
        this.imgUrls.addAll(imgUrls)
    }

    fun getImgUrls() = imgUrls

    override fun getItemCount(): Int = imgUrls.size

    override fun createFragment(position: Int): Fragment {
        return PreviewImageFragment.newInstance(imgUrl = imgUrls[position])
    }
}