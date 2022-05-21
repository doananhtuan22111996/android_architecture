package vn.geekup.app.utils

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import coil.load
import coil.transform.RoundedCornersTransformation
import vn.geekup.app.R

object ViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["avatarImageUrl"], requireAll = true)
    fun setAvatarImageUrl(imageView: AppCompatImageView, url: String?) {
        if (url?.isNotEmpty() == true) {
            imageView.load(url) {
                transformations((RoundedCornersTransformation(12f)))
                crossfade(true)
            }
        } else {
            imageView.setImageResource(R.drawable.ic_app)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["isSelected"], requireAll = true)
    fun setButtonSelector(button: AppCompatButton, isSelected: Boolean = false) {
        button.isSelected = isSelected
    }

    @JvmStatic
    @BindingAdapter(value = ["imageUrl"], requireAll = true)
    fun setImageUrl(imageView: AppCompatImageView, url: String?) {
        if (url?.isNotEmpty() == true) {
            imageView.load(url) {
                crossfade(true)
            }
        } else {
            imageView.setImageResource(R.color.color_3)
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["createdAt"], requireAll = true)
    fun setCreateAt(textView: AppCompatTextView, createdAt: String? = "") {
        if (createdAt?.isEmpty() == true) return
        textView.text = createdAt?.calculatorDiffToMomentDate(textView.context)
    }

}

fun View.visible(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun <ViewBinding : ViewDataBinding> ViewGroup.inflaterExt(
    @LayoutRes layoutId: Int,
    listener: ((viewBinding: ViewBinding) -> Unit)? = null
) {
    this.removeAllViews()
    val inflate = LayoutInflater.from(context)
    val binding = DataBindingUtil.inflate<ViewBinding>(inflate, layoutId, this, false)
    listener?.invoke(binding)
    this.addView(binding.root)
}

fun AppCompatTextView.onClickTextInsideTextView(
    clickText: String,
    @ColorRes color: Int = R.color.color_5,
    listener: ((View, String) -> Unit)?
) {
    val spannableString = SpannableStringBuilder(this.text)
    val clickableSpans = object : ClickableSpan() {
        override fun onClick(v: View) {
            listener?.invoke(v, clickText)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = this@onClickTextInsideTextView.context.getColor(color)
        }
    }

    val startIndexOfLink = this.text.toString().indexOf(clickText)
    if (startIndexOfLink < 0) return
    spannableString.setSpan(
        clickableSpans, startIndexOfLink,
        startIndexOfLink + clickText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
//    this.highlightColor = Color.TRANSPARENT // prevent TextView change background when highlight
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun AppCompatTextView.makeLinkUnderLine(onLinkListener: ((url: String) -> Unit)? = null) {
    text.toString().extractLinks()?.forEach {
        this.onClickTextInsideTextView(it, color = R.color.color_1) { _, url ->
            onLinkListener?.invoke(url)
        }
    }
}
