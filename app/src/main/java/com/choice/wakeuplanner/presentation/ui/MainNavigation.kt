package com.choice.wakeuplanner.presentation.ui

import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.choice.wakeuplanner.core.navigation.Destination
import com.choice.wakeuplanner.presentation.feature.alarm.navigation.editAlarmComposable
import com.choice.wakeuplanner.presentation.feature.home.navigation.homeComposable
import com.choice.wakeuplanner.presentation.theme.ApplicationTheme
import com.choice.wakeuplanner.presentation.theme.composable.AppNavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    var currentScreen by remember {
        mutableStateOf("")
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermission = rememberPermissionState(
            android.Manifest.permission.POST_NOTIFICATIONS
        )

        LaunchedEffect(Unit) {
            if (!notificationPermission.status.isGranted) {
                notificationPermission.launchPermissionRequest()
            }
        }
    }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        currentScreen = destination.route ?: ""
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (currentScreen.contains(Destination.Home.route)) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Destination.EditAlarm(-1))
                    },
                    containerColor = ApplicationTheme.colors.primary,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    modifier = Modifier
                        .size(ApplicationTheme.spacing.gigantic)
                ) {
                    Text("+", fontSize = 30.sp, color = ApplicationTheme.colors.background)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        AppNavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController
        ) {
            homeComposable(
                modifier = Modifier.fillMaxSize(),
                navController = navController
            )

            editAlarmComposable(
                modifier = Modifier.fillMaxSize(),
                navController = navController
            )
        }
    }
}