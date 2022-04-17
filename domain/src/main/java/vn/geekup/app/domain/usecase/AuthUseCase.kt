package vn.geekup.app.domain.usecase

import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.*
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.user.OTableModel
import vn.geekup.app.domain.repository.AuthRepository

interface AuthUseCase {

    fun logout(): Single<Boolean>

    suspend fun loginOTable(otableBody: OTableRequestBody): Flow<ResultModel<OTableModel>>

}

class AuthUseCaseImplement(private var authRepository: AuthRepository) : AuthUseCase {

    override fun logout(): Single<Boolean> {
        return authRepository.logout()
    }

    override suspend fun loginOTable(otableBody: OTableRequestBody): Flow<ResultModel<OTableModel>> {
        return authRepository.loginOTable(otableBody)
    }

}