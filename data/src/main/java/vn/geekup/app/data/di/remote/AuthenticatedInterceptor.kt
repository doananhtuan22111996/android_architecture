package vn.geekup.app.data.di.remote

import android.text.TextUtils
import okhttp3.Request
import vn.geekup.app.domain.repository.AuthRepository

class AuthenticatedInterceptor constructor(private val authRepository: AuthRepository) :
    DefaultInterceptor() {

    override fun customHeaderRequest(requestBuilder: Request.Builder) {
        super.customHeaderRequest(requestBuilder)
        val token = authRepository.getToken().blockingGet()
        if (!TextUtils.isEmpty(token)) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
    }

}