package pro.mezentsev.forecast.model

interface ForecastDataBlock {
    val data: List<ForecastDataPoint>?
    val summary: String?
    val icon: String?
}