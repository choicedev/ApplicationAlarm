package com.choice.wakeuplanner.core

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromDayOfWeekList(days: List<DayOfWeek>): String =
        gson.toJson(days.map { it.ordinal })

    @TypeConverter
    fun toDayOfWeekList(data: String): List<DayOfWeek> {
        val listType = object : TypeToken<List<Int>>() {}.type
        val ordinals: List<Int> = gson.fromJson(data, listType)
        return ordinals.map { DayOfWeek.of(it + 1) }
    }
}