package vn.geekup.app.domain.usecase

import kotlinx.coroutines.flow.*
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.user.OTableModel
import vn.geekup.app.domain.repository.AuthRepository

interface AuthUseCase {

    fun logout(): Flow<ResultModel<Boolean>>

    suspend fun saveToken(token: String): Flow<ResultModel<Unit>>

    suspend fun loginOTable(otableBody: OTableRequestBody): Flow<ResultModel<OTableModel>>

    suspend fun loginWithTravel(): Flow<ResultModel<OTableModel>>

}

class AuthUseCaseImplement(private var authRepository: AuthRepository) : AuthUseCase {

    override fun logout(): Flow<ResultModel<Boolean>> {
        return authRepository.logout()
    }

    override suspend fun loginOTable(otableBody: OTableRequestBody): Flow<ResultModel<OTableModel>> {
        return authRepository.loginOTable(otableBody)
    }

    override suspend fun saveToken(token: String): Flow<ResultModel<Unit>> {
        return authRepository.saveToken(token)
    }

    override suspend fun loginWithTravel(): Flow<ResultModel<OTableModel>> {
        return authRepository.loginWithTravel()
    }

}