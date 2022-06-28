package vn.geekup.app.data.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.geekup.app.data.Config
import vn.geekup.app.data.Config.DataStore.DATA_STORE_NAME
import vn.geekup.app.data.Config.DataStore.KEY_AUTH_TOKEN
import vn.geekup.app.data.local.PreferenceDataStore
import vn.geekup.app.data.remote.AuthApiService
import vn.geekup.app.data.remote.AliaApiService
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.reflect.KProperty

private fun provideCache(context: Context): Cache {
    val file = File(context.cacheDir, Config.Cache.CACHE_FILE_NAME)
    val isSuccess = file.mkdirs()
    return if (isSuccess) {
        Cache(file, Config.Cache.CACHE_FILE_SIZE)
    } else Cache(context.cacheDir, Config.Cache.CACHE_FILE_SIZE)
}

private fun provideOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    dataStore: PreferenceDataStore,
    cache: Cache
): OkHttpClient {
    val builder = OkHttpClient.Builder()
        .readTimeout(Config.TimeOut.TIMEOUT_READ_SECOND, TimeUnit.SECONDS)
        .connectTimeout(Config.TimeOut.TIMEOUT_CONNECT_SECOND, TimeUnit.SECONDS)
        .writeTimeout(Config.TimeOut.TIMEOUT_WRITE_SECOND, TimeUnit.SECONDS)
        .addNetworkInterceptor(Interceptor { chain ->
            var request = chain.request()
            val builder = request.newBuilder()
            val token = runBlocking {
                dataStore.getString(KEY_AUTH_TOKEN)
            }
            if (token.isNotEmpty()) {
                builder.addHeader("Authorization", "Bearer $token")
            }
            request = builder.build()
            chain.proceed(request)
        })
    if (Config.isDebug) {
        builder.addInterceptor(httpLoggingInterceptor)
        builder.addNetworkInterceptor(StethoInterceptor())
    } else {
        builder.cache(cache)
    }
    return builder.build()
}

inline fun <reified T> provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(Config.mainDomain)
        .client(okHttpClient)
        .addConverterFactory(NullOrEmptyConverterFactory())
        .addConverterFactory(gsonConverterFactory)
        .build()
    return retrofit.create(T::class.java)
}

val localModules = module {
    single {
        PreferenceDataStore(
            preferencesDataStore(
                name = DATA_STORE_NAME,
//                produceMigrations = { context ->
//                    // Since we're migrating from SharedPreferences, add a migration based on the
//                    // SharedPreferences name
//                    // Optional if previous project using SharePreferences
//                    listOf(SharedPreferencesMigration(context, DATA_STORE_NAME))
//                }
            ).getValue(androidContext(), KProperty<*>::javaClass),
        )
    }
}

val remoteModules = module {
    single { GsonConverterFactory.create() }
    single { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) }
    single { provideOkHttpClient(get(), get(), provideCache(androidContext())) }
    single { provideRetrofit<AuthApiService>(get(), get()) }
    single { provideRetrofit<AliaApiService>(get(), get()) }
}