package vn.geekup.app.domain.usecase

import io.reactivex.rxjava3.core.Single
import vn.geekup.app.domain.dto.UserEngagementRequestBody
import vn.geekup.app.domain.model.general.BaseModelListResponse
import vn.geekup.app.domain.model.user.UserEngagementModel
import vn.geekup.app.domain.model.user.UserIndicatorModel
import vn.geekup.app.domain.model.user.UserInfoModel
import vn.geekup.app.domain.repository.UserRepository

interface UserUseCase {

    fun getUserInfoServer(): Single<UserInfoModel>

    fun getUserEngagements(userEngagementRequestBody: UserEngagementRequestBody): Single<BaseModelListResponse<UserEngagementModel>>

    fun getUserIndicator(): Single<UserIndicatorModel>

}

class UserUseCaseImplement(private var userRepository: UserRepository) : UserUseCase {

    override fun getUserInfoServer(): Single<UserInfoModel> {
        return userRepository.getUserInfoServer().flatMap {
            Single.just(it)
        }
    }

    override fun getUserEngagements(userEngagementRequestBody: UserEngagementRequestBody): Single<BaseModelListResponse<UserEngagementModel>> {
        return userRepository.getUserEngagements(userEngagementRequestBody)
    }

    override fun getUserIndicator(): Single<UserIndicatorModel> {
        return userRepository.getUserIndicator()
    }

}