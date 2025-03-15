package dev.balikin.poject.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface BrowserHelper {
    fun openBrowser(url: String)
}

expect fun getBrowserHelper(): BrowserHelper

fun formattedDate(timestamp: Long): String {
    val localDateTime = Instant.fromEpochMilliseconds(timestamp)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDateTime.date.dayOfMonth.toString().padStart(2, '0')}/" +
            "${localDateTime.date.monthNumber.toString().padStart(2, '0')}/" +
            "${localDateTime.date.year}"
}