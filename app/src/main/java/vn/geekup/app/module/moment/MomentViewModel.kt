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

    val userInfo: MutableLiveData<UserInfoModel?> = MutableLiveData()
    val userInfoIndicator: MutableLiveData<UserIndicatorModelV?> = MutableLiveData()

    val moments: MutableLiveData<ArrayList<MomentModelV>> = MutableLiveData()
    val shareMoment: MutableLiveData<MetaDataModel> = MutableLiveData()
    val pagingState: MutableLiveData<PagingState> = MutableLiveData()

    val momentDetail: MutableLiveData<MomentModelV> = MutableLiveData()
    val momentComments: MutableLiveData<ArrayList<MomentCommentModelV>> = MutableLiveData()

    val newMomentState: MutableLiveData<Triple<MomentModelV, Int, MomentActionV>> =
        MutableLiveData()

    private val localMomentFeeds: ArrayList<MomentModelV> = arrayListOf()
    private val localMomentComments: ArrayList<MomentCommentModelV> = arrayListOf()
    private var nextCursor: String? = null
    var nextPreviousCommentCursor: String? = null
    private var nextNewestCommentCursor: String? = null

    override fun loadArgumentsBundle(bundle: Bundle?) {
        val momentId = bundle?.getInt(KEY_ARGUMENT_FRAGMENT, 0)
        if (momentId == null || momentId == 0) return
        getMomentDetail(momentId)
        getMomentComments(momentId)
    }

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
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

    fun requestMomentLike(momentId: Int? = 0) {
        putMomentLikeLocal(momentId, isUserLiked = true)
        momentUseCase.putMomentLike(
            momentId = momentId ?: 0,
            momentLikeRequestBody = MomentLikeRequestBody()
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                putMomentLikeLocal(it.momentId, it.newLikesCount, isUserLiked = true)
            }, {
                putMomentLikeLocal(momentId, isUserLiked = false)
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

    fun shareMomentToNexion(momentId: Int? = 0) {
        momentUseCase.shareMomentToNexion(momentId ?: 0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                shareMoment.value = it
            }, {
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

    fun getPreviousMomentComments(momentId: Int? = 0) {
        momentUseCase.getMomentComments(
            MomentCommentRequestBody(
                momentId = momentId,
                cursor = nextPreviousCommentCursor,
                limit = KEY_PAGING_LIMIT_10,
                sort = MomentSort.DESC()
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                nextPreviousCommentCursor = it.nextCursor
                it.items?.toArrayMomentCommentModelV { results ->
                    localMomentComments.addAll(0, results)
                    momentComments.value = localMomentComments
                }
            }, {
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

    fun postMomentComment(momentId: Int? = 0, comment: String? = "") {
        momentUseCase.postMomentComments(
            momentId = momentId ?: 0,
            momentPostCommentRequestBody = MomentPostCommentRequestBody(content = comment)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getNewestMomentComments(momentId)
            }, {
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

    fun updateMomentToMomentFeeds(newMoment: MomentModelV?, momentAction: MomentActionV) {
        Observable.create<Pair<MomentModelV, Int>> {
            val index = localMomentFeeds.findIndex(newMoment?.id ?: 0)
            if (index == -1) return@create
            localMomentFeeds[index] = localMomentFeeds[index].copy(
                content = newMoment?.content,
                likeCount = newMoment?.likeCount,
                commentCount = newMoment?.commentCount,
                thisUserLiked = newMoment?.thisUserLiked ?: false,
                thisUserCommented = newMoment?.thisUserCommented ?: false
            )
            it.onNext(Pair(localMomentFeeds[index], index))
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                newMomentState.value = Triple(it.first, it.second, momentAction)
            }.push()
    }

    private fun putMomentLikeLocal(
        momentId: Int? = 0,
        newLikeCount: Int = 0,
        isUserLiked: Boolean = true
    ) {
        Observable.create<Pair<MomentModelV, Int>> {
            val moment = localMomentFeeds.find { item ->
                item.id == momentId
            }
            val position = localMomentFeeds.indexOf(moment)
            moment?.apply {
                this.likeCount =
                    when {
                        newLikeCount > 0 -> newLikeCount
                        isUserLiked -> this.likeCount?.plus(1)
                        else -> this.likeCount?.minus(1)
                    }
                this.thisUserLiked = isUserLiked
            }
            it.onNext(if (moment != null) Pair(moment, position) else return@create)
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                newMomentState.value = Triple(it.first, it.second, MomentActionV.MomentLike)
            }.push()
    }

    private fun getNewestMomentComments(momentId: Int? = 0) {
        momentUseCase.getMomentComments(
            MomentCommentRequestBody(
                momentId = momentId,
                cursor = nextNewestCommentCursor,
                limit = KEY_PAGING_LIMIT_10,
                sort = MomentSort.ASC()
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
                val results: ArrayList<MomentCommentModelV> = arrayListOf()
                it.items?.forEach {
                    val existIndex = localMomentComments.findIndex(it.id ?: 0)
                    if (existIndex > -1) {
                        localMomentComments[existIndex] = MomentCommentModelV().model2ModelV(it)
                    } else {
                        results.add(MomentCommentModelV().model2ModelV(it))
                    }
                }
                results.sortBy { comment -> comment.createdAt?.convertToCurrentTimeZone()?.time }
                nextNewestCommentCursor =
                    if (results.lastIndex > -1) results[results.lastIndex].id.toString() else null
                localMomentComments.addAll(results)
                // When Post Comment success -> update All comment Count moment
                val index = localMomentFeeds.findIndex(momentId ?: 0)
                if (index == -1) return@subscribe
                val newMoment = localMomentFeeds[index]
                newMoment.commentCount = newMoment.commentCount?.plus(results.size) ?: 0
                newMoment.thisUserCommented = true
                updateMomentToMomentFeeds(newMoment, momentAction = MomentActionV.MomentComment)
                momentComments.value = localMomentComments
            }, {
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

    private fun getMomentDetail(momentId: Int? = 0) {
        momentUseCase.getMomentDetail(momentId ?: 0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                momentDetail.value = MomentModelV().model2ModelV(it)
            }, {
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }

    private fun getMomentComments(momentId: Int? = 0) {
        localMomentComments.clear()
        nextPreviousCommentCursor = null
        nextNewestCommentCursor = null
        momentUseCase.getMomentComments(
            MomentCommentRequestBody(
                momentId = momentId,
                cursor = null,
                limit = KEY_PAGING_LIMIT_10,
                sort = MomentSort.DESC()
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                nextPreviousCommentCursor = it.nextCursor
                it.items?.toArrayMomentCommentModelV { results ->
                    nextNewestCommentCursor =
                        if (results.lastIndex > -1) results[results.lastIndex].id.toString() else null
                    localMomentComments.addAll(results)
                    momentComments.value = localMomentComments
                }
            }, {
                executingServerErrorException(it as? ServerErrorException)
            }).push()
    }
}