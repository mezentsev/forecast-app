package pro.mezentsev.forecast.data

import pro.mezentsev.forecast.data.api.ForecastApi
import pro.mezentsev.forecast.data.dto.ForecastRequest
import pro.mezentsev.forecast.data.dto.ForecastResponse
import pro.mezentsev.reactive.Observable
import java.lang.Exception

class ForecastRepositoryImpl(private val api: ForecastApi) : ForecastRepository {

    override fun loadForecast(forecastRequest: ForecastRequest): Observable<ForecastResponse> {
        return Observable.create<ForecastResponse> {
            try {
                val forecastResponse = api.getForecast(forecastRequest)

                it.onNext(forecastResponse)
                it.onComplete()
            } catch (ex: Exception) {
                it.onError(ex)
            }
        }
    }
}