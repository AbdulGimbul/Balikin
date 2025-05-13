package dev.balikin.poject.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

expect fun currencyFormat(amount: Double): String

interface BrowserHelper {
    fun openBrowser(url: String)
}

expect fun getBrowserHelper(): BrowserHelper

fun formattedDate(dateString: String): String {
    val localDateTime = try {
        LocalDateTime.parse(dateString)
    } catch (e: Exception) {
        return ""
    }

    return "${localDateTime.dayOfMonth.toString().padStart(2, '0')}/" +
            "${localDateTime.monthNumber.toString().padStart(2, '0')}/" +
            "${localDateTime.year}"
}

fun getCurrentFormattedDateTime(): String {
    val currentMoment = Clock.System.now()
    val dateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    val day = dateTime.dayOfMonth.toString().padStart(2, '0')
    val month = dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    val year = dateTime.year
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')

    return "$day $month $year, $hour:$minute WIB"
}

fun formatDateCreated(dateTime: LocalDateTime): String {
    val day = dateTime.dayOfMonth
    val month = formatMonthIndo(dateTime.monthNumber)
    val time = formatTimeTo12Hour(dateTime.hour, dateTime.minute)

    return "$day $month ● $time"
}

fun formatMonthIndo(month: Int): String {
    return listOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )[month - 1]
}

fun formatTimeTo12Hour(hour: Int, minute: Int): String {
    val amPm = if (hour < 12) "AM" else "PM"
    val hour12 = if (hour % 12 == 0) 12 else hour % 12
    val minuteStr = if (minute < 10) "0$minute" else "$minute"
    val hourStr = if (hour12 < 10) "0$hour12" else "$hour12"
    return "$hourStr.$minuteStr $amPm"
}

fun formatDate(date: LocalDateTime): String {
    val day = date.dayOfMonth.toString().padStart(2, '0')
    val month = date.month.name.substring(0, 3).lowercase().replaceFirstChar { it.uppercase() }
    val year = date.year
    return "$day $month $year"
}

fun getLastWeekDate(): LocalDateTime {
    val now = Clock.System.now()
    val lastWeekInstant = now.minus(
        DateTimePeriod(days = 7),
        TimeZone.currentSystemDefault()
    )
    return lastWeekInstant.toLocalDateTime(TimeZone.currentSystemDefault())
}

fun getCurrentDate(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}