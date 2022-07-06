package vn.geekup.app.application

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import vn.geekup.app.data.Config
import vn.geekup.app.data.localModules
import vn.geekup.app.data.remoteModules
import vn.geekup.app.data.repository.repositoryModules
import vn.geekup.app.di.applicationModules
import vn.geekup.app.di.useCaseModules
import vn.geekup.app.di.viewModelModules

class AppApplication : Application() {

    companion object {
        private val instanceLock = Any()
        var instance: AppApplication? = null
            set(value) {
                synchronized(instanceLock) {
                    field = value
                }
            }
    }

    private val defaultLifecycleObserver = object : DefaultLifecycleObserver {

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            //your code here
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            //your code here
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        setupDebug()
        startKoin()

        ProcessLifecycleOwner.get().lifecycle.addObserver(defaultLifecycleObserver)
    }

    private fun setupDebug() {
        Timber.plant(Timber.DebugTree())
        Config.setup(this)
    }

    private fun startKoin() {
        startKoin {
            // use Koin logger
            if (Config.isDebug) {
                androidLogger()
            }
            androidContext(this@AppApplication)
            // declare modules
            modules(
                applicationModules,
                localModules,
                remoteModules,
                repositoryModules,
                useCaseModules,
                viewModelModules,
            )
        }
    }
}