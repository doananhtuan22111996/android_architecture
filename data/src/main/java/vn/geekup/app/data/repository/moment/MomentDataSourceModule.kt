package vn.geekup.app.data.repository.moment

import dagger.Binds
import dagger.Module
import vn.geekup.app.data.di.local.LocalModule
import vn.geekup.app.data.di.qualifier.source.Source
import vn.geekup.app.data.di.qualifier.source.SourceLocal
import vn.geekup.app.data.di.qualifier.source.SourceRemote
import vn.geekup.app.data.di.remote.RemoteModule
import vn.geekup.app.domain.repository.MomentRepository
import javax.inject.Singleton

@Module(includes = [LocalModule::class, RemoteModule::class])
abstract class MomentDataSourceModule {

    @Singleton
    @Binds
    @SourceLocal
    abstract fun provideMomentLocalDataSource(momentLocalDataSource: MomentLocalDataSource): MomentRepository

    @Singleton
    @Binds
    @SourceRemote
    abstract fun provideMomentRemoteDataSource(momentRemoteDataSource: MomentRemoteDataSource): MomentRepository

    @Singleton
    @Binds
    @Source
    abstract fun provideMomentDataSource(momentDataSource: MomentDataSource): MomentRepository
}