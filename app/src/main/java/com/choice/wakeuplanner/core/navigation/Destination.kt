package com.choice.wakeuplanner.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Destination(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Home : Destination("home")

    object EditAlarm : Destination(
        route = "alarms/edit?id={id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
        operator fun invoke(id: Long?): String {
            val newId = id ?: -1L
            return "alarms/edit?id=$newId"
        }
    }
}