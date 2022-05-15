package vn.geekup.app.data.remote.auth

import retrofit2.Response
import retrofit2.http.*
import vn.geekup.app.data.model.general.ListResponse
import vn.geekup.app.data.model.general.ObjectResponse
import vn.geekup.app.data.model.moment.MomentVO

interface AliaApiService {

//    ================== Moment ==========

    @GET("me/subscriptions/moments")
    suspend fun getFlowMomentFeeds(
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 5,
        @Query("sort") sort: String = "desc",
        @Query("date") dates: ArrayList<String>? = null,
    ): Response<ObjectResponse<ListResponse<MomentVO>>>

}
