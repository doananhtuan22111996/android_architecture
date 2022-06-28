package vn.geekup.app.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import vn.geekup.app.data.Config.DataStore.DATA_STORE_ALIAS
import java.io.IOException

class PreferenceDataStore(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun getInt(name: Preferences.Key<Int>) = mapExt(name, Int.empty()).first()

    suspend fun setInt(name: Preferences.Key<Int>, value: Int) {
        dataStore.edit {
            it[name] = value
        }
    }

    suspend fun getString(name: Preferences.Key<String>) = mapExt(name, String.empty()).first()

    suspend fun setString(name: Preferences.Key<String>, value: String) {
        dataStore.edit {
            it[name] = value
        }
    }

    suspend fun getBoolean(name: Preferences.Key<Boolean>) = mapExt(name, Boolean.default()).first()

    suspend fun setBoolean(name: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit {
            it[name] = value
        }
    }

    suspend fun <T> getObject(name: Preferences.Key<String>, clazz: Class<T>): T? = Gson().fromJson(
        mapExt(name, String.empty()).first(),
        clazz
    )

    suspend fun <T> setObject(name: Preferences.Key<String>, obj: T) {
        dataStore.edit {
            it[name] = Gson().toJson(obj)
        }
    }

    fun hasKey(key: Preferences.Key<*>) = dataStore.data.map { it.contains(key) }

    suspend fun clearDataStore() {
        dataStore.edit {
            it.clear()
        }
    }

    private inline fun <reified T> mapExt(name: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data.catch { e ->
            Timber.e("Error reading preferences: ${e.message}")
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map { preferences ->
            preferences[name] ?: defaultValue
        }
    }

    private fun Int.Companion.empty(): Int = 0
    private fun String.Companion.empty(): String = ""
    private fun Boolean.Companion.default(): Boolean = false

}
