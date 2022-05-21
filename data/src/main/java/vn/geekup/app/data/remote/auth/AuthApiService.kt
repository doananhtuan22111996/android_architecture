package vn.geekup.app.data.remote.auth

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import vn.geekup.app.data.model.general.ObjectResponse
import vn.geekup.app.data.model.user.OTableVO

interface AuthApiService {

    @GET("auth/login/otable")
    suspend fun loginOTable(
        @Query("code") code: String = "",
        @Query("state") state: String = ""
    ): Response<ObjectResponse<OTableVO>>

    @POST("auth/logout")
    fun logout(): Response<ObjectResponse<Nothing>>
}
