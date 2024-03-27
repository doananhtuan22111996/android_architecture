package vn.geekup.data.repository

import org.koin.dsl.module
import vn.geekup.domain.repository.AuthRepository
import vn.geekup.domain.repository.MomentRepository

var repositoryModules = module(createdAtStart = true) {
    factory<AuthRepository> {
        AuthDataSource(get(), get())
    }

    factory<MomentRepository> {
        MomentDataSource(get(), get())
    }
}