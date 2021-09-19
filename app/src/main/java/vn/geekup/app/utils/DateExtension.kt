package vn.geekup.app.utils

import android.content.Context
import vn.geekup.app.R
import java.text.SimpleDateFormat
import java.time.ZoneOffset.UTC
import java.util.*
import java.util.concurrent.TimeUnit

private const val PATTEN_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS"
private const val PATTEN_DATE_DAY_NAME = "EEEE"
private const val PATTEN_MOMENT_DATE = "dd, MMM yyyy"

fun String.calculatorDiffToMomentDate(context: Context?): String {
    try {
        val pasTimeResult = this.convertToCurrentTimeZone()
        val nowTime = Date()
        val dateDiff = nowTime.time - pasTimeResult.time
        val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
        val output = SimpleDateFormat(PATTEN_MOMENT_DATE, Locale.ENGLISH)
        val dateResult = output.format(pasTimeResult)
        return when {
            second < 60 -> context?.getString(R.string.just_now) ?: "just now"
            minute < 60 -> "$minute" + context?.getString(R.string.minute)
            hour < 24 -> "$hour" + context?.getString(R.string.hour)
            else -> dateResult
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun String.convertToCurrentTimeZone(): Date {
    val dateFormat = SimpleDateFormat(PATTEN_DATE_TIME, Locale.ENGLISH)
    dateFormat.timeZone = TimeZone.getTimeZone(UTC)
    val pasTimeResult = dateFormat.parse(this) ?: Date()
    dateFormat.timeZone = TimeZone.getDefault()
    val pasTime = dateFormat.format(pasTimeResult)
    return dateFormat.parse(pasTime) ?: Date()
}

fun getCurrentDayName(): String {
    val sdf = SimpleDateFormat(PATTEN_DATE_DAY_NAME, Locale.ENGLISH)
    val d = Date()
    return sdf.format(d)
}

