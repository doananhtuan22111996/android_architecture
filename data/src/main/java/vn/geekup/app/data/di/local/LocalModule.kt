package vn.geekup.app.data.di.local

import android.content.Context
import dagger.Module
import dagger.Provides
import vn.geekup.app.data.local.PreferenceWrapper
import javax.inject.Singleton

@Module
class LocalModule {

  @Singleton
  @Provides
  fun provideSharePreference(context: Context): PreferenceWrapper {
    return PreferenceWrapper(context)
  }

}