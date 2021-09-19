package vn.geekup.app.di.application

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import vn.geekup.app.application.AppApplication
import vn.geekup.app.network.NetworkChange
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): AppApplication {
        return app as AppApplication
    }

    @Provides
    @Singleton
    fun provideContext(appApplication: AppApplication): Context {
        return appApplication.applicationContext
    }

    @Provides
    @Singleton
    fun provideNetworkChange(context: Context): NetworkChange {
        return NetworkChange(context)
    }
}