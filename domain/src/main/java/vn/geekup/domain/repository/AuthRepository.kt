package vn.geekup.domain.repository

import kotlinx.coroutines.flow.Flow
import vn.geekup.domain.dto.OTableRequestBody
import vn.geekup.domain.model.general.ResultModel
import vn.geekup.domain.model.user.OTableModel

interface AuthRepository {

    fun logout(): Flow<ResultModel<Boolean>>

    suspend fun saveToken(token : String) : Flow<ResultModel<Unit>>

    suspend fun loginOTable(otableBody: OTableRequestBody): Flow<ResultModel<OTableModel>>

    suspend fun loginWithTravel(): Flow<ResultModel<OTableModel>>

}