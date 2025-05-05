package com.choice.wakeuplanner.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.choice.wakeuplanner.core.helper.ScreenStateHelper
import com.choice.wakeuplanner.presentation.feature.alarm_ring.AlarmRingActivity
import com.choice.wakeuplanner.service.AlarmService
import com.choice.wakeuplanner.service.AlarmService.Companion.ALARM_ID
import com.choice.wakeuplanner.service.AlarmService.Companion.ALARM_LABEL


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Alarm triggered! ID: ${intent.getLongExtra(ALARM_ID, -1)}")

        val alarmId =  intent.getLongExtra(ALARM_ID, -1)
        val alarmLabel =  intent.getStringExtra(ALARM_LABEL)

        if (ScreenStateHelper.isScreenOff(context)) startAlarmActivity(context, alarmId, alarmLabel)

        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra(ALARM_ID, alarmId)
        }

        context.startForegroundService(serviceIntent)
    }

    private fun startAlarmActivity(context: Context, alarmId: Long, alarmLabel: String?) {
        val activityIntent = Intent(context, AlarmRingActivity::class.java).apply {
            putExtra(ALARM_ID, alarmId)
            putExtra(ALARM_LABEL, alarmLabel)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(activityIntent)
    }

}