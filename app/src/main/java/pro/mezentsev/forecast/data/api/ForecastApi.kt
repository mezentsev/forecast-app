package pro.mezentsev.forecast.data.api

import pro.mezentsev.forecast.data.dto.ForecastRequest
import pro.mezentsev.forecast.data.dto.ForecastResponse

interface ForecastApi {
    fun getForecast(forecastRequest: ForecastRequest): ForecastResponse
}