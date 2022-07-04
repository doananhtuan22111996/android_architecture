package vn.geekup.app.utils

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import vn.geekup.app.R

object ViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["avatarImageUrl"], requireAll = true)
    fun setAvatarImageUrl(imageView: AppCompatImageView, url: String?) {
        if (url?.isNotEmpty() == true) {
            imageView.load(url) {
                crossfade(true)
                transformations(RoundedCornersTransformation(12f))
                placeholder(R.drawable.ic_app)
            }
        } else {
            imageView.setImageResource(R.drawable.ic_app)
        }
    }
}

fun View.visible(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}
