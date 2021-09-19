package vn.geekup.app.data.di.remote

import android.net.Uri
import androidx.annotation.CallSuper
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import vn.geekup.app.data.Config
import java.io.IOException

open class DefaultInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val url = original.url.toString()

        val requestBuilder = original.newBuilder().url(Uri.decode(url))
        customHeaderRequest(requestBuilder)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

    @CallSuper
    open fun customHeaderRequest(requestBuilder: Request.Builder) {
        requestBuilder.addHeader("platform", Config.PLATFORM)
        requestBuilder.addHeader("Content-Type", "application/json")

    }
}