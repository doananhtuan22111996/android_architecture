package vn.geekup.app.data.repository.auth

import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import vn.geekup.app.data.Config.SharePreference.KEY_AUTH_TOKEN
import vn.geekup.app.data.local.PreferenceWrapper
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.user.OTableModel
import vn.geekup.app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(private val preferenceWrapper: PreferenceWrapper) :
    AuthRepository {

    override fun logout(): Single<Boolean> {
        return Single.just(true)
    }

    override fun getToken(): Single<String> {
        return Single.just(preferenceWrapper.getString(KEY_AUTH_TOKEN, ""))
    }

    override suspend fun saveToken(token: String): Flow<ResultModel<Unit>> {
        preferenceWrapper.saveString(KEY_AUTH_TOKEN, token)
        return emptyFlow()
    }

    override suspend fun loginOTable(otableBody: OTableRequestBody): Flow<ResultModel<OTableModel>> {
        return emptyFlow()
    }

}
