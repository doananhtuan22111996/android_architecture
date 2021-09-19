package vn.geekup.app.di.android

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vn.geekup.app.module.moment.feed.MomentFeedFragment
import vn.geekup.app.di.viewmodel.ViewModelFactoryModule
import vn.geekup.app.module.login.LoginFragment
import vn.geekup.app.module.main.MainFragment
import vn.geekup.app.module.media.PreviewMainFragment
import vn.geekup.app.module.moment.detail.MomentDetailFragment
import vn.geekup.app.module.notification.NotificationFragment
import vn.geekup.app.module.profile.ProfileFragment

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector(modules = [ViewModelFactoryModule::class])
    abstract fun loginFragment(): LoginFragment

    @ContributesAndroidInjector(modules = [ViewModelFactoryModule::class])
    abstract fun mainFragment(): MainFragment

    @ContributesAndroidInjector(modules = [ViewModelFactoryModule::class])
    abstract fun momentFeedFragment(): MomentFeedFragment

    @ContributesAndroidInjector(modules = [ViewModelFactoryModule::class])
    abstract fun notificationFragment(): NotificationFragment

    @ContributesAndroidInjector(modules = [ViewModelFactoryModule::class])
    abstract fun profileFragment(): ProfileFragment

    @ContributesAndroidInjector(modules = [ViewModelFactoryModule::class])
    abstract fun momentDetailFragment(): MomentDetailFragment

    @ContributesAndroidInjector(modules = [ViewModelFactoryModule::class])
    abstract fun previewMainFragment(): PreviewMainFragment

}