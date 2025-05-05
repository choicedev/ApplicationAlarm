package com.choice.wakeuplanner.core.helper

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

fun vibrateDevice(milliSeconds: Long, context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val vibrationEffect = VibrationEffect.createOneShot(milliSeconds, VibrationEffect.DEFAULT_AMPLITUDE)
    vibrator.vibrate(vibrationEffect)
}