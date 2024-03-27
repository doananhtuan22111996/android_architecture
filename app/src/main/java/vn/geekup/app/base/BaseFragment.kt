package vn.geekup.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import vn.geekup.app.databinding.FragmentBaseBinding
import vn.geekup.domain.model.general.ResultModel
import vn.geekup.app.network.NetworkStatus
import vn.geekup.app.utils.setupViewClickHideKeyBoard

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding>: Fragment() {

  protected lateinit var baseActivity: BaseActivity<*, *>

  protected abstract val viewModel: VM

  protected lateinit var fragmentBinding: VB
  protected lateinit var navController: NavController
  private lateinit var baseBinding: FragmentBaseBinding

  var tagFrag: String? = null

  abstract fun provideViewBinding(parent: ViewGroup): VB

  abstract fun onInitLayout(view: View, savedInstanceState: Bundle?)

  open fun initViewModelByActivityLifecycle(): Boolean {
    return false
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    baseActivity = activity as BaseActivity<*, *>
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    baseBinding = FragmentBaseBinding.inflate(layoutInflater)
    fragmentBinding = provideViewBinding(baseBinding.root)
    baseActivity.window?.setupViewClickHideKeyBoard(baseBinding.root)
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

  open fun handleServerErrorState(serverErrorException: ResultModel.ServerErrorException?) {
    Toast.makeText(context, serverErrorException?.message.toString(), Toast.LENGTH_SHORT).show()
  }

}
