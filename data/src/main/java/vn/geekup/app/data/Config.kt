package vn.geekup.app.data

import android.content.Context
import com.facebook.stetho.Stetho

object Config {

    const val PLATFORM = "android"

    fun setup(context: Context?) {
        if (isDebug) {
            Stetho.initializeWithDefaults(context)
        }
    }

    val isDebug: Boolean
        get() = BuildConfig.DEBUG

    val mainDomain: String
        get() = BuildConfig.MAIN_DOMAIN

    object Cache {
        const val CACHE_FILE_NAME = "HttpCache"
        const val CACHE_FILE_SIZE = 1025 * 1024L * 5
    }

    object TimeOut {
        const val TIMEOUT_READ_SECOND = 15L
        const val TIMEOUT_CONNECT_SECOND = 15L
        const val TIMEOUT_WRITE_SECOND = 15L
    }

    object ErrorCode {
        const val CODE_200 = 200
        const val CODE_201 = 201
        const val CODE_302 = 302
        const val CODE_401 = 401 //Unauthorized
        const val CODE_400 = 400 //Unauthorized
        const val CODE_999 = 999 //Unauthorized
    }

    object SharePreference{
         const val KEY_AUTH_TOKEN = "key_auth_token"
         const val KEY_AUTH_REFRESH_TOKEN = "key_auth_refresh_token"
         const val KEY_AUTH_INFO_INDICATOR = "key_auth_info_indicator"
    }
}