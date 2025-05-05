package com.choice.wakeuplanner.presentation.feature.home

import androidx.lifecycle.viewModelScope
import com.choice.wakeuplanner.core.IResult.Companion.watchStatus
import com.choice.wakeuplanner.core.base.BaseViewModel
import com.choice.wakeuplanner.domain.usecase.AlarmUseCase
import com.choice.wakeuplanner.domain.usecase.UpdateActiveAlarmUseCase
import com.choice.wakeuplanner.presentation.feature.home.model.HomeUiEvent
import com.choice.wakeuplanner.presentation.feature.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val alarmUseCase: AlarmUseCase
) : BaseViewModel<HomeUiState, HomeUiEvent>(HomeUiState()) {

    init {
        viewModelScope.launch {
            getAllAlarms()
        }
    }

    override fun onEvent(event: HomeUiEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeUiEvent.DeleteAlarm -> {
                    onDeleteAlarm(event.alarmId)
                }

                is HomeUiEvent.UpdateAlarm -> {
                    onUpdateActiveAlarm(event.alarmId, event.isActive)
                }

                is HomeUiEvent.DeleteAllAlarms -> {
                    onDeleteAllAlarms()
                }
            }
        }
    }

    private suspend fun onDeleteAllAlarms(){
        alarmUseCase.deleteAllAlarms(Unit).collect {
            it.watchStatus(
                onSuccess = {
                    _state.update { state ->
                        state.copy(isLoading = false)
                    }
                },
                onLoading = {
                    _state.update { state ->
                        state.copy(isLoading = true)
                    }
                },
                onFailed = {}
            )
        }
    }

    private suspend fun onDeleteAlarm(alarmId: Long) {
        alarmUseCase.deleteAlarmUseCase(alarmId).collect {
            it.watchStatus(
                onSuccess = {},
                onFailed = {}
            )
        }
    }

    private suspend fun onUpdateActiveAlarm(alarmId: Long, isActive: Boolean) {
        alarmUseCase.updateActiveAlarmUseCase(
            UpdateActiveAlarmUseCase.Params(
                alarmId = alarmId,
                isActive = isActive
            )
        ).collect {
            it.watchStatus(
                onSuccess = {},
                onFailed = {}
            )
        }
    }

    private suspend fun getAllAlarms() {
        alarmUseCase.listAlarmUseCase(Unit)
            .onEach { result ->
                result.map { alarms ->
                    _state.update {
                        it.copy(
                            listAlarms = alarms,
                            isLoading = false
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }
}