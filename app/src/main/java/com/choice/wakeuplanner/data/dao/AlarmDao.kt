package com.choice.wakeuplanner.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.choice.wakeuplanner.core.base.BaseDao
import com.choice.wakeuplanner.data.entity.AlarmEntity
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek

@Dao
interface AlarmDao : BaseDao<AlarmEntity> {

    @Query("SELECT * FROM alarms ORDER BY hour, minute")
    fun getAlarms(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM alarms WHERE isActive = 1 ORDER BY hour, minute")
    fun getAlarmsActive(): List<AlarmEntity>

    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    suspend fun getAlarmById(alarmId: Long): AlarmEntity?

    @Query("UPDATE alarms SET label = :label WHERE id = :alarmId")
    suspend fun updateAlarmLabel(alarmId: Long, label: String)

    @Query("UPDATE alarms SET soundUri = :soundUri, soundTitle = :soundTitle WHERE id = :alarmId")
    suspend fun updateAlarmSound(alarmId: Long, soundUri: String?, soundTitle: String)

    @Query("UPDATE alarms SET snoozeEnabled = :snoozeEnabled WHERE id = :alarmId")
    suspend fun updateAlarmSnooze(alarmId: Long, snoozeEnabled: Boolean)

    @Query("UPDATE alarms SET repeatDays = :repeatDays WHERE id = :alarmId")
    suspend fun updateAlarmRepeat(alarmId: Long, repeatDays: List<DayOfWeek>)

    @Query("UPDATE alarms SET isActive = :isActive WHERE id = :alarmId")
    suspend fun updateActiveAlarm(alarmId: Long, isActive: Boolean)

    @Query("DELETE FROM alarms WHERE id = :alarmId")
    suspend fun deleteAlarm(alarmId: Long)

    @Query("DELETE FROM alarms")
    suspend fun deleteAllAlarms()
}