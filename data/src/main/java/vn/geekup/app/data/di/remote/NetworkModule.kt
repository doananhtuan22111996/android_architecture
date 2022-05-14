package vn.geekup.app.data.di.remote

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import vn.geekup.app.data.Config
import vn.geekup.app.data.di.qualifier.source.FileCache
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    @FileCache
    fun provideFileCache(context: Context): File {
        val file = File(context.cacheDir, Config.Cache.CACHE_FILE_NAME)
        val isSuccess = file.mkdirs()
        return if (isSuccess) {
            file
        } else context.cacheDir
    }

    @Singleton
    @Provides
    fun provideCache(@FileCache file: File): Cache {
        return Cache(file, Config.Cache.CACHE_FILE_SIZE)
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideStethoInterceptor(): StethoInterceptor {
        return StethoInterceptor()
    }
}