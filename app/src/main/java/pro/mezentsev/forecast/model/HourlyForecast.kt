package pro.mezentsev.forecast.model

data class HourlyForecast (
    override val cloudCover: Double? = null,
    override val dewPoint: Double? = null,
    override val humidity: Double? = null,
    override val icon: String? = null,
    override val ozone: Double? = null,
    override val precipIntensity: Double? = null,
    override val precipIntensityError: Double? = null,
    override val precipProbability: Double? = null,
    override val precipType: String? = null,
    override val pressure: Double? = null,
    override val summary: String? = null,
    override val time: Int,
    override val uvIndex: Double? = null,
    override val visibility: Double? = null,
    override val windBearing: Double? = null,
    override val windGust: Double? = null,
    override val windSpeed: Double? = null,
    override val temperature: Double? = null
) : ForecastDataPoint