package vn.geekup.app.module.profile

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import vn.geekup.app.R
import vn.geekup.app.domain.model.user.UserEngagementModel
import vn.geekup.app.domain.model.user.UserEngagementType
import vn.geekup.app.model.user.UserEngagementModelV
import vn.geekup.app.model.user.UserIndicatorModelV
import vn.geekup.app.utils.StyleTypefaceExtension

sealed class ProfileActions {
    object Logout : ProfileActions()
    object GiveNow : ProfileActions()
    object LearMore : ProfileActions()
    object IndicatorActive : ProfileActions()
    object MomentDetail : ProfileActions()
}

object ProfileExtension {

    @JvmStatic
    @BindingAdapter(value = ["engagementType"], requireAll = false)
    fun setEngagementImageUrl(imageView: AppCompatImageView, engagementType: UserEngagementType?) {
        if (engagementType == null) return
        imageView.setImageDrawable(
            when (engagementType) {
                UserEngagementType.Like() -> getDrawable(imageView.context, R.drawable.ic_like)
                UserEngagementType.Comment() -> getDrawable(
                    imageView.context,
                    R.drawable.ic_comment
                )
                else -> getDrawable(imageView.context, R.drawable.ic_created_16)
            }
        )
    }

    @JvmStatic
    @BindingAdapter(value = ["userEngagement"], requireAll = true)
    fun setEngagementName(textView: AppCompatTextView, userEngagement: UserEngagementModelV?) {
        if (userEngagement == null) return
        val strBonus = "'s"
        var nameChannel = ""
        val result = when (userEngagement.type) {
            UserEngagementType.Like() -> {
                nameChannel = userEngagement.momentCreatorProfileName ?: ""
                textView.context.getString(
                    R.string.user_engagement_like,
                    userEngagement.momentCreatorProfileName
                )
            }
            UserEngagementType.Comment() -> {
                nameChannel = userEngagement.momentCreatorProfileName ?: ""
                textView.context.getString(
                    R.string.user_engagement_comment,
                    userEngagement.momentCreatorProfileName
                )
            }
            else -> {
                nameChannel = userEngagement.momentChannelName ?: ""
                textView.context.getString(
                    R.string.user_engagement_created,
                    userEngagement.momentChannelName
                )
            }
        }
        val spanStr = SpannableStringBuilder(result)
        spanStr.setSpan(
            StyleTypefaceExtension(
                ResourcesCompat.getFont(textView.context, R.font.gilroy_medium)
            ),
            result.indexOf(nameChannel),
            result.indexOf(nameChannel) + nameChannel.length + strBonus.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spanStr.setSpan(
            ForegroundColorSpan(textView.context.getColor(R.color.color_4)),
            result.indexOf(nameChannel),
            result.indexOf(nameChannel) + nameChannel.length + strBonus.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = spanStr
    }

    @JvmStatic
    @BindingAdapter(value = ["userNextRanger"], requireAll = true)
    fun setUserNextRangerName(textView: AppCompatTextView, userIndicator: UserIndicatorModelV?) {
        if (userIndicator == null) return
        val result = textView.context.getString(
            R.string.profile_the_ranger_next_level,
            userIndicator.pointsToNextRank,
            userIndicator.nextRank
        )
        if (userIndicator.nextRank?.isEmpty() == true) {
            textView.text = result
        } else {
            val spanStr = SpannableStringBuilder(result)
            spanStr.setSpan(
                StyleTypefaceExtension(
                    ResourcesCompat.getFont(textView.context, R.font.gilroy_medium)
                ), result.indexOf(userIndicator.nextRank!!),
                result.indexOf(userIndicator.nextRank) + userIndicator.nextRank.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spanStr.setSpan(
                ForegroundColorSpan(textView.context.getColor(R.color.color_4)),
                result.indexOf(userIndicator.nextRank),
                result.indexOf(userIndicator.nextRank) + userIndicator.nextRank.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textView.text = spanStr
        }
    }

}

fun ArrayList<UserEngagementModel>.toArrayUserEngagementModelV(listener: ((ArrayList<UserEngagementModelV>) -> Unit)? = null) {
    Observable.create<ArrayList<UserEngagementModelV>> {
        val results: ArrayList<UserEngagementModelV> = arrayListOf()
        forEach { moment ->
            results.add(UserEngagementModelV().model2ModelV(moment))
        }
        it.onNext(results)
    }.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            listener?.invoke(it)
        }
}

