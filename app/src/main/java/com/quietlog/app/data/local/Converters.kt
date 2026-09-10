package com.quietlog.app.data.local

import androidx.room.TypeConverter
import org.json.JSONArray

class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val array = JSONArray()
        value.forEach { array.put(it) }
        return array.toString()
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        val array = JSONArray(value)
        return List(array.length()) { index -> array.getString(index) }
    }
}
