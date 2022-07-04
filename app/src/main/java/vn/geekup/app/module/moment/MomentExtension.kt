package vn.geekup.app.module.moment

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import vn.geekup.app.R
import vn.geekup.app.utils.*
import androidx.appcompat.widget.AppCompatImageView
import coil.load
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.*
import timber.log.Timber
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.model.moment.MomentModelV

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
