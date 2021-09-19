package vn.geekup.app.application

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import vn.geekup.app.data.Config
import vn.geekup.app.di.application.ApplicationComponent
import vn.geekup.app.di.application.DaggerApplicationComponent

class AppApplication : DaggerApplication(), LifecycleObserver {

    lateinit var applicationComponent: ApplicationComponent
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        private val instanceLock = Any()
        var instance: AppApplication? = null
            set(value) {
                synchronized(instanceLock) {
                    field = value
                }
            }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        applicationComponent =
            DaggerApplicationComponent.factory().create(this) as ApplicationComponent
        return applicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        firebaseAnalytics = Firebase.analytics
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
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