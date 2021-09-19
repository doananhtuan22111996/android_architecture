package vn.geekup.app.data.repository.user

import io.reactivex.rxjava3.core.Single
import vn.geekup.app.data.local.PreferenceWrapper
import vn.geekup.app.domain.dto.UserEngagementRequestBody
import vn.geekup.app.domain.model.general.BaseModelListResponse
import vn.geekup.app.domain.model.user.UserEngagementModel
import vn.geekup.app.domain.model.user.UserIndicatorModel
import vn.geekup.app.domain.model.user.UserInfoModel
import vn.geekup.app.domain.repository.UserRepository
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(private val preferenceWrapper: PreferenceWrapper) :
    UserRepository {

    override fun getUserInfoServer(): Single<UserInfoModel> {
        return Single.never()
    }

    override fun getUserEngagements(userEngagementRequestBody: UserEngagementRequestBody): Single<BaseModelListResponse<UserEngagementModel>> {
        return Single.never()
    }

    override fun getUserIndicator(): Single<UserIndicatorModel> {
        return Single.never()
    }
}