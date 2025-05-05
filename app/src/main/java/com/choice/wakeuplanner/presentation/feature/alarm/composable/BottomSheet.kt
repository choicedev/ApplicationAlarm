package com.choice.wakeuplanner.presentation.feature.alarm.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
fun RepeatBottomSheet(
    selectedDays: List<RepeatDay>,
    onDayToggled: (RepeatDay) -> Unit,
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
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Select Days",
                    color = ApplicationTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    style = ApplicationTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                )

                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .padding(end = ApplicationTheme.spacing.medium)
                        .align(Alignment.CenterEnd)
                ) {
                    Text(
                        text = "Close",
                        color = ApplicationTheme.colors.primary,
                        style = ApplicationTheme.typography.bodyMedium
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = ApplicationTheme.spacing.medium),
                color = ApplicationTheme.colors.onBackground.copy(alpha = 0.1f)
            )

            LazyColumn {
                items(items = repeatDays, key = { it.dayOfWeek }){ item ->
                    DaysItem(onDayToggled, item, selectedDays.any { day -> day.dayOfWeek == item.dayOfWeek })
                }
            }
        }
    }
}

@Composable
private fun DaysItem(
    onDayToggled: (RepeatDay) -> Unit,
    day: RepeatDay,
    isSelected: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDayToggled(day) }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = day.label,
            color = ApplicationTheme.colors.onSurface,
            fontSize = 16.sp
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFFFDF733)
            )
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

