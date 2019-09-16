package pro.mezentsev.forecast.data.dto

import org.json.JSONArray
import org.json.JSONObject
import pro.mezentsev.forecast.model.*

data class ForecastResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val currently: CurrentlyForecast? = null,
    val hourlyBlock: HourlyForecastBlock? = null
)

fun String.toForecastResponse(): ForecastResponse {
    return with(JSONObject(this)) {
        val latitude = getDouble("latitude")
        val longitude = getDouble("longitude")
        val timezone = getString("timezone")
        val currently = optJSONObject("currently").toCurrentlyForecast()
        val hourly = optJSONObject("hourly").toHourlyForecastBlock()

        ForecastResponse(latitude, longitude, timezone, currently, hourly)
    }
}

private fun JSONObject?.toCurrentlyForecast(): CurrentlyForecast? {
    this ?: return null

    val forecastDataPoint = toDataPoint()
    return CurrentlyForecast(
        forecastDataPoint?.cloudCover,
        forecastDataPoint?.dewPoint,
        forecastDataPoint?.humidity,
        forecastDataPoint?.icon,
        optDouble("nearestStormBearing"),
        optDouble("nearestStormDistance"),
        forecastDataPoint?.ozone,
        forecastDataPoint?.precipIntensity,
        forecastDataPoint?.precipIntensityError,
        forecastDataPoint?.precipProbability,
        forecastDataPoint?.precipType,
        forecastDataPoint?.pressure,
        forecastDataPoint?.summary,
        forecastDataPoint?.time ?: 0,
        forecastDataPoint?.uvIndex,
        forecastDataPoint?.visibility,
        forecastDataPoint?.windBearing,
        forecastDataPoint?.windGust,
        forecastDataPoint?.windSpeed,
        forecastDataPoint?.temperature
    )
}

private fun JSONObject?.toDataPoint(): ForecastDataPoint? {
    this ?: return null

    return object : ForecastDataPoint {
        override val temperature: Double?
            get() = optDouble("temperature")
        override val cloudCover: Double?
            get() = optDouble("cloudCover")
        override val dewPoint: Double?
            get() = optDouble("dewPoint")
        override val humidity: Double?
            get() = optDouble("humidity")
        override val icon: String?
            get() = optString("icon")
        override val ozone: Double?
            get() = optDouble("ozone")
        override val precipIntensity: Double?
            get() = optDouble("precipIntensity")
        override val precipIntensityError: Double?
            get() = optDouble("precipIntensityError")
        override val precipProbability: Double?
            get() = optDouble("precipProbability")
        override val precipType: String?
            get() = optString("precipType")
        override val pressure: Double?
            get() = optDouble("pressure")
        override val summary: String?
            get() = optString("summary")
        override val time: Int
            get() = getInt("time")
        override val uvIndex: Double?
            get() = optDouble("uvIndex")
        override val visibility: Double?
            get() = optDouble("visibility")
        override val windBearing: Double?
            get() = optDouble("windBearing")
        override val windGust: Double?
            get() = optDouble("windGust")
        override val windSpeed: Double?
            get() = optDouble("windSpeed")
    }
}

private fun JSONObject?.toHourlyForecastBlock(): HourlyForecastBlock? {
    this ?: return null

    return HourlyForecastBlock(
        optJSONArray("data").toHourlyForecastList(),
        optString("summary"),
        optString("icon")
    )
}

private fun JSONArray?.toHourlyForecastList(): List<HourlyForecast>? {
    this ?: return null
    if (length() == 0) return null

    val hourlyForecastList = mutableListOf<HourlyForecast>()
    for (i in 0 until length()) {
        val jsonObject: JSONObject? = optJSONObject(i)
        hourlyForecastList.add(
            HourlyForecast(
                cloudCover = jsonObject?.optDouble("cloudCover"),
                dewPoint = jsonObject?.optDouble("dewPoint"),
                humidity = jsonObject?.optDouble("humidity"),
                icon = jsonObject?.optString("icon"),
                ozone = jsonObject?.optDouble("ozone"),
                precipIntensity = jsonObject?.optDouble("precipIntensity"),
                precipIntensityError = jsonObject?.optDouble("precipIntensityError"),
                precipProbability = jsonObject?.optDouble("precipProbability"),
                precipType = jsonObject?.optString("precipType"),
                pressure = jsonObject?.optDouble("pressure"),
                summary = jsonObject?.optString("summary"),
                time = jsonObject?.getInt("time") ?: 0,
                uvIndex = jsonObject?.optDouble("uvIndex"),
                visibility = jsonObject?.optDouble("visibility"),
                windBearing = jsonObject?.optDouble("windBearing"),
                windGust = jsonObject?.optDouble("windGust"),
                windSpeed = jsonObject?.optDouble("windSpeed"),
                temperature = jsonObject?.optDouble("temperature")
            )
        )
    }
    return hourlyForecastList
}