package com.choice.wakeuplanner.data.repository

import com.choice.wakeuplanner.core.IResult
import com.choice.wakeuplanner.core.IResult.Companion.failed
import com.choice.wakeuplanner.core.IResult.Companion.loading
import com.choice.wakeuplanner.core.IResult.Companion.success
import com.choice.wakeuplanner.data.dao.AlarmDao
import com.choice.wakeuplanner.data.mapping.toDomain
import com.choice.wakeuplanner.data.mapping.toEntity
import com.choice.wakeuplanner.domain.kt.AlarmScheduler
import com.choice.wakeuplanner.domain.model.Alarm
import com.choice.wakeuplanner.domain.repository.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AlarmRepositoryImpl @Inject constructor(
    private val dao: AlarmDao,
    private val alarmScheduler: AlarmScheduler
) : AlarmRepository {
    override suspend fun getAlarms(): Flow<IResult<List<Alarm>>> {
        return flow {
            dao.getAlarms()
                .map { alarms -> alarms.map { it.toDomain() } }
                .catch { emit(failed(it)) }
                .collect { emit(success(it)) }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insertAlarm(alarm: Alarm): Flow<IResult<Unit>> {
        return flow {
            runCatching {
                val newId = dao.insert(alarm.toEntity())
                newId
            }.onSuccess { newId ->
                val alarmWithId = dao.getAlarmById(newId)?.toDomain() ?: return@flow
                alarmScheduler.schedule(alarmWithId)
                emit(success(Unit))
            }.onFailure {
                emit(failed(it))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAlarmById(alarmId: Long): Flow<IResult<Alarm>> {
        return flow {
            runCatching {
                dao.getAlarmById(alarmId)?.toDomain()
            }.onSuccess {
                if (it != null) {
                    emit(success(it))
                } else {
                    emit(failed(Exception("Alarm not found")))
                }
            }.onFailure {
                emit(failed(it))
            }
        }.flowOn(Dispatchers.IO)
    }


    override suspend fun deleteAlarm(alarmId: Long): Flow<IResult<Unit>> {
        return flow {
            val alarm = dao.getAlarmById(alarmId)?.toDomain() ?: return@flow
            runCatching {
                dao.delete(alarm.toEntity())
            }.onSuccess {
                alarmScheduler.cancel(alarm)
                emit(success(Unit))
            }.onFailure {
                emit(failed(it))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateActiveAlarm(
        alarmId: Long,
        isActive: Boolean
    ): Flow<IResult<Unit>> {
        return flow {
            val alarm = dao.getAlarmById(alarmId)?.toDomain() ?: return@flow
            runCatching {
                dao.update(alarm.copy(isActive = isActive).toEntity())
            }.onSuccess {
                alarmScheduler.cancel(alarm)
                if (isActive) alarmScheduler.schedule(alarm)

                emit(success(Unit))
            }.onFailure {
                emit(failed(it))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun update(alarm: Alarm): Flow<IResult<Unit>> {
        return flow {
            val updateSchedule = dao.getAlarmById(alarm.id!!)?.toDomain() ?: return@flow

            runCatching {
                dao.update(alarm.toEntity())
            }.onSuccess {
                alarmScheduler.cancel(updateSchedule)
                if (alarm.isActive) alarmScheduler.schedule(alarm)
                emit(success(Unit))
            }.onFailure {
                emit(failed(it))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteAllAlarms(): Flow<IResult<Unit>> {
        return flow {
            emit(loading())
            runCatching {
                dao.deleteAllAlarms()
            }.onSuccess {
                emit(success(Unit))
            }.onFailure {
                emit(failed(it))
            }
        }
    }

    override fun rescheduleAlarms() {
        dao.getAlarmsActive().map { it.toDomain() }.let { alarms ->
            alarms.map { alarm ->
                alarmScheduler.schedule(alarm)
            }
        }
    }
}