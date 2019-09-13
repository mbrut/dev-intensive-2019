package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR


fun Date.format(pattern: String="HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

data class HumanizeVals(val value: String, val before: String ="назад", val after: String = "через") {
    fun getTextual(diff: Long): String = (if (diff <= 0) "$after $value" else "$value $before").trim()
}

fun Date.humanizeDiff(): String {
    val timeDiff = Date().time - this.time
    return when (val absDiff = timeDiff.absoluteValue) {
        in 0..1 * SECOND -> HumanizeVals("только что", "", "")
        in 1 * SECOND..45 * SECOND -> HumanizeVals("несколько секунд")
        in 45 * SECOND..75 * SECOND-> HumanizeVals("минуту")
        in 75 * SECOND..45 * MINUTE -> HumanizeVals(TimeUnits.MINUTE.plural(absDiff / MINUTE))
        in 45 * MINUTE..75 * MINUTE -> HumanizeVals("час")
        in 75 * MINUTE..22 * HOUR -> HumanizeVals(TimeUnits.HOUR.plural(absDiff / HOUR))
        in 22 * HOUR..26 * HOUR -> HumanizeVals("день")
        in 26 * HOUR..360 * DAY -> HumanizeVals(TimeUnits.DAY.plural(absDiff / DAY))
        else -> HumanizeVals("", "более года назад", "более чем через год")
    }.getTextual(timeDiff)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time
    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

enum class TimeUnits(private val plurals: List<String> = listOf("")) {
    SECOND(listOf("секунду", "секунды", "секунд")),
    MINUTE(listOf("минуту", "минуты", "минут")),
    HOUR(listOf("час", "часа", "часов")),
    DAY(listOf("день", "дня", "дней"));

    fun plural(n: Long): String {
        val plrl = if (n % 10 == 1L && n % 100 != 11L) plurals[0] else if (n % 10 in 2..4 && (n % 100 < 10 || n % 100 >= 20)) plurals[1] else plurals[2]
        return "$n $plrl"
    }
}

fun Date.humanizeDiff(date: Date = Date()):String {
    return ""
}