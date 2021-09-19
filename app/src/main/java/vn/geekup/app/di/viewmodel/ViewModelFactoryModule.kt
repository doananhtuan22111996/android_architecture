package vn.geekup.app.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import vn.geekup.app.module.login.LoginViewModel
import vn.geekup.app.module.media.PreviewMainViewModel
import vn.geekup.app.module.moment.MomentViewModel
import vn.geekup.app.module.notification.NotificationViewModel
import vn.geekup.app.module.profile.ProfileViewModel
import vn.geekup.app.module.root.RootViewModel

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RootViewModel::class)
    abstract fun bindRootViewModel(viewModel: RootViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MomentViewModel::class)
    abstract fun bindMomentFeedViewModel(viewModel: MomentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel::class)
    abstract fun bindNotificationViewModel(viewModel: NotificationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PreviewMainViewModel::class)
    abstract fun bindPreviewMainViewModel(viewModel: PreviewMainViewModel): ViewModel

}