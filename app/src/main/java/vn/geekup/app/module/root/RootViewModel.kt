package vn.geekup.app.module.root

import androidx.lifecycle.MutableLiveData
import vn.geekup.app.base.BaseViewModel
import vn.geekup.app.network.NetworkChange

class RootViewModel(
    networkChange: NetworkChange,
) : BaseViewModel(networkChange) {

    val isLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

//     fun isRootViewReadyFunc(): Boolean {
//         viewModelScope.launch {
//             flow {
//                 delay(2000)
//                 emit(true)
//             }.collectLatest {
//
//             }
//         }
//
//        return true
//    }


}