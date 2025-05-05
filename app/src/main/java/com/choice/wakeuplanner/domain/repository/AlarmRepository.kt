package com.choice.wakeuplanner.domain.repository

import com.choice.wakeuplanner.core.IResult
import com.choice.wakeuplanner.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun getAlarms(): Flow<IResult<List<Alarm>>>
    suspend fun insertAlarm(alarm: Alarm): Flow<IResult<Unit>>
    suspend fun getAlarmById(alarmId: Long): Flow<IResult<Alarm>>
    suspend fun deleteAlarm(alarmId: Long): Flow<IResult<Unit>>
    suspend fun updateActiveAlarm(alarmId: Long, isActive: Boolean): Flow<IResult<Unit>>
    suspend fun update(alarm: Alarm): Flow<IResult<Unit>>
    suspend fun deleteAllAlarms(): Flow<IResult<Unit>>
    fun rescheduleAlarms()


}