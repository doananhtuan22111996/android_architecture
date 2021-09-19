package vn.geekup.app.data.repository.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.geekup.app.data.di.local.LocalModule
import vn.geekup.app.data.di.qualifier.source.Source
import vn.geekup.app.data.di.qualifier.source.SourceLocal
import vn.geekup.app.data.di.qualifier.source.SourceRemote
import vn.geekup.app.data.di.remote.RemoteModule
import vn.geekup.app.domain.repository.AuthRepository
import javax.inject.Singleton

@Module(includes = [LocalModule::class, RemoteModule::class])
@InstallIn(SingletonComponent::class)
abstract class AuthDataSourceModule {
  
  @Singleton
  @Binds
  @SourceLocal
  abstract fun provideAuthLocalDataSource(authLocalDataSource: AuthLocalDataSource): AuthRepository
  
  @Singleton
  @Binds
  @SourceRemote
  abstract fun provideAuthRemoteDataSource(authRemoteDataSource: AuthRemoteDataSource): AuthRepository
  
  @Singleton
  @Binds
  @Source
  abstract fun provideAuthDataSource(authDataSource: AuthDataSource): AuthRepository
  
}