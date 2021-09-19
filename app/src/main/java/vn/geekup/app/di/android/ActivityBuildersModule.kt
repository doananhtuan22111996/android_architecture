package vn.geekup.app.di.android

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vn.geekup.app.module.root.RootActivity
import vn.geekup.app.di.viewmodel.ViewModelFactoryModule

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [ViewModelFactoryModule::class])
    abstract fun rootActivity(): RootActivity

}