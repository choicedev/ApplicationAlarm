package com.choice.wakeuplanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.choice.wakeuplanner.core.Converters
import com.choice.wakeuplanner.data.dao.AlarmDao
import com.choice.wakeuplanner.data.entity.AlarmEntity

@Database(
    entities = [AlarmEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}
