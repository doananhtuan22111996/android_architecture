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
}
