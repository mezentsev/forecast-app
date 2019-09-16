package pro.mezentsev.forecast.model

interface ForecastDataPoint {
    val cloudCover: Double?
    val dewPoint: Double?
    val humidity: Double?
    val icon: String?
    val ozone: Double?
    val precipIntensity: Double?
    val precipIntensityError: Double?
    val precipProbability: Double?
    val precipType: String?
    val pressure: Double?
    val summary: String?
    val time: Int
    val uvIndex: Double?
    val visibility: Double?
    val windBearing: Double?
    val windGust: Double?
    val windSpeed: Double?
    val temperature: Double?
}