package com.choice.wakeuplanner.domain.model

import java.time.DayOfWeek

data class Alarm(
    val id: Long? = null,
    val hour: Int,
    val minute: Int,
    val isAm: Boolean,
    val dayOfWeeks: List<DayOfWeek>,
    val label: String,
    val soundUri: String?,
    val soundTitle: String,
    val snoozeEnabled: Boolean,
    val isActive: Boolean
){


    private val minuteFormatted = if (minute < 10) "0$minute" else minute.toString()
    val timeFormatted = "$hour:$minuteFormatted"
    val getAm = if (isAm) "AM" else "PM"
    val repeatDaysFormatted = RepeatDay.formatDays(dayOfWeeks)

}
