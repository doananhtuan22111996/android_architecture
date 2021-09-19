package vn.geekup.app.utils

import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import timber.log.Timber

open class WebViewOTableClient : WebViewClient() {

    private var cookieManager: CookieManager = CookieManager.getInstance()

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Timber.d("Loading WebView: ${request?.url.toString()}")
        setIsLoading(true)
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        setIsLoading(false)
        val token =
            url?.substringAfter("$KEY_OTABLE_CODE=")?.substringBefore("&$KEY_OTABLE_SCOPE")
        Timber.d("Finish WebView: $url --- $token")
        if (url?.contains(KEY_OTABLE_CODE) == true && token?.isNotEmpty() == true) {
            setIsLoading(false)
            cookieManager.removeAllCookies(null)
            cookieManager.removeSessionCookies(null)
            loginSuccess(token)
        }
    }

    open fun setIsLoading(isLoading: Boolean) {}

    open fun loginSuccess(otableToken: String) {}
}