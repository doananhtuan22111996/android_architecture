package vn.geekup.app.data.repository.user

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.geekup.app.data.di.local.LocalModule
import vn.geekup.app.data.di.qualifier.source.Source
import vn.geekup.app.data.di.qualifier.source.SourceLocal
import vn.geekup.app.data.di.qualifier.source.SourceRemote
import vn.geekup.app.data.di.remote.RemoteModule
import vn.geekup.app.domain.repository.UserRepository
import javax.inject.Singleton

@Module(includes = [LocalModule::class, RemoteModule::class])
@InstallIn(SingletonComponent::class)
abstract class UserDataSourceModule {

    @Singleton
    @Binds
    @SourceLocal
    abstract fun provideUserLocalDataSource(userLocalDataSource: UserLocalDataSource): UserRepository

    @Singleton
    @Binds
    @SourceRemote
    abstract fun provideUserRemoteDataSource(userRemoteDataSource: UserRemoteDataSource): UserRepository

    @Singleton
    @Binds
    @Source
    abstract fun provideUserDataSource(userDataSource: UserDataSource): UserRepository
}
