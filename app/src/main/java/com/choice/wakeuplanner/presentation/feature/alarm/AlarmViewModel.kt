package com.choice.wakeuplanner.presentation.feature.alarm

import android.media.RingtoneManager
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.choice.wakeuplanner.core.IResult.Companion.watchStatus
import com.choice.wakeuplanner.core.base.BaseViewModel
import com.choice.wakeuplanner.domain.model.Alarm
import com.choice.wakeuplanner.domain.model.RepeatDay
import com.choice.wakeuplanner.domain.usecase.AlarmUseCase
import com.choice.wakeuplanner.presentation.feature.alarm.model.AlarmUiEvent
import com.choice.wakeuplanner.presentation.feature.alarm.model.AlarmUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmUseCase: AlarmUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<AlarmUiState, AlarmUiEvent>(AlarmUiState()) {


    init {
        savedStateHandle.get<String>("id")?.toLong()?.let {
            if (it != -1L) {
                viewModelScope.launch {
                    getAlarm(it)
                }
            }else{
                val timeHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val timeMinute = Calendar.getInstance().get(Calendar.MINUTE)
                val isAm = Calendar.getInstance().get(Calendar.AM_PM) == Calendar.AM

                _state.update {
                    it.copy(
                        timeHour = timeHour,
                        timeMinute = timeMinute,
                        isAm = isAm,
                        isLoading = false
                    )
                }
            }
        }
    }

    override fun onEvent(event: AlarmUiEvent) {
        viewModelScope.launch {
            when (event) {
                is AlarmUiEvent.LabelChanged -> {
                    _state.update {
                        it.copy(
                            label = event.label
                        )
                    }
                }

                is AlarmUiEvent.PickSound -> {
                    _state.update {
                        it.copy(
                            soundUri = event.uri.toUri(),
                            soundTitle = event.name
                        )
                    }
                }

                is AlarmUiEvent.RepeatDayToggled -> {
                    _state.update {
                        it.copy(
                            repeatDays = it.repeatDays.toMutableList().apply {
                                firstOrNull { first ->
                                    first.dayOfWeek == event.repeatDay.dayOfWeek
                                }.let { day ->
                                    if(day == null){
                                        add(event.repeatDay)
                                    }else{
                                        remove(day)
                                    }
                                }
                            }
                        )
                    }
                }

                is AlarmUiEvent.SaveAlarm -> {
                    if(_state.value.alarmId == null){
                        onInsertAlarm()
                    }else{
                        onUpdateAlarm()
                    }
                }

                is AlarmUiEvent.DeleteAlarm -> {
                    onDeleteAlarm()
                }

                is AlarmUiEvent.SnoozeToggled -> {
                    _state.update {
                        it.copy(
                            snoozeEnabled = !it.snoozeEnabled
                        )
                    }
                }

                is AlarmUiEvent.TimeChanged -> {
                    _state.update {
                        it.copy(
                            timeHour = event.hour,
                            timeMinute = event.minute,
                            isAm = event.isAm,
                        )
                    }
                }
            }
        }
    }

    private suspend fun getAlarm(id: Long) {
        alarmUseCase.getAlarmUseCase(id).collect {
            it.watchStatus(
                onSuccess = { alarm ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            alarmId = alarm.id,
                            label = alarm.label,
                            timeHour = alarm.hour,
                            timeMinute = alarm.minute,
                            isAm = alarm.isAm,
                            repeatDays = alarm.dayOfWeeks.map { RepeatDay.fromDayOfWeek(it) },
                            soundUri = alarm.soundUri?.toUri(),
                            soundTitle = alarm.soundTitle,
                            snoozeEnabled = alarm.snoozeEnabled,
                        )
                    }
                },
                onLoading = {
                }
            )
        }
    }

    private suspend fun onDeleteAlarm() {
        val state = _state.value
        alarmUseCase.deleteAlarmUseCase(state.alarmId!!).collect {
            it.watchStatus(
                onSuccess = {},
                onFailed = {}
            )
        }
    }

    private suspend fun onUpdateAlarm(){
        val state = _state.value
        alarmUseCase.updateAlarmUseCase(
            Alarm(
                id = state.alarmId,
                hour = state.timeHour,
                minute = state.timeMinute,
                isAm = state.isAm,
                dayOfWeeks = state.repeatDays.map { it.dayOfWeek },
                label = state.label!!,
                soundUri = state.soundUri!!.toString(),
                soundTitle = state.soundTitle,
                snoozeEnabled = state.snoozeEnabled,
                isActive = true
            )
        ).collect {
            it.watchStatus(
                onSuccess = {},
                onFailed = {}
            )
        }
    }

    private suspend fun onInsertAlarm() {
        val state = _state.value
        alarmUseCase.saveAlarmUseCase(
            Alarm(
                id = state.alarmId,
                hour = state.timeHour,
                minute = state.timeMinute,
                isAm = state.isAm,
                dayOfWeeks = state.repeatDays.map { it.dayOfWeek },
                label = state.label ?: "Alarm",
                soundUri = (state.soundUri ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)).toString(),
                soundTitle = state.soundTitle,
                snoozeEnabled = state.snoozeEnabled,
                isActive = state.isActive
            )
        ).collect {
            it.watchStatus(
                onSuccess = {},
                onFailed = {}
            )
        }
    }
}