package com.choice.wakeuplanner.presentation.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val extraExtraSmall: Dp = 2.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val mediumSmall: Dp = 12.dp,
    val medium: Dp = 16.dp,
    val mediumLarge: Dp = 20.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
    val huge: Dp = 40.dp,
    val extraHuge: Dp = 48.dp,
    val massive: Dp = 56.dp,
    val extraMassive: Dp = 64.dp,
    val gigantic: Dp = 72.dp,
    val extraGigantic: Dp = 80.dp,
    val enormous: Dp = 88.dp,
    val extraEnormous: Dp = 96.dp,
    val colossal: Dp = 104.dp,
    val extraColossal: Dp = 112.dp,
    val ultraEpic: Dp = 200.dp
)
internal val LocalSpacing = compositionLocalOf { Spacing() }