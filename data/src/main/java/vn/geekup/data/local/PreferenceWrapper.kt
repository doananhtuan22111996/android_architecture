package vn.geekup.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import android.security.keystore.KeyProperties
import android.security.keystore.KeyGenParameterSpec
import com.google.gson.Gson
import timber.log.Timber
import vn.geekup.data.BuildConfig

@Suppress("unused", "MemberVisibilityCanBePrivate")
class PreferenceWrapper(private val context: Context) {

    private var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences = initEncryptedPreference()
    }

    fun clear() {
        sharedPreferences?.edit()?.clear()?.apply()
    }

    fun remove(key: String) {
        sharedPreferences?.edit()?.remove(key)?.apply()
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences?.edit()?.putBoolean(key, value)?.apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences?.getBoolean(key, defaultValue) ?: false
    }

    fun saveString(key: String, value: String) {
        sharedPreferences?.edit()?.putString(key, value)?.apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences?.getString(key, defaultValue) ?: ""
    }

    fun saveInt(key: String, value: Int) {
        sharedPreferences?.edit()?.putInt(key, value)?.apply()
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences?.getInt(key, defaultValue) ?: 0
    }

    fun saveLong(key: String, value: Long) {
        sharedPreferences?.edit()?.putLong(key, value)?.apply()
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return sharedPreferences?.getLong(key, defaultValue) ?: 0L
    }

    fun saveFloat(key: String, value: Float) {
        sharedPreferences?.edit()?.putFloat(key, value)?.apply()
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return sharedPreferences?.getFloat(key, defaultValue) ?: 0f
    }

    fun toJson(jsonObject: Any?): String {
        return Gson().toJson(jsonObject)
    }

    fun <T> fromJson(value: String, clazz: Class<T>): T? {
        return Gson().fromJson(value, clazz)
    }

    fun saveObject(key: String, value: Any?) {
        sharedPreferences?.edit()?.putString(key, toJson(value))?.apply()
    }

    fun <T> getObject(key: String, defaultValue: String = "", clazz: Class<T>): T? {
        return fromJson(sharedPreferences?.getString(key, defaultValue) ?: "", clazz)
    }

    private fun initEncryptedPreference(): SharedPreferences? {
        if (initMaterKey() == null) return null
        return EncryptedSharedPreferences.create(
            context,
            BuildConfig.LIBRARY_PACKAGE_NAME,
            initMaterKey()!!, // masterKey created above
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun initMaterKey(): MasterKey? {
        try {
            val spec = KeyGenParameterSpec.Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                .build()
            return MasterKey.Builder(context)
                .setKeyGenParameterSpec(spec)
                .build()
        } catch (e: Exception) {
            Timber.e(e, "Error on getting encrypted shared preferences")
        }
        return null
    }

}