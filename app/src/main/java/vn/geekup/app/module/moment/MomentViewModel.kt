package vn.geekup.app.module.moment

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import vn.geekup.app.base.BaseViewModel
import vn.geekup.app.domain.dto.*
import vn.geekup.app.domain.model.general.MetaDataModel
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.user.UserInfoModel
import vn.geekup.app.domain.throwable.ServerErrorException
import vn.geekup.app.domain.usecase.MomentUseCase
import vn.geekup.app.model.moment.MomentActionV
import vn.geekup.app.model.moment.MomentCommentModelV
import vn.geekup.app.model.moment.MomentModelV
import vn.geekup.app.model.PagingState
import vn.geekup.app.model.user.UserIndicatorModelV
import vn.geekup.app.network.NetworkChange
import vn.geekup.app.utils.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MomentViewModel @Inject constructor(
    networkChange: NetworkChange,
    private val momentUseCase: MomentUseCase
) : BaseViewModel(networkChange) {

    /**
     * @see localMomentFeeds -> List Moment Feed
     * @see nextCursor -> Moment item next for paging Moment Feed
     * @see pagingState -> Show Indicator Load more MomentFeed
     * @see localMomentComments -> List Comment for Moment Detail
     * @see nextPreviousCommentCursor -> Comment item previous for paging Comment Feed -> Load data old comments
     * @see nextNewestCommentCursor -> Comment item last list for paging Comment Feed -> Load data newest comments
     * @see userInfo, userInfoIndicator -> Received Data callback from RootActivity then save in Moment ViewModel
     * @method updateMomentToMomentFeeds -> Update New state Moment when like, comment change from moment detail to Moment List
     * @method loadArgumentsBundle -> Received MomentId when open Moment Detail from Moment Feed, User Tab
     */

    val moments: MutableLiveData<ArrayList<MomentModelV>> = MutableLiveData()
    val pagingState: MutableLiveData<PagingState> = MutableLiveData()

    private val localMomentFeeds: ArrayList<MomentModelV> = arrayListOf()
    private var nextCursor: String? = null

    fun getMomentFeeds(date: String = "", isReload: Boolean = false) {
        if (isReload) {
            localMomentFeeds.clear()
            nextCursor = null
        }
        if (nextCursor?.isEmpty() == true) return
        pagingState.value =
            if (nextCursor != null) PagingState.LoadingMore else PagingState.Loaded
        momentUseCase.getMomentFeeds(
            MomentFeedRequestBody(
                cursor = nextCursor,
                limit = KEY_PAGING_LIMIT_20,
                dates = if (date.isNotEmpty()) arrayListOf(date) else null
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                nextCursor = it.nextCursor
                it.items?.toArrayMomentModelV { results ->
                    localMomentFeeds.addAll(results)
                    moments.value = localMomentFeeds
                }
                pagingState.value = PagingState.Loaded
            }, {
                pagingState.value = PagingState.Loaded
//                executingServerErrorException(it as? ResultModel.ServerErrorException)
            }).push()
    }

}