package vn.geekup.app.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.geekup.app.data.di.qualifier.source.Source
import vn.geekup.app.domain.repository.AuthRepository
import vn.geekup.app.domain.repository.MomentRepository
import vn.geekup.app.domain.usecase.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideAuthUseCase(@Source authRepository: AuthRepository): AuthUseCase {
        return AuthUseCaseImplement(authRepository)
    }

    @Provides
    @Singleton
    fun provideMomentUseCase(@Source authRepository: MomentRepository): MomentUseCase {
        return MomentUseCaseImplement(authRepository)
    }

}