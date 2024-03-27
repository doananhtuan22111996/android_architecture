package vn.geekup.data.dao.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

open class BaseTypeConverter<T> {

    @TypeConverter
    fun fromTypeToString(list: List<T>): String {
        return Gson().toJson(list) ?: String.Companion.empty()
    }

    @TypeConverter
    fun fromStringToType(str: String): T? {
        val listType: Type = object : TypeToken<List<T>?>() {}.type
        return Gson().fromJson(str, listType)
    }

    private fun String.Companion.empty() = ""

}