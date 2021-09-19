package vn.geekup.app.di.usecase

import dagger.Module
import dagger.Provides
import vn.geekup.app.data.di.qualifier.source.Source
import vn.geekup.app.domain.repository.AuthRepository
import vn.geekup.app.domain.repository.MomentRepository
import vn.geekup.app.domain.repository.UserRepository
import vn.geekup.app.domain.usecase.*
import javax.inject.Singleton

@Module
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

    @Provides
    @Singleton
    fun provideUserUseCase(@Source userRepository: UserRepository): UserUseCase {
        return UserUseCaseImplement(userRepository)
    }


}