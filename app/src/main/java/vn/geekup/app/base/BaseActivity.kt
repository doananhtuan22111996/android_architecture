package vn.geekup.app.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import vn.geekup.app.databinding.ActivityBaseBinding
import vn.geekup.app.domain.model.general.ResultModel
import vn.geekup.app.network.NetworkStatus
import vn.geekup.app.utils.setupViewClickHideKeyBoard

abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {

  protected abstract val viewModel: VM
  protected lateinit var activityBinding: VB
  private lateinit var baseBinding: ActivityBaseBinding

  abstract fun provideViewModelClass(): Class<VM>

  abstract fun provideViewBinding(parent: ViewGroup): VB

  abstract fun onInitLayout(savedInstanceState: Bundle?)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    baseBinding = ActivityBaseBinding.inflate(layoutInflater)
    super.setContentView(baseBinding.root)
    activityBinding = provideViewBinding(baseBinding.flBase)
    window?.setupViewClickHideKeyBoard(baseBinding.root)
    viewModel.loadIntentBundle(intent)
    onInitLayout(savedInstanceState)
    bindViewModel()
  }

  @CallSuper
  open fun bindViewModel() {
    viewModel.fullScreenLoadingState.observe(this, this::handleViewFullScreenLoading)
    viewModel.networkChangeState.observe(this, this::handleNetworkChangeStatus)
    viewModel.errorServerState.observe(this, this::handleServerErrorState)
  }

  open fun handleViewFullScreenLoading(isFullScreenLoading: Boolean) {
    baseBinding.layoutLoading.rlParentProgressbar.visibility =
      if (isFullScreenLoading) View.VISIBLE else View.GONE
  }

  open fun handleNetworkChangeStatus(networkStatus: NetworkStatus) {
    // Do nothing, maybe handle at subclass
  }

  open fun handleServerErrorState(serverErrorException: ResultModel.ServerErrorException?) {
    Toast.makeText(this, serverErrorException?.message.toString(), Toast.LENGTH_SHORT).show()
  }
}