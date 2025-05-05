package com.choice.wakeuplanner.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val hour: Int,
    val minute: Int,
    val isAm: Boolean,
    val repeatDays: List<DayOfWeek>,
    val label: String,
    val soundUri: String?,
    val soundTitle: String,
    val snoozeEnabled: Boolean,
    val isActive: Boolean = true
)