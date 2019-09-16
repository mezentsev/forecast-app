package pro.mezentsev.forecast.data

import pro.mezentsev.forecast.data.dto.ForecastRequest
import pro.mezentsev.forecast.data.dto.ForecastResponse
import pro.mezentsev.reactive.Observable

interface ForecastRepository {

    fun loadForecast(forecastRequest: ForecastRequest): Observable<ForecastResponse>

}