package com.choice.wakeuplanner.core.helper

import android.content.Context
import android.os.PowerManager

object ScreenStateHelper {
    fun isScreenOff(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return !powerManager.isInteractive
    }
}