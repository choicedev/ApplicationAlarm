package com.choice.wakeuplanner.presentation.feature.alarm.model

import android.net.Uri
import com.choice.wakeuplanner.domain.model.RepeatDay
import java.time.DayOfWeek

data class AlarmUiState(
    val isLoading: Boolean = true,
    val alarmId: Long? = null,
    val timeHour: Int = -1,
    val timeMinute: Int = -1,
    val isAm: Boolean = true,
    val repeatDays: List<RepeatDay> = emptyList(),
    val label: String? = null,
    val soundUri: Uri? = null,
    val soundTitle: String = "Default",
    val snoozeEnabled: Boolean = true,
    val isActive: Boolean = true,
){


    val getAm = if (isAm) "AM" else "PM"

}