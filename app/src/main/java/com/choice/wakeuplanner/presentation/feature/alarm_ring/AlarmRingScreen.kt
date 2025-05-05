package com.choice.wakeuplanner.presentation.feature.alarm_ring

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.choice.wakeuplanner.R
import com.choice.wakeuplanner.presentation.theme.ApplicationTheme
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AlarmRingScreen(
    label: String,
    onDismiss: () -> Unit,
    onSnooze: () -> Unit
) {
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000)
            currentTime = LocalTime.now()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationTheme.colors.background)
    ) {
        Text(
            text = currentTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
            style = ApplicationTheme.typography.displayLarge,
            color = ApplicationTheme.colors.onSurface,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(vertical = ApplicationTheme.spacing.extraLarge)
        )


        Column(
            modifier = Modifier
                .padding(ApplicationTheme.spacing.medium)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                textAlign = TextAlign.Center,
                style = ApplicationTheme.typography.displayLarge,
                color = ApplicationTheme.colors.onSurface,
            )
            Spacer(modifier = Modifier.size(ApplicationTheme.spacing.medium))
            Button(
                onClick = onSnooze,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ApplicationTheme.colors.primary
                ),
            ) {
                Icon(
                    painter = painterResource(R.drawable.rounded_snooze_24),
                    contentDescription = "Dismiss",
                    tint = ApplicationTheme.colors.background
                )
                Spacer(modifier = Modifier.width(ApplicationTheme.spacing.small))
                Text(
                    text = "Sleep (5 min)",
                    style = ApplicationTheme.typography.labelLarge,
                    color = ApplicationTheme.colors.background
                )
            }
        }

        Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(
                containerColor = ApplicationTheme.colors.onError
            ),
            modifier = Modifier
                .padding(bottom = ApplicationTheme.spacing.large)
                .align(Alignment.BottomCenter)
        ) {
            Icon(
                painter = painterResource(R.drawable.rounded_stop_circle_24),
                contentDescription = "Dismiss",
                tint = ApplicationTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.width(ApplicationTheme.spacing.small))
            Text(
                text = "Stop",
                style = ApplicationTheme.typography.labelLarge,
                color = ApplicationTheme.colors.onSurface
            )
        }

    }
}