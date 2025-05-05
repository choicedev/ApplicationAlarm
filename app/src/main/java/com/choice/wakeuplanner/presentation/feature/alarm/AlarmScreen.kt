package com.choice.wakeuplanner.presentation.feature.alarm

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.choice.wakeuplanner.domain.model.RepeatDay
import com.choice.wakeuplanner.presentation.feature.alarm.composable.RepeatBottomSheet
import com.choice.wakeuplanner.presentation.feature.alarm.model.AlarmUiEvent
import com.choice.wakeuplanner.presentation.theme.ApplicationTheme
import com.choice.wakeuplanner.presentation.feature.alarm.composable.CircularTimePicker

@Composable
fun AlarmScreen(
    modifier: Modifier,
    viewModel: AlarmViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val isLoading = state.isLoading
    var showBottomSheet by remember { mutableStateOf(false) }

    val repeatDays = RepeatDay.formatDays(state.repeatDays.map { it.dayOfWeek })

    val ringtonePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                ?.let { uri ->
                    val ringtone = RingtoneManager.getRingtone(context, uri)
                    val title = ringtone.getTitle(context)
                    viewModel.onEvent(AlarmUiEvent.PickSound(title, uri.toString()))
                }
        }
    }

    BackHandler {
        onBack()
    }

    Scaffold(
        modifier = modifier,
    ) { padding ->

        val defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val currentUri = state.soundUri ?: defaultUri
        val ringtone = RingtoneManager.getRingtone(context, currentUri)
        val soundTitle = ringtone?.getTitle(context) ?: "Default Alarm Sound"

        Column(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize()
                .background(ApplicationTheme.colors.background)
                .padding(ApplicationTheme.spacing.medium),
            horizontalAlignment = if (isLoading) Alignment.CenterHorizontally else Alignment.Start,
            verticalArrangement = if (isLoading) Arrangement.Center else Arrangement.Top
        ) {

            if (isLoading) {
                CircularProgressIndicator()
            } else {

                AlarmTopAppBar(
                    title = if(state.alarmId == null) "Add Alarm" else "Edit Alarm",
                    onCancel = onBack,
                    viewModel = viewModel,
                    onSave = onBack
                )

                Spacer(Modifier.height(ApplicationTheme.spacing.extraLarge))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ApplicationTheme.spacing.ultraEpic)
                        .background(
                            ApplicationTheme.colors.background,
                            shape = RoundedCornerShape(ApplicationTheme.spacing.mediumSmall)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    CircularTimePicker(
                        initialHour = state.timeHour,
                        initialMinute = state.timeMinute,
                        initialMeridian = state.getAm,
                        onTimeSelected = { hour, minute, isAm ->
                            viewModel.onEvent(
                                AlarmUiEvent.TimeChanged(
                                    hour, minute, isAm == "AM"
                                )
                            )
                        }
                    )

                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = ApplicationTheme.colors.onSurface.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(ApplicationTheme.spacing.mediumSmall)
                        ),
                ) {
                    AlarmOptionRow("Repeat") {
                        TextButton(onClick = { showBottomSheet = true }) {
                            Text(repeatDays, color = ApplicationTheme.colors.primary)
                        }
                    }

                    AlarmOptionRow("Label") {
                        val focusManager = LocalFocusManager.current
                        val density = LocalDensity.current
                        val imeBottomPx = WindowInsets.ime.getBottom(density)
                        val isImeVisible = imeBottomPx > 0

                        LaunchedEffect(isImeVisible) {
                            if (!isImeVisible) {
                                focusManager.clearFocus()
                            }
                        }

                        TextField(
                            value = state.label ?: "",
                            onValueChange = { viewModel.onEvent(AlarmUiEvent.LabelChanged(it)) },
                            modifier = Modifier
                                .width(ApplicationTheme.spacing.ultraEpic),
                            singleLine = true,
                            maxLines = 1,
                            placeholder = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Alarm",
                                    textAlign = TextAlign.End,
                                    color = ApplicationTheme.colors.primary.copy(alpha = 0.4f)
                                )
                            },
                            textStyle = TextStyle(
                                color = ApplicationTheme.colors.primary,
                                textDirection = TextDirection.Ltr,
                                textAlign = TextAlign.End
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = ApplicationTheme.colors.primary,
                                unfocusedTextColor = ApplicationTheme.colors.primary.copy(alpha = 1f),
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = ApplicationTheme.colors.secondary,
                            ),
                            shape = RoundedCornerShape(ApplicationTheme.spacing.small)
                        )
                    }

                    AlarmOptionRow("Sound") {
                        TextButton(onClick = {
                            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
                                putExtra(
                                    RingtoneManager.EXTRA_RINGTONE_TYPE,
                                    RingtoneManager.TYPE_ALARM
                                )
                                putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Sound")
                                putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentUri)
                            }
                            ringtonePickerLauncher.launch(intent)
                        }) {
                            Text(soundTitle, color = ApplicationTheme.colors.primary)
                        }
                    }

                    AlarmOptionRow("Snooze", showHorizontalDivider = false) {
                        Switch(
                            checked = state.snoozeEnabled,
                            onCheckedChange = {
                                viewModel.onEvent(AlarmUiEvent.SnoozeToggled)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ApplicationTheme.colors.background,
                                checkedTrackColor = ApplicationTheme.colors.primary,
                                uncheckedThumbColor = Color.Gray
                            )
                        )
                    }
                }

                if(state.alarmId != null) {

                    Spacer(Modifier.height(ApplicationTheme.spacing.extraLarge))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.onEvent(AlarmUiEvent.DeleteAlarm)
                            onBack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ApplicationTheme.colors.onSurface.copy(alpha = 0.1f),
                        ),
                        shape = RoundedCornerShape(ApplicationTheme.spacing.mediumSmall)
                    ) {
                        Text(
                            text = "Delete Alarm",
                            style = ApplicationTheme.typography.bodySmall,
                            color = Color.Red.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        RepeatBottomSheet(
            selectedDays = state.repeatDays,
            onDayToggled = { day ->
                viewModel.onEvent(AlarmUiEvent.RepeatDayToggled(day))
            },
            onDismissRequest = { showBottomSheet = false }
        )
    }
}

@Composable
private fun AlarmTopAppBar(
    title: String,
    onCancel: () -> Unit,
    viewModel: AlarmViewModel,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onCancel) {
            Text("Cancel", color = ApplicationTheme.colors.primary)
        }
        Text(
            text = title,
            color = ApplicationTheme.colors.onSurface,
            style = ApplicationTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = {
            viewModel.onEvent(AlarmUiEvent.SaveAlarm)
            onSave()
        }) {
            Text("Save", color = ApplicationTheme.colors.primary)
        }
    }
}

@Composable
fun AlarmOptionRow(
    label: String,
    showHorizontalDivider: Boolean = true,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ApplicationTheme.spacing.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            color = ApplicationTheme.colors.onSurface,
            style = ApplicationTheme.typography.bodyLarge
        )
        content()
    }
    if (showHorizontalDivider) {
        HorizontalDivider(color = ApplicationTheme.colors.onBackground.copy(alpha = 0.1f))
    }
}
