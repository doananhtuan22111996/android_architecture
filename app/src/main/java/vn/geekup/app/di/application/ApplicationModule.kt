package vn.geekup.app.di.application

import android.content.Context
import dagger.Module
import dagger.Provides
import vn.geekup.app.application.AppApplication
import vn.geekup.app.network.NetworkChange
import javax.inject.Singleton

@Module
class ApplicationModule {

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