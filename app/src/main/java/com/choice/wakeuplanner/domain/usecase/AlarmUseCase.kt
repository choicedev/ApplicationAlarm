package com.choice.wakeuplanner.domain.usecase

import javax.inject.Inject

data class AlarmUseCase @Inject constructor (
    val listAlarmUseCase: ListAlarmUseCase,
    val getAlarmUseCase: GetAlarmUseCase,
    val saveAlarmUseCase: SaveAlarmUseCase,
    val deleteAlarmUseCase: DeleteAlarmUseCase,
    val updateActiveAlarmUseCase: UpdateActiveAlarmUseCase,
    val updateAlarmUseCase: UpdateAlarmUseCase,
    val deleteAllAlarms: DeleteAllAlarmsUseCase
)