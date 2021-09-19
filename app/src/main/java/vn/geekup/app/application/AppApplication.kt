package vn.geekup.app.application

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import vn.geekup.app.data.Config

@HiltAndroidApp
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
}