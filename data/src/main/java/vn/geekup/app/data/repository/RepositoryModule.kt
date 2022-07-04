package vn.geekup.app.data.repository

import org.koin.dsl.module
import vn.geekup.app.domain.repository.AuthRepository
import vn.geekup.app.domain.repository.MomentRepository

var repositoryModules = module(createdAtStart = true) {
    factory<AuthRepository> {
        AuthDataSource(get(), get())
    }

    factory<MomentRepository> {
        MomentDataSource(get(), get())
    }
}