package com.choice.wakeuplanner.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.choice.wakeuplanner.domain.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmRepository: AlarmRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            alarmRepository.rescheduleAlarms()
        }
    }
}