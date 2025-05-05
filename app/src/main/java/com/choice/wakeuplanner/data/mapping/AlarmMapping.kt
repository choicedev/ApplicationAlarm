package com.choice.wakeuplanner.data.mapping

import com.choice.wakeuplanner.data.entity.AlarmEntity
import com.choice.wakeuplanner.domain.model.Alarm

fun AlarmEntity.toDomain() = Alarm(
    id = this.id,
    hour = this.hour,
    minute = this.minute,
    isAm = this.isAm,
    dayOfWeeks = this.repeatDays,
    label = this.label,
    soundUri = this.soundUri,
    soundTitle = this.soundTitle,
    snoozeEnabled = this.snoozeEnabled,
    isActive = this.isActive
)

fun Alarm.toEntity() = AlarmEntity(
    id = this.id,
    hour = this.hour,
    minute = this.minute,
    isAm = this.isAm,
    repeatDays = this.dayOfWeeks,
    label = this.label,
    soundUri = this.soundUri,
    soundTitle = this.soundTitle,
    snoozeEnabled = this.snoozeEnabled,
    isActive = this.isActive
)