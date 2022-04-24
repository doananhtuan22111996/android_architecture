package vn.geekup.app.domain.repository

import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.user.OTableModel

interface AuthRepository {

    fun logout(): Single<Boolean>

    fun getToken(): Single<String>

    suspend fun saveToken(token : String) : Flow<ResultModel<Unit>>

    suspend fun loginOTable(otableBody: OTableRequestBody): Flow<ResultModel<OTableModel>>

}