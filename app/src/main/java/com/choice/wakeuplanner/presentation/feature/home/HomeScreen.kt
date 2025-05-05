package com.choice.wakeuplanner.presentation.feature.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.choice.wakeuplanner.core.navigation.Destination
import com.choice.wakeuplanner.domain.model.Alarm
import com.choice.wakeuplanner.presentation.feature.home.composable.DeleteAlarmsBottomSheet
import com.choice.wakeuplanner.presentation.feature.home.model.HomeUiEvent
import com.choice.wakeuplanner.presentation.theme.ApplicationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val alarms = state.listAlarms
    val isLoading = state.isLoading

    var showBottomSheet by remember { mutableStateOf(false) }

    if(showBottomSheet){
        DeleteAlarmsBottomSheet(
            onConfirm = {
                viewModel.onEvent(HomeUiEvent.DeleteAllAlarms)
                showBottomSheet = false
            },
            onDismissRequest = {
                showBottomSheet = false
            }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            HomeTopBar {
                showBottomSheet = !showBottomSheet
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(color = ApplicationTheme.colors.background),
            horizontalAlignment = if (isLoading) Alignment.CenterHorizontally else Alignment.Start,
            verticalArrangement = if (isLoading) Arrangement.Center else Arrangement.Top
        ) {

            if (isLoading) {
                CircularProgressIndicator()
            }else {

                LazyColumn {
                    items(alarms, key = { it.id!! }) { alarm ->
                        SwipeToRevealDeleteItem(
                            onDelete = {
                                viewModel.onEvent(HomeUiEvent.DeleteAlarm(alarm.id!!))
                            },
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                        ) {
                            AlarmCardItem(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .fillMaxWidth(),
                                alarm = alarm,
                                onClick = {
                                    navController.navigate(Destination.EditAlarm(alarm.id!!))
                                },
                                onCheckedChange = {
                                    viewModel.onEvent(HomeUiEvent.UpdateAlarm(alarm.id!!, it))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    onShowBottomSheet: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ApplicationTheme.spacing.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Alarm",
            color = ApplicationTheme.colors.onSurface,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onShowBottomSheet) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Menu",
                tint = ApplicationTheme.colors.onSurface
            )
        }
    }
}

@Composable
fun AlarmCardItem(
    modifier: Modifier = Modifier,
    alarm: Alarm,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit = {}
) {

    val isActive = alarm.isActive

    val subTextColor =
        if (isActive) ApplicationTheme.colors.primary else ApplicationTheme.colors.onSurface.copy(
            alpha = 0.1f
        )

    val titleColor =
        if (isActive) ApplicationTheme.colors.onSurface else ApplicationTheme.colors.onSurface.copy(
            alpha = 0.1f
        )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = ApplicationTheme.colors.surfaceVariant.copy(
                if (isActive) 1f else 0.95f
            )
        ),
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(ApplicationTheme.spacing.medium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontSize = ApplicationTheme.typography.displayMedium.fontSize)) {
                                append(alarm.timeFormatted)
                            }
                            withStyle(SpanStyle(fontSize = ApplicationTheme.typography.labelLarge.fontSize)) {
                                append(alarm.getAm)
                            }
                        },
                        color = titleColor,
                        style = ApplicationTheme.typography.labelLarge
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontSize = ApplicationTheme.typography.labelLarge.fontSize)) {
                                append(alarm.label)
                            }
                            append(", ")
                            withStyle(SpanStyle(fontSize = ApplicationTheme.typography.labelLarge.fontSize)) {
                                append(alarm.repeatDaysFormatted)
                            }
                        },
                        color = subTextColor,
                        style = ApplicationTheme.typography.labelLarge
                    )
                }
                Switch(
                    checked = alarm.isActive,
                    onCheckedChange = {
                        onCheckedChange(it)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ApplicationTheme.colors.background
                    )
                )
            }
        }
    }
}

@Composable
fun SwipeToRevealDeleteItem(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val screenWidth =
        LocalDensity.current.run { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val buttonOffset = with(LocalDensity.current) { 80.dp.toPx() }
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val cardHeightCollapsed = 0.dp
    val cardHeightExpanded = ApplicationTheme.spacing.extraColossal
    var isDeleted by remember { mutableStateOf(false) }

    val targetHeight by animateDpAsState(
        targetValue = if (isDeleted) cardHeightCollapsed else cardHeightExpanded,
        label = "CardHeight"
    )

    suspend fun deleteItem() {
        isDeleted = true
        offsetX.animateTo(-screenWidth)
        delay(500L)
        onDelete()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(targetHeight)
            .animateContentSize()
            .background(Color.Transparent)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        scope.launch {
                            val newOffset = (offsetX.value + dragAmount).coerceIn(-screenWidth, 0f)
                            offsetX.snapTo(newOffset)
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            when {
                                offsetX.value < -screenWidth * 0.7f -> {
                                    deleteItem()
                                }

                                offsetX.value < -buttonOffset -> {
                                    offsetX.animateTo(-buttonOffset)
                                }

                                else -> {
                                    offsetX.animateTo(0f)
                                }
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(ApplicationTheme.spacing.small)
                .animateContentSize()
                .fillMaxWidth()
                .fillMaxHeight()
                .background(ApplicationTheme.colors.onError, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(
                modifier = Modifier.padding(end = ApplicationTheme.spacing.medium),
                onClick = {
                    scope.launch {
                        deleteItem()
                    }
                }
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
            }
        }

        Box(
            modifier = Modifier
                .animateContentSize()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
        ) {
            content()
        }
    }
}