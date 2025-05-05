package com.choice.wakeuplanner.presentation.feature.home.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.choice.wakeuplanner.core.navigation.Destination
import com.choice.wakeuplanner.presentation.feature.home.HomeScreen
import com.choice.wakeuplanner.presentation.theme.composable.appComposable

fun NavGraphBuilder.homeComposable(
    modifier: Modifier,
    navController: NavHostController,
) {
    this.appComposable(
        destination = Destination.Home
    ) {
        HomeScreen(modifier, navController)
    }
}