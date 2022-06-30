package vn.geekup.app.data.repository

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import vn.geekup.app.data.repository.auth.AuthLocalDataSource
import vn.geekup.app.data.repository.auth.AuthDataSource
import vn.geekup.app.data.repository.auth.AuthRemoteDataSource
import vn.geekup.app.data.repository.moment.MomentDataSource
import vn.geekup.app.data.repository.moment.MomentLocalDataSource
import vn.geekup.app.data.repository.moment.MomentRemoteDataSource
import vn.geekup.app.domain.repository.AuthRepository
import vn.geekup.app.domain.repository.MomentRepository

var repositoryModules = module(createdAtStart = true) {
    factory<AuthRepository> {
        AuthDataSource(
            AuthLocalDataSource(get()),
            AuthRemoteDataSource(get())
        )
    }

    factory<MomentRepository> {
        MomentDataSource(
            MomentRemoteDataSource(get())
        )
    }
}