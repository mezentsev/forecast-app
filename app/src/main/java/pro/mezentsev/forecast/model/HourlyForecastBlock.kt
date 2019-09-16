package pro.mezentsev.forecast.model

data class HourlyForecastBlock (
    override val data: List<HourlyForecast>?,
    override val summary: String? = null,
    override val icon: String? = null
) : ForecastDataBlock