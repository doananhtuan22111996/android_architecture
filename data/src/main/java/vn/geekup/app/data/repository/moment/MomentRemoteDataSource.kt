package vn.geekup.app.data.repository.moment

import io.reactivex.rxjava3.core.Single
import vn.geekup.app.data.services.MiddleWareService
import vn.geekup.app.domain.dto.*
import vn.geekup.app.domain.model.general.BaseModelListResponse
import vn.geekup.app.domain.model.general.MetaDataModel
import vn.geekup.app.domain.model.moment.MomentCommentModel
import vn.geekup.app.domain.model.moment.MomentLikeModel
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.domain.repository.MomentRepository
import javax.inject.Inject

class MomentRemoteDataSource @Inject constructor(
    private val middleWareService: MiddleWareService
) : MomentRepository {

    override fun getMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Single<BaseModelListResponse<MomentModel>> {
        val requestFunc = middleWareService.aliaApiService.getMomentFeeds(
            cursor = momentFeedRequestBody.cursor,
            limit = momentFeedRequestBody.limit,
            sort = when (momentFeedRequestBody.sort) {
                MomentSort.DESC() -> MomentSort.DESC().sortName
                else -> MomentSort.ASC().sortName
            }, dates = momentFeedRequestBody.dates
        )
        return middleWareService.requestSingleApi(requestFunc = requestFunc) {
            val results: BaseModelListResponse<MomentModel> =
                BaseModelListResponse(limit = it.data?.limit, nextCursor = it.data?.nextCursor)
            val items: ArrayList<MomentModel> = arrayListOf()
            it.data?.items?.forEach { item ->
                items.add(item.vo2Model())
            }
            results.items = items
            Single.just(results)
        } as Single<BaseModelListResponse<MomentModel>>
    }

    override fun putMomentLike(
        momentId: Int,
        momentLikeRequestBody: MomentLikeRequestBody
    ): Single<MomentLikeModel> {
        val requestFunc = middleWareService.aliaApiService.putMomentLike(
            momentId = momentId,
            momentLikeRequestBody = momentLikeRequestBody
        )
        return middleWareService.requestSingleApi(requestFunc = requestFunc) {
            Single.just(it.data?.vo2Model() ?: MomentLikeModel())
        } as Single<MomentLikeModel>
    }

    override fun shareMomentToNexion(momentId: Int): Single<MetaDataModel> {
        val requestFunc = middleWareService.aliaApiService.shareMomentToNexion(momentId = momentId)
        return middleWareService.requestSingleApi(requestFunc = requestFunc) {
            Single.just(
                MetaDataModel(
                    statusCode = it.meta?.statusCode,
                    message = it.meta?.message,
                    status = it.meta?.status
                )
            )
        } as Single<MetaDataModel>
    }

    override fun getMomentDetail(momentId: Int): Single<MomentModel> {
        val requestFunc = middleWareService.aliaApiService.getMomentDetail(momentId = momentId)
        return middleWareService.requestSingleApi(requestFunc = requestFunc) {
            Single.just(it.data?.vo2Model() ?: MomentModel())
        } as Single<MomentModel>
    }

    override fun getMomentComments(momentCommentRequestBody: MomentCommentRequestBody): Single<BaseModelListResponse<MomentCommentModel>> {
        val requestFunc = middleWareService.aliaApiService.getMomentComments(
            momentId = momentCommentRequestBody.momentId ?: 0,
            cursor = momentCommentRequestBody.cursor,
            limit = momentCommentRequestBody.limit,
            sort = when (momentCommentRequestBody.sort) {
                MomentSort.ASC() -> MomentSort.ASC().sortName
                else -> MomentSort.DESC().sortName
            }
        )
        return middleWareService.requestSingleApi(requestFunc = requestFunc) {
            val results: BaseModelListResponse<MomentCommentModel> =
                BaseModelListResponse(limit = it.data?.limit, nextCursor = it.data?.nextCursor)
            val items: ArrayList<MomentCommentModel> = arrayListOf()
            it.data?.items?.forEach { item ->
                items.add(item.vo2Model())
            }
            results.items = items
            Single.just(results)
        } as Single<BaseModelListResponse<MomentCommentModel>>
    }

    override fun postMomentComments(
        momentId: Int,
        momentPostCommentRequestBody: MomentPostCommentRequestBody
    ): Single<MomentCommentModel> {
        val requestFunc = middleWareService.aliaApiService.postMomentComments(
            momentId = momentId,
            momentPostCommentRequestBody = momentPostCommentRequestBody
        )
        return middleWareService.requestSingleApi(requestFunc = requestFunc) {
            Single.just(it.data?.vo2Model() ?: MomentCommentModel())
        } as Single<MomentCommentModel>
    }
}
