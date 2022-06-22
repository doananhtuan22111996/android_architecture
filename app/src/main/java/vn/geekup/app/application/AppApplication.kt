package vn.geekup.app.application

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import vn.geekup.app.data.Config
import vn.geekup.app.data.di.localModules
import vn.geekup.app.data.di.remoteModules
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

    override fun onCreate() {
        super.onCreate()
        instance = this
        setupDebug()
        startKoin()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun appOnForegrounded() {
        // Handle action app to foreground
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun appOnBackgrounded() {
        // Handle action app to background
    }

    private fun setupDebug() {
        Timber.plant(Timber.DebugTree())
        Config.setup(this)
    }

    private fun startKoin() {
        startKoin {
            // use Koin logger
            androidLogger()
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