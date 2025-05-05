package com.choice.wakeuplanner.domain.kt

import com.choice.wakeuplanner.domain.model.Alarm

interface AlarmScheduler {
    fun schedule(alarm: Alarm)
    fun cancel(alarm: Alarm)
    fun scheduleSnooze(alarm: Alarm)
    fun cancelSnooze(alarmId: Long)
}