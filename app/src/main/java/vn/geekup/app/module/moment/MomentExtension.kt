package vn.geekup.app.module.moment

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import vn.geekup.app.R
import vn.geekup.app.utils.*
import androidx.appcompat.widget.AppCompatImageView
import coil.load
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.*
import vn.geekup.app.domain.model.moment.MomentImagePosition
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.model.moment.MomentModelV
import java.io.File

object MomentExtension {

    @JvmStatic
    @BindingAdapter(value = ["posterName", "channelName"], requireAll = true)
    fun setNameMomentHeader(
        textView: AppCompatTextView,
        posterName: String?,
        channelName: String?
    ) {
        val posterNameStr = if (posterName?.isNotEmpty() == true) posterName else KEY_MOMENT_DEFAULT
        val channelNameStr =
            if (channelName?.isNotEmpty() == true) channelName else KEY_MOMENT_CHANNEL_DEFAULT
        val momentPoster =
            "$posterNameStr$KEY_MOMENT_POSTER_S $KEY_MOMENT_POSTER $channelNameStr"
        val spanStr = SpannableStringBuilder(momentPoster)
        spanStr.setSpan(
            StyleTypefaceExtension(
                ResourcesCompat.getFont(textView.context, R.font.gilroy_semibold)
            ),
            momentPoster.indexOf(posterNameStr),
            momentPoster.indexOf(posterNameStr) + posterNameStr.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spanStr.setSpan(
            StyleTypefaceExtension(
                ResourcesCompat.getFont(textView.context, R.font.gilroy_semibold)
            ),
            momentPoster.indexOf(channelNameStr),
            momentPoster.indexOf(channelNameStr) + channelNameStr.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spanStr.setSpan(
            StyleTypefaceExtension(
                ResourcesCompat.getFont(textView.context, R.font.gilroy_medium)
            ),
            momentPoster.indexOf(KEY_MOMENT_POSTER_S),
            momentPoster.indexOf(KEY_MOMENT_POSTER_S) + KEY_MOMENT_POSTER_S.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.setText(spanStr, TextView.BufferType.SPANNABLE)
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["creatorName", "createdAt"], requireAll = true)
    fun setCreateAtMomentHeader(
        textView: AppCompatTextView,
        creatorName: String?,
        createdAt: String?
    ) {
        textView.text = "$creatorName - ${createdAt?.calculatorDiffToMomentDate(textView.context)}"
    }

    @JvmStatic
    @BindingAdapter(value = ["momentImageUrl", "positionImage"], requireAll = false)
    fun setMomentImageUrl(
        imageView: AppCompatImageView,
        url: String?,
        positionImage: MomentImagePosition?
    ) {
        if (url?.isNotEmpty() == true) {
            val multiTransform = when (positionImage) {
                MomentImagePosition.ALL_LEFT ->
                    RoundedCornersTransformation(topLeft = 12f, bottomLeft = 12f)
                MomentImagePosition.ALL_RIGHT ->
                    RoundedCornersTransformation(topRight = 12f, bottomRight = 12f)
                MomentImagePosition.ALL_BOTTOM ->
                    RoundedCornersTransformation(bottomLeft = 12f, bottomRight = 12f)
                MomentImagePosition.ALL_TOP ->
                    RoundedCornersTransformation(topLeft = 12f, topRight = 12f)
                MomentImagePosition.TOP_LEFT ->
                    RoundedCornersTransformation(topLeft = 12f)
                MomentImagePosition.TOP_RIGHT ->
                    RoundedCornersTransformation(topRight = 12f)
                MomentImagePosition.BOTTOM_LEFT ->
                    RoundedCornersTransformation(bottomLeft = 12f)
                MomentImagePosition.BOTTOM_RIGHT ->
                    RoundedCornersTransformation(bottomRight = 12f)
                else ->
                    RoundedCornersTransformation(12f)
            }
            if (url.lowercase().contains(KEY_MOMENT_LINK_PATTERN_S) || url.lowercase()
                    .contains(KEY_MOMENT_LINK_PATTERN)
            ) {
                imageView.load(url) {
                    transformations(multiTransform)
                    crossfade(true)
                    placeholder(R.drawable.bg_image_momnet_landscap)
                }
            } else {
                imageView.load(File(url)) {
                    transformations(multiTransform)
                    crossfade(true)
                    placeholder(R.drawable.bg_image_momnet_landscap)
                }
            }
        } else {
            imageView.setImageResource(R.drawable.bg_image_momnet_landscap)
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["isLiked"], requireAll = true)
    fun setMomentButtonFooterLike(button: AppCompatButton, isLiked: Boolean = false) {
        button.isEnabled = !isLiked
        button.setCompoundDrawablesWithIntrinsicBounds(
            if (isLiked) R.drawable.ic_like_active else R.drawable.ic_like,
            0,
            0,
            0
        )
    }

}

fun ArrayList<MomentModel>.toArrayMomentModelV(listener: ((ArrayList<MomentModelV>) -> Unit)? = null) {
    CoroutineScope(Dispatchers.IO).launch {
        val results: ArrayList<MomentModelV> = arrayListOf()
        forEach { moment ->
            results.add(MomentModelV().model2ModelV(moment))
        }
        withContext(Dispatchers.Main) {
            listener?.invoke(results)
        }
    }
}
