package vn.geekup.app.data.repository.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber
import vn.geekup.app.data.Config.DataStore.KEY_AUTH_TOKEN
import vn.geekup.app.data.Config.DataStore.KEY_AUTH_TOKEN_SECURE
import vn.geekup.app.data.local.PreferenceDataStore
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.user.OTableModel
import vn.geekup.app.domain.repository.AuthRepository

class AuthLocalDataSource constructor(private val dataStore: PreferenceDataStore) :
    AuthRepository {

    override fun logout(): Flow<ResultModel<Boolean>> = emptyFlow()

    override suspend fun saveToken(token: String): Flow<ResultModel<Unit>> {
        dataStore.setString(KEY_AUTH_TOKEN, token)
        return emptyFlow()
    }

    override suspend fun loginOTable(otableBody: OTableRequestBody): Flow<ResultModel<OTableModel>> {
        return emptyFlow()
    }

}
