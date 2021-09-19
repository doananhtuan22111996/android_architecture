package vn.geekup.app.domain.repository

import io.reactivex.rxjava3.core.Single
import vn.geekup.app.domain.dto.UserEngagementRequestBody
import vn.geekup.app.domain.model.general.BaseModelListResponse
import vn.geekup.app.domain.model.user.UserEngagementModel
import vn.geekup.app.domain.model.user.UserIndicatorModel
import vn.geekup.app.domain.model.user.UserInfoModel

interface UserRepository {

    fun getUserInfoServer(): Single<UserInfoModel>

    fun getUserEngagements(userEngagementRequestBody: UserEngagementRequestBody): Single<BaseModelListResponse<UserEngagementModel>>

    fun getUserIndicator(): Single<UserIndicatorModel>

}