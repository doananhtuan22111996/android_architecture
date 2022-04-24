package vn.geekup.app.data.remote.auth

import io.reactivex.rxjava3.core.Single
import retrofit2.http.*
import vn.geekup.app.data.model.general.BaseResponseVO
import vn.geekup.app.data.model.general.ListResponseVO
import vn.geekup.app.data.model.moment.MomentCommentVO
import vn.geekup.app.data.model.moment.MomentLikeVO
import vn.geekup.app.data.model.moment.MomentVO
import vn.geekup.app.data.model.user.UserEngagementVO
import vn.geekup.app.data.model.user.UserIndicatorVO
import vn.geekup.app.data.model.user.UserInfoVO
import vn.geekup.app.domain.dto.MomentLikeRequestBody
import vn.geekup.app.domain.dto.MomentPostCommentRequestBody

interface AliaApiService {

//    ================== User ==========

    @GET("fetch")
    fun getUserInfo(): Single<BaseResponseVO<UserInfoVO>>

    @GET("me/engagements")
    fun getUserEngagements(
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String = "desc",
    ): Single<BaseResponseVO<ListResponseVO<UserEngagementVO>>>

    @GET("me/indicator")
    fun getUserIndicator(): Single<BaseResponseVO<UserIndicatorVO>>

//    ================== Moment ==========

    @GET("me/subscriptions/moments")
    fun getMomentFeeds(
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String = "desc",
        @Query("date") dates: ArrayList<String>? = null,
    ): Single<BaseResponseVO<ListResponseVO<MomentVO>>>

    @GET("me/subscriptions/moments")
    suspend fun getFlowMomentFeeds(
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String = "desc",
        @Query("date") dates: ArrayList<String>? = null,
    ): BaseResponseVO<ListResponseVO<MomentVO>>


    @PUT("moments/{momentId}/reactions")
    fun putMomentLike(
        @Path("momentId") momentId: Int = 0,
        @Body momentLikeRequestBody: MomentLikeRequestBody
    ): Single<BaseResponseVO<MomentLikeVO>>

    @POST("moments/{momentId}/share-to-nexion")
    fun shareMomentToNexion(
        @Path("momentId") momentId: Int = 0,
    ): Single<BaseResponseVO<Any>>

    @GET("moments/{momentId}")
    fun getMomentDetail(
        @Path("momentId") momentId: Int = 0,
    ): Single<BaseResponseVO<MomentVO>>

    @GET("moments/{momentId}")
    suspend fun getFlowMomentDetail(
        @Path("momentId") momentId: Int = 0,
    ): BaseResponseVO<MomentVO>

    @GET("moments/{momentId}/comments")
    fun getMomentComments(
        @Path("momentId") momentId: Int = 0,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String = "asc",
    ): Single<BaseResponseVO<ListResponseVO<MomentCommentVO>>>

    @POST("moments/{momentId}/comments")
    fun postMomentComments(
        @Path("momentId") momentId: Int = 0,
        @Body momentPostCommentRequestBody: MomentPostCommentRequestBody
    ): Single<BaseResponseVO<MomentCommentVO>>

}
