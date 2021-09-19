package vn.geekup.app.utils

import android.content.res.Resources
import android.util.Patterns
import android.util.TypedValue
import vn.geekup.app.base.BaseViewItem
import java.util.regex.Matcher

const val KEY_OTABLE_CODE = "code"
const val KEY_OTABLE_SCOPE = "scope"
const val KEY_MOMENT_POSTER_S = "'s"
const val KEY_MOMENT_POSTER = "moment\nin"
const val KEY_MOMENT_DEFAULT = "Geek Adventure"
const val KEY_MOMENT_CHANNEL_DEFAULT = "Tribe"
const val KEY_MOMENT_SEE_MORE = "... see more"
const val KEY_MOMENT_MAX_CHARS = 500
const val KEY_MOMENT_MAX_CHARS_IMAGES = 200
const val KEY_MOMENT_HASH_TAG_PATTERN = "#([A-Za-z0-9_-]+)"
const val KEY_MOMENT_LINK_PATTERN_S = "https://"
const val KEY_MOMENT_LINK_PATTERN = "http://"
const val KEY_PAGING_LIMIT_20 = 20
const val KEY_PAGING_LIMIT_10 = 10
const val KEY_DOUBLE_CLICK_TIME_DELTA = 300
const val KEY_ARGUMENT_IMAGES = "key_argument_images"
const val KEY_ARGUMENT_FRAGMENT = "key_argument_fragment"

val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), Resources.getSystem().displayMetrics
    )

fun String.extractLinks(): ArrayList<String>? {
    if (this.isEmpty()) return arrayListOf()
    val links: MutableList<String> = ArrayList()
    val m: Matcher = Patterns.WEB_URL.matcher(this)
    while (m.find()) {
        val url: String = m.group()
        if (!url.contains(KEY_MOMENT_LINK_PATTERN_S) && !url.contains(KEY_MOMENT_LINK_PATTERN))
            continue
        links.add(url)
    }
    return links as? ArrayList<String>
}

fun <T : BaseViewItem> ArrayList<T>.findIndex(id: Int = 0): Int {
    var index = -1
    forEachIndexed { i, baseViewItem ->
        if (baseViewItem.getItemId() == id.toString()) {
            index = i
            return@forEachIndexed
        }
    }
    return index
}
