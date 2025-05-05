package com.choice.wakeuplanner.presentation.feature.alarm_ring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choice.wakeuplanner.domain.kt.AlarmScheduler
import com.choice.wakeuplanner.domain.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmRingViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    fun dismissAlarm(alarmId: Long) {
        viewModelScope.launch {
            alarmRepository.getAlarmById(alarmId)
        }
    }

    fun snoozeAlarm(alarmId: Long) {

    }
}