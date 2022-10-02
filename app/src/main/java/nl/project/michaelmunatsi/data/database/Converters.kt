package nl.project.michaelmunatsi.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.project.michaelmunatsi.model.Category

class Converters {
    @TypeConverter
    fun fromCategoryListToString(category: List<Category>): String {
        return Json.encodeToString(category)
    }

    @TypeConverter
    fun fromStringToCategory(value: String): List<Category> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromStringListToString(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToStringList(value: String) : List<String>{
        return Json.decodeFromString(value)
    }
}
