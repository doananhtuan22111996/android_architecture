package vn.geekup.app.module.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import vn.geekup.app.R
import vn.geekup.app.base.BaseFragment
import vn.geekup.app.databinding.FragmentMainBinding
import vn.geekup.app.module.root.RootViewModel
import vn.geekup.app.utils.setAppColorStatusBar
import vn.geekup.app.utils.visible

@AndroidEntryPoint
class MainFragment : BaseFragment<RootViewModel, FragmentMainBinding>() {

    // Listener for MomentFeedFragment
    interface OnChildFragmentListener {
        fun onAutoReloadMomentFeed()
    }

    override val viewModel: RootViewModel by activityViewModels()

    private var onChildFragmentListener: OnChildFragmentListener? = null

    private var destinationChangedListener: NavController.OnDestinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            selectedBottomNavigation(getViewDestination(destination.id))
        }

    override fun initViewModelByActivityLifecycle(): Boolean = true

    override fun provideViewBinding(parent: ViewGroup): FragmentMainBinding =
        FragmentMainBinding.inflate(layoutInflater, parent, true)

    override fun onInitLayout(view: View, savedInstanceState: Bundle?) {
        baseActivity.setAppColorStatusBar(R.color.color_white)
        fragmentBinding.fragment = this
        setupMainNavigation()
        selectedBottomNavigation(fragmentBinding.bottomNavigation.lnHome)
//        fragmentBinding.bottomNavigation.lnHome.setDoubleClickListener(singleListener = {
//            onClickBottomNavigation(it)
//        }, doubleListener = this::setAutoReloadMomentFeed)

        fragmentBinding.bottomNavigation.lnHome.setOnClickListener {
            onClickBottomNavigation(it)
            setAutoReloadMomentFeed()
        }

    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(destinationChangedListener)
    }

    fun setOnChildFragmentListener(onChildFragmentListener: OnChildFragmentListener) {
        this.onChildFragmentListener = onChildFragmentListener
    }

    fun onClickBottomNavigation(view: View) {
        selectedBottomNavigation(view)
        when (view.id) {
            R.id.lnHome -> keepInstanceBackStacks(R.id.momentFeedFragment)
            R.id.ivNotification -> keepInstanceBackStacks(R.id.notificationFragment)
            else -> keepInstanceBackStacks(R.id.profileFragment)
        }
    }

    fun bottomNavigationState(isShow: Boolean) {
        fragmentBinding.bottomNavigation.root.visible(isShow)
    }

    // Double click Button Home -> Auto Reload page and scroll to Top
    private fun setAutoReloadMomentFeed() {
        onChildFragmentListener?.onAutoReloadMomentFeed()
    }

    private fun setupMainNavigation() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.main_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.nav_main)
        // Todo Handle Argument in intent
//    val navArgument = NavArgument.Builder().setDefaultValue().build()
//    graph.addArgument(
//      KEY_ARGUMENT,
//      navArgument
//    ) // This is where you pass the bundle data from Activity to StartDestination
        navHostFragment.navController.graph = graph
    }

    private fun selectedBottomNavigation(view: View) {
        if (view.id == R.id.lnHome) {
            fragmentBinding.bottomNavigation.ivHome.isSelected = true
            fragmentBinding.bottomNavigation.tvMomentFeeds.isSelected = true
        } else {
            fragmentBinding.bottomNavigation.ivHome.isSelected = false
            fragmentBinding.bottomNavigation.tvMomentFeeds.isSelected = false
        }
        fragmentBinding.bottomNavigation.ivNotification.isSelected = view.id == R.id.ivNotification
        fragmentBinding.bottomNavigation.ivProfile.isSelected = view.id == R.id.ivProfile
    }

    private fun getViewDestination(@IdRes navId: Int): View {
        return when (navId) {
            R.id.momentFeedFragment -> fragmentBinding.bottomNavigation.lnHome
            R.id.notificationFragment -> fragmentBinding.bottomNavigation.ivNotification
            else -> fragmentBinding.bottomNavigation.ivProfile
        }
    }

    @SuppressLint("RestrictedApi")
    private fun keepInstanceBackStacks(@IdRes navId: Int) {
        if (navController.currentDestination?.id == navId) return
        val navBackStackEntry = navController.backStack.find {
            it.destination.id == navId
        }
        if (navBackStackEntry != null) {
            navController.popBackStack(navId, false)
        } else {
            navController.navigate(navId)
        }
    }
}