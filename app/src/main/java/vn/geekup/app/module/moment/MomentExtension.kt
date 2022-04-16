package vn.geekup.app.module.moment

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import vn.geekup.app.R
import vn.geekup.app.utils.*
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import vn.geekup.app.domain.model.moment.ImgUrlModel
import vn.geekup.app.domain.model.moment.MomentImagePosition
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.model.moment.MomentModelV
import java.util.regex.Pattern

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
    @BindingAdapter(value = ["moment", "onClickSeeMore", "onClickLink"], requireAll = false)
    fun setContentMoment(
        textView: AppCompatTextView,
        moment: MomentModelV?,
        onClickSeeMore: ((MomentModelV?) -> Unit)? = null,
        onClickLink: ((url: String) -> Unit)? = null
    ) {
        val sizeImages = moment?.imgUrls?.size ?: 0
        val contentChars = moment?.content?.length ?: 0
        if (sizeImages > 0 && contentChars > KEY_MOMENT_MAX_CHARS_IMAGES) {
            textView.makeTextSeeMore(
                content = moment?.content,
                maxChars = KEY_MOMENT_MAX_CHARS_IMAGES
            ) { onClickSeeMore?.invoke(moment) }
        } else if (sizeImages == 0 && contentChars > KEY_MOMENT_MAX_CHARS) {
            textView.makeTextSeeMore(
                content = moment?.content,
                maxChars = KEY_MOMENT_MAX_CHARS
            ) { onClickSeeMore?.invoke(moment) }
        } else {
            textView.text = moment?.content
        }
        textView.makeLinkUnderLine(onLinkListener = onClickLink)
        textView.setHashTagMoment()
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
                    MultiTransformation(
                        RoundedCornersTransformation(
                            12.toPx.toInt(),
                            0,
                            RoundedCornersTransformation.CornerType.TOP_LEFT
                        ),
                        RoundedCornersTransformation(
                            12.toPx.toInt(),
                            0,
                            RoundedCornersTransformation.CornerType.BOTTOM_LEFT
                        )
                    )
                MomentImagePosition.ALL_RIGHT ->
                    MultiTransformation(
                        RoundedCornersTransformation(
                            12.toPx.toInt(),
                            0,
                            RoundedCornersTransformation.CornerType.TOP_RIGHT
                        ),
                        RoundedCornersTransformation(
                            12.toPx.toInt(),
                            0,
                            RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
                        )
                    )
                MomentImagePosition.ALL_BOTTOM ->
                    MultiTransformation(
                        RoundedCornersTransformation(
                            12,
                            0,
                            RoundedCornersTransformation.CornerType.BOTTOM_LEFT
                        ),
                        RoundedCornersTransformation(
                            12,
                            0,
                            RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
                        )
                    )
                MomentImagePosition.ALL_TOP ->
                    MultiTransformation(
                        RoundedCornersTransformation(
                            12.toPx.toInt(),
                            0,
                            RoundedCornersTransformation.CornerType.TOP_LEFT
                        ),
                        RoundedCornersTransformation(
                            12.toPx.toInt(),
                            0,
                            RoundedCornersTransformation.CornerType.TOP_RIGHT
                        )
                    )
                MomentImagePosition.TOP_LEFT ->
                    RoundedCornersTransformation(
                        12.toPx.toInt(),
                        0,
                        RoundedCornersTransformation.CornerType.TOP_LEFT
                    )
                MomentImagePosition.TOP_RIGHT ->
                    RoundedCornersTransformation(
                        12.toPx.toInt(),
                        0,
                        RoundedCornersTransformation.CornerType.TOP_RIGHT
                    )
                MomentImagePosition.BOTTOM_LEFT ->
                    RoundedCornersTransformation(
                        12.toPx.toInt(),
                        0,
                        RoundedCornersTransformation.CornerType.BOTTOM_LEFT
                    )
                MomentImagePosition.BOTTOM_RIGHT ->
                    RoundedCornersTransformation(
                        12.toPx.toInt(),
                        0,
                        RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
                    )
                else ->
                    RoundedCornersTransformation(
                        12.toPx.toInt(),
                        0,
                        RoundedCornersTransformation.CornerType.ALL
                    )
            }
            Glide.with(imageView.context)
                .load(url)
                .thumbnail(
                    Glide.with(imageView.context)
                        .load(R.drawable.bg_image_momnet_landscap)
                        .transform(CenterCrop())
                )
                .transform(CenterCrop(), multiTransform)
                .placeholder(R.drawable.bg_image_momnet_landscap)
                .into(imageView)
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

fun AppCompatTextView.setHashTagMoment() {
    Observable.create<SpannableStringBuilder> {
        val spannable = SpannableStringBuilder(text)
        val matcher = Pattern.compile(KEY_MOMENT_HASH_TAG_PATTERN).matcher(spannable)
        while (matcher.find()) {
            spannable.setSpan(
                ForegroundColorSpan(this.context.getColor(R.color.color_text_hash_tag)),
                matcher.start(),
                matcher.end(),
                0
            )
        }
        it.onNext(spannable)
    }.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            this.text = it
        }
}

fun ArrayList<MomentModel>.toArrayMomentModelV(listener: ((ArrayList<MomentModelV>) -> Unit)? = null) {
    Observable.create<ArrayList<MomentModelV>> {
        val results: ArrayList<MomentModelV> = arrayListOf()
        forEach { moment ->
            results.add(MomentModelV().model2ModelV(moment))
        }
        it.onNext(results)
    }.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            listener?.invoke(it)
        }
}

fun ArrayList<ImgUrlModel>.toArrayString(listener: ((ArrayList<String>) -> Unit)? = null) {
    Observable.create<ArrayList<String>> {
        val results: ArrayList<String> = arrayListOf()
        forEach { img ->
            results.add(img.original ?: "")
        }
        it.onNext(results)
    }.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            listener?.invoke(it)
        }
}
