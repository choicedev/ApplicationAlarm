package com.choice.wakeuplanner.presentation.feature.home.model

sealed class HomeUiEvent {
    data class DeleteAlarm(val alarmId: Long) : HomeUiEvent()
    data object DeleteAllAlarms : HomeUiEvent()
    data class UpdateAlarm(
        val alarmId: Long,
        val isActive: Boolean
    ) : HomeUiEvent()
}