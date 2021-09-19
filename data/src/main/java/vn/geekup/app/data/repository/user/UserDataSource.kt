package vn.geekup.app.data.repository.user

import io.reactivex.rxjava3.core.Single
import vn.geekup.app.data.di.qualifier.source.SourceLocal
import vn.geekup.app.data.di.qualifier.source.SourceRemote
import vn.geekup.app.domain.dto.UserEngagementRequestBody
import vn.geekup.app.domain.model.general.BaseModelListResponse
import vn.geekup.app.domain.model.user.UserEngagementModel
import vn.geekup.app.domain.model.user.UserIndicatorModel
import vn.geekup.app.domain.model.user.UserInfoModel
import vn.geekup.app.domain.repository.UserRepository
import javax.inject.Inject

class UserDataSource @Inject constructor(
    @SourceLocal
    private val local: UserRepository,

    @SourceRemote
    private val remote: UserRepository
)  : UserRepository{

    override fun getUserInfoServer(): Single<UserInfoModel> {
        return remote.getUserInfoServer()
    }

    override fun getUserEngagements(userEngagementRequestBody: UserEngagementRequestBody): Single<BaseModelListResponse<UserEngagementModel>> {
       return remote.getUserEngagements(userEngagementRequestBody)
    }

    override fun getUserIndicator(): Single<UserIndicatorModel> {
       return remote.getUserIndicator()
    }

}