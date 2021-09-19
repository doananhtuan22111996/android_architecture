package vn.geekup.app.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import vn.geekup.app.R
import androidx.core.view.WindowInsetsControllerCompat

fun Window.hideSoftKeyboard() {
    val inputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
}

@SuppressLint("ClickableViewAccessibility")
fun Window.setupViewClickHideKeyBoard(vararg views: View) {
    for (view in views) {
        if (view is EditText || view.isClickable) {
            continue
        }
        view.setOnTouchListener { _, _ ->
            if (currentFocus != null && currentFocus!!.windowToken != null) {
                hideSoftKeyboard()
                currentFocus!!.clearFocus()
            }
            false
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupViewClickHideKeyBoard(innerView)
            }
        }
    }
}

fun AppCompatActivity.setAppColorStatusBar(@ColorRes color: Int = R.color.color_status_bar) {
    val window: Window = this.window
    val decorView = window.decorView
    val wic = WindowInsetsControllerCompat(window, decorView)
    wic.isAppearanceLightStatusBars = color == R.color.color_white
    window.statusBarColor = ContextCompat.getColor(this, color)
}

fun AppCompatActivity.openBrowserApp(url: String? = "") {
    if (url?.isEmpty() == true) return
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(browserIntent)
}