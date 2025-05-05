package com.choice.wakeuplanner.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper
import com.choice.wakeuplanner.domain.kt.AlarmScheduler
import com.choice.wakeuplanner.service.AlarmService
import com.choice.wakeuplanner.service.AlarmService.Companion.ALARM_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {
        var action = intent.action
        val alarmId = intent.getLongExtra(ALARM_ID, -1L)
        if (alarmId == -1L) return

        when (action) {
            AlarmService.ACTION_DISMISS -> {
                Intent(context, AlarmService::class.java).apply {
                    putExtra(ALARM_ID, alarmId)
                    this.action = action
                    context.startService(this)
                }
                alarmScheduler.cancelSnooze(alarmId)
            }
            AlarmService.ACTION_SNOOZE -> {
                Intent(context, AlarmService::class.java).apply {
                    putExtra(ALARM_ID, alarmId)
                    this.action = action
                    context.startService(this)
                }
            }
        }
    }
}