package com.choice.wakeuplanner.presentation.feature.alarm.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.choice.wakeuplanner.core.navigation.Destination
import com.choice.wakeuplanner.presentation.feature.alarm.AlarmScreen
import com.choice.wakeuplanner.presentation.theme.composable.appComposable

fun NavGraphBuilder.editAlarmComposable(
    modifier: Modifier,
    navController: NavHostController,
) {
    this.appComposable(
        destination = Destination.EditAlarm
    ) {
        AlarmScreen(
            modifier = modifier,
            onBack = {
                navController.popBackStack()
            },
        )
    }
}