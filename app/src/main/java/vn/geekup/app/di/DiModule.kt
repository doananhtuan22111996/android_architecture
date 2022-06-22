package vn.geekup.app.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import vn.geekup.app.application.AppApplication
import vn.geekup.app.base.BaseViewModel
import vn.geekup.app.domain.repository.AuthRepository
import vn.geekup.app.domain.usecase.AuthUseCase
import vn.geekup.app.domain.usecase.AuthUseCaseImplement
import vn.geekup.app.domain.usecase.MomentUseCase
import vn.geekup.app.domain.usecase.MomentUseCaseImplement
import vn.geekup.app.module.login.LoginViewModel
import vn.geekup.app.module.moment.MomentViewModel
import vn.geekup.app.module.root.RootViewModel
import vn.geekup.app.network.NetworkChange

val applicationModules = module {
    single { NetworkChange(androidContext()) }
}

val useCaseModules = module(createdAtStart = true) {
    factory<AuthUseCase> { AuthUseCaseImplement(get()) }
    factory<MomentUseCase> { MomentUseCaseImplement(get()) }
}

val viewModelModules = module(createdAtStart = true) {
    viewModel { RootViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { MomentViewModel(get(), get()) }
}