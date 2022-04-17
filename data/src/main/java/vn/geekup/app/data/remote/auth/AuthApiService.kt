package vn.geekup.app.data.remote.auth

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import vn.geekup.app.data.model.general.BaseResponseVO
import vn.geekup.app.data.model.user.OTableVO
import vn.geekup.app.data.model.user.TokenVO
import vn.geekup.app.domain.dto.RefreshTokenBody

interface AuthApiService {

    @GET("auth/login/otable")
    suspend fun loginOTable(
        @Query("code") code: String = "",
        @Query("state") state: String = ""
    ): BaseResponseVO<OTableVO>

    @POST("auth/refresh")
    fun refreshToken(
        @Body refreshToken: RefreshTokenBody,
    ): Single<BaseResponseVO<TokenVO>>

    @POST("auth/logout")
    fun logout(): Completable
}
