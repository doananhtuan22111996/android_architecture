package vn.geekup.app.data.repository.auth

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import vn.geekup.app.data.di.qualifier.source.SourceLocal
import vn.geekup.app.data.di.qualifier.source.SourceRemote
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.model.general.ConfigModel
import vn.geekup.app.domain.model.user.OTableModel
import vn.geekup.app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthDataSource @Inject constructor(

    @SourceLocal
    private val local: AuthRepository,

    @SourceRemote
    private val remote: AuthRepository

) : AuthRepository {

    override fun fetchAppConfig(): Single<ConfigModel> {
        return remote.fetchAppConfig()
    }

    override fun logout(): Single<Boolean> {
        return remote.logout()
    }

    override fun clearAuthInfo(): Completable {
        return local.clearAuthInfo()
    }

    override fun getToken(): Single<String> {
        return local.getToken()
    }

    override fun saveToken(token: String): Completable {
        return local.saveToken(token)
    }

    override fun saveRefreshToken(refreshToken: String): Completable {
        return local.saveRefreshToken(refreshToken)
    }

    override fun loginOTable(otableBody: OTableRequestBody): Single<OTableModel> {
        return remote.loginOTable(otableBody)
    }

    override fun saveUserIndicatorActive(isEnable: Boolean): Completable {
        return local.saveUserIndicatorActive(isEnable)
    }

    override fun getUserIndicatorActive(): Single<Boolean> {
        return local.getUserIndicatorActive()
    }

}