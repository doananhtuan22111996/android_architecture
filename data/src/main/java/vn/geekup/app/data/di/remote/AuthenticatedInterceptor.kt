package vn.geekup.app.data.di.remote

import android.text.TextUtils
import kotlinx.coroutines.flow.collectLatest
import okhttp3.Request
import vn.geekup.app.data.Config
import vn.geekup.app.data.local.PreferenceWrapper
import vn.geekup.app.domain.repository.AuthRepository

class AuthenticatedInterceptor constructor(private val preferenceWrapper: PreferenceWrapper) :
    DefaultInterceptor() {

    override fun customHeaderRequest(requestBuilder: Request.Builder) {
        super.customHeaderRequest(requestBuilder)
        val token = preferenceWrapper.getString(Config.SharePreference.KEY_AUTH_TOKEN, "")
        if (!TextUtils.isEmpty(token)) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
    }

}