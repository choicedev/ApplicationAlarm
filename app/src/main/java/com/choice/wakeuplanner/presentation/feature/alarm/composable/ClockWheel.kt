package com.choice.wakeuplanner.presentation.feature.alarm.composable

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.choice.wakeuplanner.core.helper.vibrateDevice
import com.choice.wakeuplanner.presentation.theme.ApplicationTheme


@Composable
fun CircularTimePicker(
    modifier: Modifier = Modifier,
    initialHour: Int = 6,
    initialMinute: Int = 30,
    initialMeridian: String = "AM",
    onTimeSelected: (Int, Int, String) -> Unit
) {
    val minutes = (0..59).toList()
    val meridians = listOf("AM", "PM")

    var selectedHour by remember { mutableIntStateOf(initialHour.coerceIn(1, 12)) }
    var selectedMinute by remember { mutableIntStateOf(initialMinute) }
    var selectedMeridian by remember { mutableStateOf(initialMeridian) }
    val hours = (1..12).toList()

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .height(ApplicationTheme.spacing.huge + 10.dp)
                .fillMaxWidth()
                .animateContentSize()
                .background(
                    ApplicationTheme.colors.onBackground.copy(alpha = 0.1f), RoundedCornerShape(
                        ApplicationTheme.spacing.small
                    )
                )
        )

        Row(
            modifier = modifier.padding(ApplicationTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(ApplicationTheme.spacing.mediumSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InfiniteWheel(
                items = hours.map { it.toString().padStart(2, '0') },
                initialSelectedIndex = initialHour-1,
                onItemSelected = { selectedHour = it.toInt() }
            )

            InfiniteWheel(
                items = minutes.map { it.toString().padStart(2, '0') },
                initialSelectedIndex = initialMinute,
                onItemSelected = { selectedMinute = it.toInt() }
            )

            StaticWheel(
                items = meridians,
                initialSelectedIndex = if (initialMeridian == "AM") 0 else 1,
                onItemSelected = { selectedMeridian = it }
            )
        }
    }


    LaunchedEffect(selectedHour, selectedMinute, selectedMeridian) {
        onTimeSelected(selectedHour, selectedMinute, selectedMeridian)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfiniteWheel(
    items: List<String>,
    visibleItemsCount: Int = 5,
    initialSelectedIndex: Int = 0,
    onItemSelected: (String) -> Unit
) {
    val itemHeight = ApplicationTheme.spacing.extraLarge
    val centerItemIndex = visibleItemsCount / 2

    val infiniteOffset = Int.MAX_VALUE / 2
    val initialIndex = remember {
        infiniteOffset - (infiniteOffset % items.size) + initialSelectedIndex
    }

    val context = LocalContext.current

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

    val firstVisibleItemScrollOffset by remember { derivedStateOf { listState.firstVisibleItemScrollOffset } }

    val visibleIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }

    LaunchedEffect(firstVisibleItemScrollOffset) {
        if (listState.isScrollInProgress) {
            vibrateDevice(5, context)
        }
    }

    LaunchedEffect(Unit) {
        listState.scrollToItem(
            index = initialIndex,
            scrollOffset = 0
        )
    }

    LaunchedEffect(listState.isScrollInProgress.not(), initialIndex) {
        val selectedItem = items[visibleIndex % items.size]
        onItemSelected(selectedItem)
    }

    Box(
        modifier = Modifier
            .width(ApplicationTheme.spacing.gigantic)
            .height(itemHeight * visibleItemsCount),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = itemHeight * centerItemIndex),
            horizontalAlignment = Alignment.CenterHorizontally,
            flingBehavior = rememberSnapFlingBehavior(listState),
        ) {
            items(Int.MAX_VALUE) { index ->
                val item = items[index % items.size]

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        style = ApplicationTheme.typography.bodyLarge.copy(
                            fontSize = if (visibleIndex == index) ApplicationTheme.typography.bodyLarge.fontSize * 1.5f else ApplicationTheme.typography.bodyLarge.fontSize
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = ApplicationTheme.colors.onSurface.copy(alpha = if (visibleIndex == index) 1f else 0.2f)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StaticWheel(
    items: List<String>,
    initialSelectedIndex: Int = 0,
    onItemSelected: (String) -> Unit
) {
    val itemHeight = ApplicationTheme.spacing.extraLarge
    val visibleItemsCount = 3
    val centerItemIndex = 1

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialSelectedIndex)

    val visibleIndex by remember {
        derivedStateOf {
            (listState.firstVisibleItemIndex).coerceIn(0, items.lastIndex)
        }
    }

    LaunchedEffect(Unit) {
        listState.scrollToItem(
            index = initialSelectedIndex,
            scrollOffset = 0
        )
    }

    LaunchedEffect(visibleIndex) {
        onItemSelected(items[visibleIndex])
    }

    Box(
        modifier = Modifier
            .width(72.dp)
            .height(itemHeight * visibleItemsCount),
        contentAlignment = Alignment.Center
    ) {

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = itemHeight * centerItemIndex),
            horizontalAlignment = Alignment.CenterHorizontally,
            flingBehavior = rememberSnapFlingBehavior(listState),
        ) {
            items(items.size) { index ->
                Box(
                    modifier = Modifier
                        .height(itemHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = items[index],
                        style = ApplicationTheme.typography.bodyLarge.copy(
                            fontSize = if (visibleIndex == index) ApplicationTheme.typography.bodyLarge.fontSize * 1.5f else ApplicationTheme.typography.bodyLarge.fontSize
                        ),
                        color = ApplicationTheme.colors.onSurface.copy(
                            alpha = if (visibleIndex == index) 1f else 0.2f
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}