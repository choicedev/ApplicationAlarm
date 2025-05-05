package com.choice.wakeuplanner.presentation.feature.home.model

import com.choice.wakeuplanner.domain.model.Alarm

data class HomeUiState(
    val isLoading: Boolean = true,
    val listAlarms: List<Alarm> = emptyList()
)