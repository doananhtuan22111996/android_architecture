package vn.geekup.app.di.application

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import vn.geekup.app.application.AppApplication
import vn.geekup.app.data.di.services.ServiceModule
import vn.geekup.app.data.repository.auth.AuthDataSourceModule
import vn.geekup.app.data.repository.moment.MomentDataSourceModule
import vn.geekup.app.data.repository.user.UserDataSourceModule
import vn.geekup.app.di.android.FragmentBuildersModule
import vn.geekup.app.di.usecase.UseCaseModule
import vn.geekup.app.di.android.ActivityBuildersModule
import vn.geekup.app.di.viewmodel.ViewModelFactoryModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        ActivityBuildersModule::class,
        FragmentBuildersModule::class,
        ViewModelFactoryModule::class,
        ServiceModule::class,
        UseCaseModule::class,
        AuthDataSourceModule::class,
        MomentDataSourceModule::class,
        UserDataSourceModule::class,
    ]
)
interface ApplicationComponent : AndroidInjector<AppApplication> {

    @Component.Factory
    abstract class Builder : AndroidInjector.Factory<AppApplication>

//  fun authUseCase(): AuthUseCase

}