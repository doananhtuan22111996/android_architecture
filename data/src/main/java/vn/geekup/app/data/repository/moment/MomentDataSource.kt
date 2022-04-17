package vn.geekup.app.data.repository.moment

import io.reactivex.rxjava3.core.Single
import vn.geekup.app.data.di.qualifier.source.SourceLocal
import vn.geekup.app.data.di.qualifier.source.SourceRemote
import vn.geekup.app.domain.dto.MomentCommentRequestBody
import vn.geekup.app.domain.dto.MomentFeedRequestBody
import vn.geekup.app.domain.dto.MomentLikeRequestBody
import vn.geekup.app.domain.dto.MomentPostCommentRequestBody
import vn.geekup.app.domain.model.general.BaseModelListResponse
import vn.geekup.app.domain.model.general.MetaDataModel
import vn.geekup.app.domain.model.moment.MomentCommentModel
import vn.geekup.app.domain.model.moment.MomentLikeModel
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.domain.repository.MomentRepository
import javax.inject.Inject

class MomentDataSource @Inject constructor(
    @SourceLocal
    private val local: MomentRepository,

    @SourceRemote
    private val remote: MomentRepository
) : MomentRepository {

    override fun getMomentFeeds(momentFeedRequestBody: MomentFeedRequestBody): Single<BaseModelListResponse<MomentModel>> {
        return remote.getMomentFeeds(momentFeedRequestBody)
    }

}