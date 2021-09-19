package vn.geekup.app.data.repository.auth

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import vn.geekup.app.data.Config.SharePreference.KEY_AUTH_INFO_INDICATOR
import vn.geekup.app.data.Config.SharePreference.KEY_AUTH_REFRESH_TOKEN
import vn.geekup.app.data.Config.SharePreference.KEY_AUTH_TOKEN
import vn.geekup.app.data.local.PreferenceWrapper
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.model.general.ConfigModel
import vn.geekup.app.domain.model.user.OTableModel
import vn.geekup.app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(private val preferenceWrapper: PreferenceWrapper) :
    AuthRepository {

    override fun fetchAppConfig(): Single<ConfigModel> {
        return Single.just(ConfigModel(false, 1))
    }

    override fun logout(): Single<Boolean> {
        return Single.just(true)
    }

    override fun getToken(): Single<String> {
        return Single.just(preferenceWrapper.getString(KEY_AUTH_TOKEN, ""))
    }

    override fun saveToken(token: String): Completable {
        return Completable.fromAction { preferenceWrapper.saveString(KEY_AUTH_TOKEN, token) }
    }

    override fun saveRefreshToken(refreshToken: String): Completable {
        return Completable.fromAction {
            preferenceWrapper.saveString(
                KEY_AUTH_REFRESH_TOKEN,
                refreshToken
            )
        }
    }

    override fun loginOTable(otableBody: OTableRequestBody): Single<OTableModel> {
        return Single.never()
    }

    override fun saveUserIndicatorActive(isEnable: Boolean): Completable {
        return Completable.fromAction {
            preferenceWrapper.saveBoolean(KEY_AUTH_INFO_INDICATOR, isEnable)
        }
    }

    override fun getUserIndicatorActive(): Single<Boolean> {
        return Single.just(preferenceWrapper.getBoolean(KEY_AUTH_INFO_INDICATOR, true))
    }

    override fun clearAuthInfo(): Completable {
        return Completable.fromAction {
            preferenceWrapper.remove(KEY_AUTH_TOKEN)
            preferenceWrapper.remove(KEY_AUTH_REFRESH_TOKEN)
            preferenceWrapper.remove(KEY_AUTH_INFO_INDICATOR)
        }
    }

}
