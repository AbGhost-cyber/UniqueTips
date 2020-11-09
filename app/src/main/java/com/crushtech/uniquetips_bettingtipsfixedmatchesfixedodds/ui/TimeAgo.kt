package com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.ui

import java.util.*

private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS
private const val WEEKDAYS = 7

fun currentDate(): Date {
    val calendar = Calendar.getInstance()
    return calendar.time
}

fun getTimeAgo(date: Date): String {
    var time = date.time
    if (time < 1000000000000L) {
        time *= 1000
    }

    val now = currentDate().time
    if (time > now || time <= 0) {
        return "in the future"
    }

    val diff = now - time
    return when {
        diff < MINUTE_MILLIS -> "moments ago"
        diff < 2 * MINUTE_MILLIS -> "a minute ago"
        diff < 60 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} minutes ago"
        diff < 2 * HOUR_MILLIS -> "an hour ago"
        diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
        diff < 48 * HOUR_MILLIS -> "yesterday"
        else -> {
            if (diff / DAY_MILLIS == WEEKDAYS.toLong()
                && diff / DAY_MILLIS < WEEKDAYS * 2.toLong()
            ) {
                "a week ago"
            } else if (diff / DAY_MILLIS == WEEKDAYS * 2.toLong()
                && diff / DAY_MILLIS < WEEKDAYS * 3.toLong()
            ) {
                "2 weeks ago"
            } else if (diff / DAY_MILLIS == WEEKDAYS * 3.toLong()
                && diff / DAY_MILLIS < WEEKDAYS * 4.toLong()
            ) {
                "3 weeks ago"
            } else if (diff / DAY_MILLIS == WEEKDAYS * 4.toLong()
                && diff / DAY_MILLIS < WEEKDAYS * 5.toLong()
            ) {
                "months ago"
            } else {
                "${diff / DAY_MILLIS} days ago"
            }
        }
    }


}