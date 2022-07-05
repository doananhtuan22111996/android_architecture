package vn.geekup.app.module.moment

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
import kotlin.collections.ArrayList

class MomentViewModel(
    networkChange: NetworkChange,
    private val momentUseCase: MomentUseCase
) : BaseViewModel(networkChange) {

    suspend fun fetchPagingNetworkFlow(): Flow<PagingData<MomentModel>> {
        return momentUseCase.getPagingMomentFeeds().cachedIn(viewModelScope)
    }

    suspend fun fetchPagingLocalFlow(): Flow<PagingData<MomentModel>> {
        return momentUseCase.getPagingLocalMomentFeeds().cachedIn(viewModelScope)
    }
}
