package pro.mezentsev.forecast.util

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import pro.mezentsev.forecast.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun Double?.asDegree(fallback: String = "-"): String =
    this?.let {
        "${roundToInt()}°"
    } ?: fallback

fun Double?.asUnits(units: String, fallback: String = "-"): String =
    this?.let {
        "${roundToInt()} $units"
    } ?: fallback

fun Double?.asPercentage(fallback: String = "-"): String =
    this?.let {
        "${roundToInt()}%"
    } ?: fallback

fun String?.asIconResource(fallback: Int): Int {
    this ?: return fallback

    return try {
        val iconName = "ic_${replace('-', '_')}"
        val field = R.drawable::class.java.getDeclaredField(iconName)
        field.getInt(field)
    } catch (e: Exception) {
        fallback
    }
}

fun Long.asTime(withTz: Boolean = true): String? {
    val pattern = "HH:mm" + if (withTz) " Z" else ""
    return try {
        val date = Date(this * 1000L)
        SimpleDateFormat(pattern).format(date)
    } catch (e: Exception) {
        null
    }
}