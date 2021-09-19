package vn.geekup.app.domain.usecase

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import vn.geekup.app.domain.dto.ConfigRequestBody
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.repository.AuthRepository

interface AuthUseCase {

    fun needUpgradeApplication(configRequestBody: ConfigRequestBody): Single<Boolean>

    fun getToken(): Single<String>

    fun logout(): Single<Boolean>

    fun loginOTable(otableBody: OTableRequestBody): Single<Boolean>

    fun forceToLogin(): Completable

    fun saveUserIndicatorActive(isEnable: Boolean): Completable

    fun getUserIndicatorActive(): Single<Boolean>
}

class AuthUseCaseImplement(private var authRepository: AuthRepository) : AuthUseCase {

    override fun needUpgradeApplication(configRequestBody: ConfigRequestBody): Single<Boolean> {
        return authRepository.fetchAppConfig()
            .map { it.isForceUpdate && it.latestVersion > configRequestBody.currentVersion }
    }

    override fun getToken(): Single<String> {
        return authRepository.getToken()
    }

    override fun logout(): Single<Boolean> {
        return authRepository.logout().flatMap {
            authRepository.clearAuthInfo().andThen(Single.just(it))
        }
    }

    override fun loginOTable(otableBody: OTableRequestBody): Single<Boolean> {
        return authRepository.loginOTable(otableBody).flatMap {
            authRepository.saveToken(it.token ?: "")
                .andThen(authRepository.saveRefreshToken(it.refreshToken ?: ""))
                .andThen(Single.just(it.token?.isNotEmpty() == true))
        }
    }

    override fun forceToLogin(): Completable {
        return authRepository.clearAuthInfo()
    }

    override fun getUserIndicatorActive(): Single<Boolean> {
        return authRepository.getUserIndicatorActive()
    }

    override fun saveUserIndicatorActive(isEnable: Boolean): Completable {
        return authRepository.saveUserIndicatorActive(isEnable = isEnable)
    }
}