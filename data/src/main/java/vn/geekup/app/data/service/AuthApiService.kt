package vn.geekup.app.data.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import vn.geekup.app.data.model.general.ObjectResponse
import vn.geekup.app.data.model.user.OTableVO
import vn.geekup.app.data.model.user.UserInfoVO

interface AuthApiService {

    @GET("auth/login/otable")
    suspend fun loginOTable(
        @Query("code") code: String = "",
        @Query("state") state: String = ""
    ): Response<ObjectResponse<OTableVO>>

    @POST("users-login")
    suspend fun loginWithTravel(
        @Query("username") email: String? = "jason@vinova.sg",
        @Query("password") password: String? = "123123",
        @Query("fcm_token") fcmToken: String? = "dGKlJJU1lCc:APA91bGZTz25rKtcb5WobysyPQSUp0Bfp4w1hblFjgWQeGdCEZwgFmRTCTQX9vhDk2WazWcvwpOHn8MV4NyTjrgE5vFEraxP5GbAMOnqYmo6FyVGy924yS98pEYSJXBJZ_5g_56nIFuC"
    ): Response<ObjectResponse<OTableVO>>

    @POST("auth/logout")
    fun logout(): Response<ObjectResponse<Nothing>>
}
