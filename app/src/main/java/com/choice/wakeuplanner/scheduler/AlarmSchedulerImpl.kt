package com.choice.wakeuplanner.scheduler

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.choice.wakeuplanner.domain.kt.AlarmScheduler
import com.choice.wakeuplanner.domain.model.Alarm
import com.choice.wakeuplanner.receiver.AlarmReceiver
import com.choice.wakeuplanner.service.AlarmService.Companion.ALARM_ID
import com.choice.wakeuplanner.service.AlarmService.Companion.ALARM_LABEL
import java.time.DayOfWeek
import java.util.Calendar
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    private val context: Context
) : AlarmScheduler {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    override fun schedule(alarm: Alarm) {
        require(alarm.id != null) { "Alarm ID não pode ser nulo" }

        if (alarm.dayOfWeeks.isNotEmpty()) {
            scheduleRepeatingAlarms(alarm)
        } else {
            scheduleOneTimeAlarm(alarm)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleRepeatingAlarms(alarm: Alarm) {
        alarm.dayOfWeeks.forEach { day ->
            val calendar = createAlarmCalendar(alarm, day)
            val pendingIntent = createPendingIntent(alarm, day.value)

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleOneTimeAlarm(alarm: Alarm) {
        val calendar = createAlarmCalendar(alarm)
        val pendingIntent = createPendingIntent(alarm)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    @SuppressLint("ScheduleExactAlarm")
    override fun scheduleSnooze(alarm: Alarm) {
        require(alarm.id != null) { "Alarm ID não pode ser nulo" }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis() + 5 * 60 * 1000 // 5 min
        }

        val snoozeAlarm = alarm.copy(
            hour = calendar.get(Calendar.HOUR_OF_DAY),
            minute = calendar.get(Calendar.MINUTE),
            dayOfWeeks = emptyList()
        )

        val pendingIntent = createPendingIntent(snoozeAlarm)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    override fun cancel(alarm: Alarm) {
        if (alarm.dayOfWeeks.isNotEmpty()) {
            alarm.dayOfWeeks.forEach { day ->
                cancelPendingIntent(alarm.id!!, day.value)
            }
        } else {
            cancelPendingIntent(alarm.id!!)
        }
        cancelSnooze(alarm.id!!)
    }

    override fun cancelSnooze(alarmId: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = generateSnoozeRequestCode(alarmId)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }


    private fun createAlarmCalendar(alarm: Alarm, day: DayOfWeek? = null): Calendar {
        return Calendar.getInstance().apply {
            day?.let { set(Calendar.DAY_OF_WEEK, convertDayOfWeek(day)) }
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            set(Calendar.AM_PM, if (alarm.isAm) Calendar.AM else Calendar.PM)

            if (before(Calendar.getInstance())) {
                if (day == null) {
                    add(Calendar.DAY_OF_YEAR, 1)
                } else {
                    add(Calendar.DAY_OF_YEAR, 7)
                }
            }
        }
    }

    private fun convertDayOfWeek(day: DayOfWeek): Int {
        return when (day) {
            DayOfWeek.SUNDAY -> Calendar.SUNDAY
            DayOfWeek.MONDAY -> Calendar.MONDAY
            DayOfWeek.TUESDAY -> Calendar.TUESDAY
            DayOfWeek.WEDNESDAY -> Calendar.WEDNESDAY
            DayOfWeek.THURSDAY -> Calendar.THURSDAY
            DayOfWeek.FRIDAY -> Calendar.FRIDAY
            DayOfWeek.SATURDAY -> Calendar.SATURDAY
        }
    }

    private fun createPendingIntent(alarm: Alarm, dayCode: Int? = null): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(ALARM_ID, alarm.id)
            putExtra(ALARM_LABEL, alarm.label)
        }
        return PendingIntent.getBroadcast(
            context,
            generateRequestCode(alarm.id!!, dayCode),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun cancelPendingIntent(alarmId: Long, dayCode: Int? = null) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            generateRequestCode(alarmId, dayCode),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }

    private fun generateRequestCode(alarmId: Long, dayCode: Int? = null): Int {
        return (alarmId.toInt() shl 16) or (dayCode ?: 0)
    }


    private fun generateSnoozeRequestCode(alarmId: Long): Int {
        return (alarmId.toInt() shl 16) or 0xFFFF
    }
}