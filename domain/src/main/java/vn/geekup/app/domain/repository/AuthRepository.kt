package vn.geekup.app.domain.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.model.general.ConfigModel
import vn.geekup.app.domain.model.user.OTableModel

interface AuthRepository {

    fun fetchAppConfig(): Single<ConfigModel>

    fun logout(): Single<Boolean>

    fun clearAuthInfo(): Completable

    fun getToken(): Single<String>

    fun saveToken(token: String): Completable

    fun saveRefreshToken(refreshToken: String): Completable

    fun loginOTable(otableBody: OTableRequestBody): Single<OTableModel>

    fun saveUserIndicatorActive(isEnable: Boolean): Completable

    fun getUserIndicatorActive(): Single<Boolean>

}