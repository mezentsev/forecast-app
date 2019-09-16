package pro.mezentsev.forecast

import android.app.Application
import pro.mezentsev.forecast.data.ForecastRepository
import pro.mezentsev.forecast.data.ForecastRepositoryImpl
import pro.mezentsev.forecast.data.api.ForecastApi
import pro.mezentsev.forecast.data.api.HttpForecastApi

class ForecastApp : Application() {
    companion object {
        fun repository(): ForecastRepository = ForecastRepositoryImpl(forecastApi())
        fun forecastApi(): ForecastApi = HttpForecastApi()
    }

    override fun onCreate() {
        super.onCreate()
    }
}