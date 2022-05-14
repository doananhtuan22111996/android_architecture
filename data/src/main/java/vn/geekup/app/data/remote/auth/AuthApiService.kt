package vn.geekup.app.data.remote.auth

import io.reactivex.rxjava3.core.Completable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import vn.geekup.app.data.model.general.ObjectResponseVO
import vn.geekup.app.data.model.user.OTableVO

interface AuthApiService {

    @GET("auth/login/otable")
    suspend fun loginOTable(
        @Query("code") code: String = "",
        @Query("state") state: String = ""
    ): Response<ObjectResponseVO<OTableVO>>

    @POST("auth/logout")
    fun logout(): Completable
}
