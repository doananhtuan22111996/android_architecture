package vn.geekup.app.data.repository.moment

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import vn.geekup.app.data.local.PreferenceWrapper
import vn.geekup.app.domain.dto.MomentFeedRequestBody
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.domain.repository.MomentRepository
import javax.inject.Inject

class MomentLocalDataSource @Inject constructor(private val preferenceWrapper: PreferenceWrapper) :
    MomentRepository {

    override suspend fun getFlowMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Flow<ResultModel<ArrayList<MomentModel>>> =
        emptyFlow()


    override suspend fun getFlowMomentDetail(id: Int): Flow<ResultModel<MomentModel>> =
        emptyFlow()
}