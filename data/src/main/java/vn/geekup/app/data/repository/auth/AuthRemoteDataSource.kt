package vn.geekup.app.data.repository.auth

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import vn.geekup.app.data.Config.ErrorCode.CODE_200
import vn.geekup.app.data.remote.auth.AuthApiService
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.model.general.ConfigModel
import vn.geekup.app.domain.model.user.OTableModel
import vn.geekup.app.domain.repository.AuthRepository
import vn.geekup.app.domain.throwable.ServerErrorException
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(private val authApiService: AuthApiService) :
    AuthRepository {

    override fun fetchAppConfig(): Single<ConfigModel> {
        return Single.never()
    }

    override fun logout(): Single<Boolean> {
        return authApiService.logout().andThen(Single.just(true))
    }

    override fun clearAuthInfo(): Completable {
        return Completable.complete()
    }

    override fun getToken(): Single<String> {
        return Single.never()
    }

    override fun saveToken(token: String): Completable {
        return Completable.complete()
    }

    override fun saveRefreshToken(refreshToken: String): Completable {
        return Completable.complete()
    }

    override fun loginOTable(otableBody: OTableRequestBody): Single<OTableModel> {
        return authApiService.loginOTable(otableBody.token, otableBody.currentAuthority)
            .flatMap {
                if (it.meta?.statusCode != CODE_200) {
                    Single.error(ServerErrorException(it.meta?.statusCode, it.meta?.message))
                } else {
                    Single.just(it.data?.vo2Model() ?: OTableModel())
                }
            }
    }

    override fun saveUserIndicatorActive(isEnable: Boolean): Completable {
        return Completable.complete()
    }

    override fun getUserIndicatorActive(): Single<Boolean> {
        return Single.never()
    }

}