package com.choice.wakeuplanner.presentation.feature.alarm.model

import com.choice.wakeuplanner.domain.model.RepeatDay
import java.time.DayOfWeek

sealed class AlarmUiEvent {
    data class TimeChanged(val hour: Int, val minute: Int, val isAm: Boolean): AlarmUiEvent()
    data class RepeatDayToggled(val repeatDay: RepeatDay): AlarmUiEvent()
    data class LabelChanged(val label: String): AlarmUiEvent()
    data class PickSound(val name: String, val uri: String): AlarmUiEvent()
    object SnoozeToggled: AlarmUiEvent()
    object SaveAlarm: AlarmUiEvent()
    object DeleteAlarm: AlarmUiEvent()
}
