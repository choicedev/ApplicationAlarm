package com.choice.wakeuplanner.domain.model

import java.time.DayOfWeek

data class RepeatDay(
    val id: Int,
    val label: String,
    val dayOfWeek: DayOfWeek
) {
    companion object {
        private val dayMappings = listOf(
            RepeatDay(1, "mon", DayOfWeek.MONDAY),
            RepeatDay(2, "tue", DayOfWeek.TUESDAY),
            RepeatDay(3, "wed", DayOfWeek.WEDNESDAY),
            RepeatDay(4, "thu", DayOfWeek.THURSDAY),
            RepeatDay(5, "fri", DayOfWeek.FRIDAY),
            RepeatDay(6, "sat", DayOfWeek.SATURDAY),
            RepeatDay(7, "sun", DayOfWeek.SUNDAY)
        )

        fun fromDayOfWeek(day: DayOfWeek): RepeatDay {
            return dayMappings.first { it.dayOfWeek == day }
        }

        fun formatDays(days: List<DayOfWeek>): String {
            return when {
                days.size == 7 -> "Every day"
                days.isEmpty() -> "None"
                else -> days.sortedBy { it.value }.joinToString(", ") { fromDayOfWeek(it).label }.replaceFirstChar { it.uppercase() }
            }
        }
    }
}