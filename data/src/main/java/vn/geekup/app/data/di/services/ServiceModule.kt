package vn.geekup.app.data.di.services

import dagger.Module
import dagger.Provides
import vn.geekup.app.data.di.local.LocalModule
import vn.geekup.app.data.di.remote.RemoteModule
import vn.geekup.app.data.local.PreferenceWrapper
import vn.geekup.app.data.remote.auth.AliaApiService
import vn.geekup.app.data.remote.auth.AuthApiService
import vn.geekup.app.data.services.MiddleWareService
import javax.inject.Singleton

@Module(includes = [RemoteModule::class, LocalModule::class])
class ServiceModule {

    @Singleton
    @Provides
    fun provideMiddleWareService(
        authApiService: AuthApiService,
        aliaApiService: AliaApiService,
        preferenceWrapper: PreferenceWrapper
    ): MiddleWareService =
        MiddleWareService(
            authApiService = authApiService,
            aliaApiService = aliaApiService,
            preferenceWrapper = preferenceWrapper
        )
}