package vn.geekup.app.data.repository.auth

import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber
import vn.geekup.app.data.Config.ErrorCode.CODE_200
import vn.geekup.app.data.remote.auth.AuthApiService
import vn.geekup.app.domain.dto.OTableRequestBody
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.user.OTableModel
import vn.geekup.app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(private val authApiService: AuthApiService) :
    AuthRepository {

    override fun getToken(): Single<String> {
        return Single.never()
    }

    override fun logout(): Single<Boolean> {
        return authApiService.logout().andThen(Single.just(true))
    }

    override suspend fun saveToken(token: String): Flow<ResultModel<Unit>> = emptyFlow();

    override suspend fun loginOTable(otableBody: OTableRequestBody): Flow<ResultModel<OTableModel>> {
        return flow {
            Timber.e("Thread Data Source: ${Thread.currentThread().name}")
            val response = authApiService.loginOTable(otableBody.token, otableBody.currentAuthority)
            Timber.e("Thread Data Source in Map: ${Thread.currentThread().name}")
            if (response.meta?.statusCode != CODE_200) {
                emit(
                    ResultModel.ServerErrorException(
                        response.meta?.statusCode,
                        response.meta?.message
                    )
                )
            } else {
                emit(ResultModel.Success(data = response.data?.vo2Model() ?: OTableModel()))
            }
        }.flowOn(Dispatchers.IO)
    }
}