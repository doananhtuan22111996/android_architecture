package vn.geekup.app.data.di.remote

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import vn.geekup.app.data.Config
import vn.geekup.app.data.di.qualifier.interceptor.InterceptorAuthenticate
import vn.geekup.app.data.di.qualifier.interceptor.InterceptorDefault
import vn.geekup.app.data.di.qualifier.source.Source
import vn.geekup.app.data.remote.auth.AuthApiService
import vn.geekup.app.domain.repository.AuthRepository
import vn.geekup.app.data.di.qualifier.retrofit.OkHttpClientAuthenticated
import vn.geekup.app.data.di.qualifier.retrofit.OkHttpClientDefault
import vn.geekup.app.data.di.qualifier.retrofit.RetrofitAuthenticated
import vn.geekup.app.data.di.qualifier.retrofit.RetrofitDefault
import vn.geekup.app.data.remote.auth.AliaApiService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class RemoteModule {

    @Singleton
    @Provides
    @InterceptorDefault
    fun provideDefaultInterceptor(): Interceptor {
        return DefaultInterceptor()
    }

    @Singleton
    @Provides
    @InterceptorAuthenticate
    fun provideAuthenticateInterceptor(@Source authRepository: AuthRepository): Interceptor {
        return AuthenticatedInterceptor(authRepository)
    }

    @Singleton
    @Provides
    @OkHttpClientDefault
    fun provideOkHttpClientDefault(
        @InterceptorDefault interceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        stethoInterceptor: StethoInterceptor,
        cache: Cache
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(Config.TimeOut.TIMEOUT_READ_SECOND.toLong(), TimeUnit.SECONDS)
            .connectTimeout(Config.TimeOut.TIMEOUT_CONNECT_SECOND.toLong(), TimeUnit.SECONDS)
            .writeTimeout(Config.TimeOut.TIMEOUT_WRITE_SECOND.toLong(), TimeUnit.SECONDS)
        if (Config.isDebug) {
            builder.addInterceptor(httpLoggingInterceptor)
            builder.addNetworkInterceptor(stethoInterceptor)
        } else {
            builder.cache(cache)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    @RetrofitDefault
    fun provideRetrofitDefault(
        @OkHttpClientDefault okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        rxJava3CallAdapterFactory: RxJava3CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Config.mainDomain)
            .addConverterFactory(NullOrEmptyConverterFactory())
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava3CallAdapterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @OkHttpClientAuthenticated
    fun provideOkHttpClientAuthenticated(
        @InterceptorAuthenticate interceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        stethoInterceptor: StethoInterceptor,
        cache: Cache
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(Config.TimeOut.TIMEOUT_READ_SECOND.toLong(), TimeUnit.SECONDS)
            .connectTimeout(Config.TimeOut.TIMEOUT_CONNECT_SECOND.toLong(), TimeUnit.SECONDS)
            .writeTimeout(Config.TimeOut.TIMEOUT_WRITE_SECOND.toLong(), TimeUnit.SECONDS)
        if (Config.isDebug) {
            builder.addInterceptor(httpLoggingInterceptor)
            builder.addNetworkInterceptor(stethoInterceptor)
        } else {
            builder.cache(cache)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    @RetrofitAuthenticated
    fun provideRetrofitAuthenticated(
        @OkHttpClientAuthenticated okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        rxJava3CallAdapterFactory: RxJava3CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Config.mainDomain)
            .addConverterFactory(NullOrEmptyConverterFactory())
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava3CallAdapterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthApiService(@RetrofitDefault retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideAliaApiService(@RetrofitAuthenticated retrofit: Retrofit): AliaApiService {
        return retrofit.create(AliaApiService::class.java)
    }

}