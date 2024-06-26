package vn.root.domain.usecase

import kotlinx.coroutines.flow.Flow
import vn.root.domain.model.ResultModel
import vn.root.domain.model.TokenModel
import vn.root.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private var repository: AuthRepository) :
	BaseUseCase<Any, ResultModel<TokenModel>>() {
	override fun execute(vararg params: Any?): Flow<ResultModel<TokenModel>> {
		return repository.login()
	}
}