package vn.geekup.app.data.repository.user

import io.reactivex.rxjava3.core.Single
import vn.geekup.app.data.services.MiddleWareService
import vn.geekup.app.domain.dto.MomentSort
import vn.geekup.app.domain.model.general.SortType
import vn.geekup.app.domain.dto.UserEngagementRequestBody
import vn.geekup.app.domain.model.general.BaseModelListResponse
import vn.geekup.app.domain.model.user.UserEngagementModel
import vn.geekup.app.domain.model.user.UserIndicatorModel
import vn.geekup.app.domain.model.user.UserInfoModel
import vn.geekup.app.domain.repository.UserRepository
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(private val middleWareService: MiddleWareService) :
    UserRepository {

    override fun getUserInfoServer(): Single<UserInfoModel> {
        val requestFunc = middleWareService.aliaApiService.getUserInfo()
        return middleWareService.requestSingleApi(requestFunc = requestFunc) {
            Single.just(it.data?.vo2Model() ?: UserInfoModel())
        } as Single<UserInfoModel>
    }

    override fun getUserEngagements(userEngagementRequestBody: UserEngagementRequestBody): Single<BaseModelListResponse<UserEngagementModel>> {
        val requestFunc = middleWareService.aliaApiService.getUserEngagements(
            cursor = userEngagementRequestBody.cursor,
            limit = userEngagementRequestBody.limit,
            sort = when (userEngagementRequestBody.sort) {
                SortType.DESC() -> MomentSort.DESC().sortName
                else -> MomentSort.ASC().sortName
            }
        )
        return middleWareService.requestSingleApi(requestFunc = requestFunc) {
            val results: BaseModelListResponse<UserEngagementModel> =
                BaseModelListResponse(limit = it.data?.limit, nextCursor = it.data?.nextCursor)
            val items: ArrayList<UserEngagementModel> = arrayListOf()
            it.data?.items?.forEach { item ->
                items.add(item.vo2Model())
            }
            results.items = items
            Single.just(results)
        } as Single<BaseModelListResponse<UserEngagementModel>>
    }

    override fun getUserIndicator(): Single<UserIndicatorModel> {
        val requestFunc = middleWareService.aliaApiService.getUserIndicator()
        return middleWareService.requestSingleApi(requestFunc = requestFunc) {
            Single.just(it.data?.vo2Model() ?: UserIndicatorModel())
        } as Single<UserIndicatorModel>
    }
}
