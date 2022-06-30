package vn.geekup.app.module.moment

import androidx.lifecycle.*
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import vn.geekup.app.base.BaseViewModel
import vn.geekup.app.domain.dto.*
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.domain.model.moment.MomentModel
import vn.geekup.app.domain.usecase.MomentUseCase
import vn.geekup.app.model.moment.MomentModelV
import vn.geekup.app.model.PagingState
import vn.geekup.app.network.NetworkChange
import vn.geekup.app.utils.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MomentViewModel @Inject constructor(
    networkChange: NetworkChange,
    private val momentUseCase: MomentUseCase
) : BaseViewModel(networkChange) {

    val moments: MutableLiveData<ArrayList<MomentModelV>> = MutableLiveData()
    val pagingState: MutableLiveData<PagingState> = MutableLiveData()

    private val localMomentFeeds: ArrayList<MomentModelV> = arrayListOf()
    private var nextCursor: String? = null

    val pagingMoment: MediatorLiveData<PagingData<MomentModel>> = MediatorLiveData()

    fun getFlowMomentFeeds(date: String = "", isReload: Boolean = false) {
        if (isReload) {
            localMomentFeeds.clear()
            nextCursor = null
        }
        if (nextCursor?.isEmpty() == true) return
        pagingState.value =
            if (nextCursor != null) PagingState.LoadingMore else PagingState.Loaded

        viewModelScope.launch((Dispatchers.Main)) {
            momentUseCase.getFlowMomentFeeds(
                MomentFeedRequestBody(
                    cursor = nextCursor,
                    limit = KEY_PAGING_LIMIT_20,
                    dates = if (date.isNotEmpty()) arrayListOf(date) else null
                )
            ).collectLatest {
                when (it) {
                    is ResultModel.Success -> {
                        nextCursor = it.nextCursor
                        it.data?.toArrayMomentModelV { results ->
                            localMomentFeeds.addAll(results)
                            moments.value = localMomentFeeds
                        }
                        pagingState.value = PagingState.Loaded
                    }
                    is ResultModel.Loading -> {
                        Timber.e("Moment Network Loading")
                    }
                    else -> {
                        pagingState.value = PagingState.Loaded
                        executingServerErrorException(it as? ResultModel.ServerErrorException)
                    }
                }
            }
        }
    }


    fun getPagingMomentFeeds(date: String = "") {
        viewModelScope.launch((Dispatchers.Main)) {
            Timber.tag("getPagingMomentFeeds").e(Thread.currentThread().name)
            pagingMoment.addSource(
                momentUseCase.getPagingMomentFeeds(
                    MomentFeedRequestBody(
                        cursor = "1",
                        limit = KEY_PAGING_LIMIT_20,
                        dates = if (date.isNotEmpty()) arrayListOf(date) else null
                    )
                ).asLiveData()
            ) {
                pagingMoment.value = it
            }
        }
    }
}
