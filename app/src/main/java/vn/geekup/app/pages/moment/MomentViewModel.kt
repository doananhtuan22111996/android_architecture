package vn.geekup.app.pages.moment

import androidx.paging.PagingData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.*
import vn.geekup.app.base.BaseViewModel
import vn.geekup.domain.model.moment.MomentModel
import vn.geekup.domain.usecase.MomentUseCase
import vn.geekup.app.network.NetworkChange

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
