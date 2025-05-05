package com.choice.wakeuplanner.presentation.feature.home.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choice.wakeuplanner.domain.model.RepeatDay
import com.choice.wakeuplanner.presentation.theme.ApplicationTheme
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAlarmsBottomSheet(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = ApplicationTheme.colors.background
    ) {
        Column {
            Text(
                text = "Delete All Alarms",
                color = ApplicationTheme.colors.onSurface,
                textAlign = TextAlign.Center,
                style = ApplicationTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = ApplicationTheme.spacing.medium),
                color = ApplicationTheme.colors.onBackground.copy(alpha = 0.1f)
            )

            Row(
                modifier = Modifier
                    .padding(ApplicationTheme.spacing.medium)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                TextButton(
                    onClick = onDismissRequest,
                ) {
                    Text(
                        text = "Cancel",
                        color = ApplicationTheme.colors.error

                    )
                }

                TextButton(
                    onClick = onConfirm,
                ) {
                    Text(
                        text = "Delete", color = ApplicationTheme.colors.primary
                    )
                }
            }
        }
    }
}

val repeatDays = listOf(
    RepeatDay(1, "Every Monday", DayOfWeek.MONDAY),
    RepeatDay(2, "Every Tuesday", DayOfWeek.TUESDAY),
    RepeatDay(3, "Every Wednesday", DayOfWeek.WEDNESDAY),
    RepeatDay(4, "Every Thursday", DayOfWeek.THURSDAY),
    RepeatDay(5, "Every Friday", DayOfWeek.FRIDAY),
    RepeatDay(6, "Every Saturday", DayOfWeek.SATURDAY),
    RepeatDay(7, "Every Sunday", DayOfWeek.SUNDAY)
)

