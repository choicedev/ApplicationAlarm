package com.choice.wakeuplanner.presentation.feature.alarm_ring

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PowerManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.choice.wakeuplanner.domain.kt.AlarmScheduler
import com.choice.wakeuplanner.presentation.theme.ApplicationAlarmTheme
import com.choice.wakeuplanner.service.AlarmService
import com.choice.wakeuplanner.service.AlarmService.Companion.ACTION_SNOOZE
import com.choice.wakeuplanner.service.AlarmService.Companion.ACTION_STOP_ALARM
import com.choice.wakeuplanner.service.AlarmService.Companion.ALARM_ID
import com.choice.wakeuplanner.service.AlarmService.Companion.ALARM_LABEL
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmRingActivity : ComponentActivity() {
    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    private val alarmId by lazy { intent.getLongExtra(ALARM_ID, -1L) }
    private val alarmLabel by lazy { intent.getStringExtra(ALARM_LABEL) }
    private val stopAlarmReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            setShowWhenLocked(false)
            setTurnScreenOn(false)
            finishAffinity()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTurnScreenOn(true)
        setShowWhenLocked(true)

        registerBroadcast()

        setContent {
            ApplicationAlarmTheme {
                val context = LocalContext.current
                AlarmRingScreen(
                    label = alarmLabel ?: "Alarm",
                    onDismiss = {
                        Intent(context, AlarmService::class.java).apply {
                            putExtra(ALARM_ID, alarmId)
                            this.action = AlarmService.ACTION_DISMISS
                            context.startService(this)
                        }
                        alarmScheduler.cancelSnooze(alarmId)
                        finishAffinity()
                    },
                    onSnooze = {
                        Intent(context, AlarmService::class.java).apply {
                            putExtra(ALARM_ID, alarmId)
                            this.action = ACTION_SNOOZE
                            context.startService(this)
                        }
                        finishAffinity()
                    }
                )
            }
        }
    }

    private fun registerBroadcast() {
        val filter = IntentFilter(ACTION_STOP_ALARM)
        LocalBroadcastManager.getInstance(this).registerReceiver(stopAlarmReceiver, filter)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(stopAlarmReceiver)
        super.onDestroy()
    }

}