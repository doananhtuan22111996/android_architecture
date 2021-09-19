package vn.geekup.app.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import dagger.android.support.DaggerFragment
import vn.geekup.app.databinding.FragmentBaseBinding
import vn.geekup.app.di.viewmodel.ViewModelFactory
import vn.geekup.app.domain.throwable.ServerErrorException
import vn.geekup.app.network.NetworkStatus
import vn.geekup.app.utils.setupViewClickHideKeyBoard
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding>: DaggerFragment() {

  protected lateinit var activity: BaseActivity<*, *>

  @Inject
  protected lateinit var factory: ViewModelFactory
  protected lateinit var viewModel: VM
  protected lateinit var fragmentBinding: VB
  protected lateinit var navController: NavController
  private lateinit var baseBinding: FragmentBaseBinding

  var tagFrag: String? = null

  abstract fun provideViewModelClass(): Class<VM>

  abstract fun provideViewBinding(parent: ViewGroup): VB

  abstract fun onInitLayout(view: View, savedInstanceState: Bundle?)

  open fun initViewModelByActivityLifecycle(): Boolean {
    return false
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    activity = context as BaseActivity<*, *>
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(if (initViewModelByActivityLifecycle()) activity else this, factory).get(provideViewModelClass())
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    baseBinding = FragmentBaseBinding.inflate(layoutInflater)
    fragmentBinding = provideViewBinding(baseBinding.root)
    activity.window?.setupViewClickHideKeyBoard(baseBinding.root)
    return baseBinding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.loadArgumentsBundle(arguments)
    navController = Navigation.findNavController(view)
    onInitLayout(view, savedInstanceState)
    bindViewModel()
  }

  @CallSuper
  open fun bindViewModel() {
    viewModel.networkChangeState.observe(viewLifecycleOwner, this::handleNetworkChangeStatus)

    viewModel.errorServerState.observe(viewLifecycleOwner, this::handleServerErrorState)
  }

  open fun handleNetworkChangeStatus(networkStatus: NetworkStatus) {
    // Do nothing, maybe handle at subclass
  }

  open fun handleServerErrorState(serverErrorException: ServerErrorException) {
    Toast.makeText(context, serverErrorException.message.toString(), Toast.LENGTH_SHORT).show()
  }

}
