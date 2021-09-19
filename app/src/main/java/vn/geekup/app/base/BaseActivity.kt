package vn.geekup.app.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import dagger.android.support.DaggerAppCompatActivity
import vn.geekup.app.databinding.ActivityBaseBinding
import vn.geekup.app.network.NetworkStatus
import vn.geekup.app.utils.setupViewClickHideKeyBoard
import vn.geekup.app.di.viewmodel.ViewModelFactory
import vn.geekup.app.domain.throwable.ServerErrorException
import javax.inject.Inject

abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : DaggerAppCompatActivity() {

  @Inject
  protected lateinit var factory: ViewModelFactory
  protected lateinit var viewModel: VM
  protected lateinit var activityBinding: VB
  private lateinit var baseBinding: ActivityBaseBinding

  abstract fun provideViewModelClass(): Class<VM>

  abstract fun provideViewBinding(parent: ViewGroup): VB

  abstract fun onInitLayout(savedInstanceState: Bundle?)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(provideViewModelClass())
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

  open fun handleServerErrorState(serverErrorException: ServerErrorException) {
    Toast.makeText(this, serverErrorException.message.toString(), Toast.LENGTH_SHORT).show()
  }
}