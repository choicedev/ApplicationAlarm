package com.choice.wakeuplanner.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.choice.wakeuplanner.BuildConfig
import com.choice.wakeuplanner.R
import com.choice.wakeuplanner.core.IResult
import com.choice.wakeuplanner.core.IResult.Companion.watchStatus
import com.choice.wakeuplanner.core.helper.vibrateDevice
import com.choice.wakeuplanner.domain.kt.AlarmScheduler
import com.choice.wakeuplanner.domain.model.Alarm
import com.choice.wakeuplanner.domain.repository.AlarmRepository
import com.choice.wakeuplanner.receiver.AlarmActionReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService : LifecycleService() {
    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    private var mediaPlayer: MediaPlayer? = null
    private var currentAlarm: Alarm? = null
    private val notificationId = 101
    private val channelId = "alarm_channel"
    private var countDownTimer: CountDownTimer? = null

    companion object {
        const val ACTION_DISMISS = "action_dismiss"
        const val ACTION_SNOOZE = "action_snooze"
        const val ALARM_ID = "alarm_id"
        const val ALARM_LABEL = "alarm_label"
        const val ACTION_STOP_ALARM = "STOP_ALARM_ACTION"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    @SuppressLint("ForegroundServiceType", "UnspecifiedRegisterReceiverFlag")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val alarmId = intent?.getLongExtra(ALARM_ID, -1L) ?: -1L
        val action = intent?.action

        if (alarmId == -1L) {
            stopSelf()
            return START_NOT_STICKY
        }

        when (action) {
            ACTION_DISMISS -> {
                stopAlarm()
            }

            ACTION_SNOOZE -> {
                handleSnooze(alarmId)
            }

            else -> {
                handleAlarm(alarmId)
            }
        }
        return START_NOT_STICKY
    }

    private fun handleAlarm(alarmId: Long) {
        lifecycleScope.launch {
            alarmRepository.getAlarmById(alarmId).collect { result ->
                when (result) {
                    is IResult.Success -> {
                        startForeground(notificationId, createNotification().build())
                        currentAlarm = result.data
                        playAlarmSound(result.data)
                        handlePostAlarmActions(result.data)
                        updateNotification(result.data)
                        startAutoStopCountdown()
                    }

                    else -> stopSelf()
                }
            }
        }
    }

    private fun updateNotification(alarm: Alarm) {
        val notification = createNotification(alarm)
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .notify(notificationId, notification.build())
    }

    private fun createNotification(alarm: Alarm? = null): NotificationCompat.Builder {
        val dismissIntent = Intent(this, AlarmActionReceiver::class.java).apply {
            action = ACTION_DISMISS
            putExtra(ALARM_ID, currentAlarm?.id ?: -1L)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val snoozeIntent = Intent(this, AlarmActionReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(ALARM_ID, currentAlarm?.id ?: -1L)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(BuildConfig.APP_NAME)
            .setContentText(alarm?.label)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .apply {
                addAction(R.drawable.rounded_stop_circle_24, "Stop", dismissPendingIntent)
                if (alarm?.snoozeEnabled == true) {
                    addAction(R.drawable.rounded_snooze_24, "Sleep (5min)", snoozePendingIntent)
                }
            }
    }

    private fun handleSnooze(alarmId: Long) {
        lifecycleScope.launch {
            alarmRepository.getAlarmById(alarmId).collect { result ->
                when (result) {
                    is IResult.Success -> {
                        if (result.data.snoozeEnabled) {
                            alarmScheduler.scheduleSnooze(result.data)
                        }
                        stopAlarm()
                    }

                    else -> stopAlarm()
                }
            }
        }
    }

    private fun stopAlarm() {
        mediaPlayer?.release()
        mediaPlayer = null
        countDownTimer?.cancel()
        countDownTimer = null
        val intent = Intent(ACTION_STOP_ALARM)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun playAlarmSound(alarm: Alarm) {
        val soundUri = alarm.soundUri?.toUri()
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        mediaPlayer = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_ALARM)
            setOnPreparedListener { it.start() }
            try {
                setDataSource(applicationContext, soundUri)
                prepareAsync()
            } catch (e: Exception) {
                setDataSource(applicationContext, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                prepareAsync()
                Log.e("AlarmService", "Failed to play alarm sound", e)
            }
        }
    }

    private suspend fun handlePostAlarmActions(alarm: Alarm) {
        if (alarm.dayOfWeeks.isEmpty()) {
            alarmRepository.update(alarm.copy(isActive = false))
                .collect {
                    it.watchStatus(
                        onSuccess = {},
                        onFailed = {}
                    )
                }
        }else{
            alarmScheduler.schedule(alarm)
        }
    }

    private fun startAutoStopCountdown() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(2 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                vibrateDevice(10, applicationContext)
            }

            override fun onFinish() {
                stopAlarm()
            }
        }.start()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            "Alarms",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Channel alarms" }

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(channel)
    }

    override fun onDestroy() {
        stopAlarm()
        super.onDestroy()
    }
}