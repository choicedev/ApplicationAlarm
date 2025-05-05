package com.choice.wakeuplanner.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.choice.wakeuplanner.presentation.ui.MainNavigation
import com.choice.wakeuplanner.presentation.theme.ApplicationAlarmTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApplicationAlarmTheme {
                MainNavigation()
            }
        }
    }
}