package pro.mezentsev.forecast.data.api

import pro.mezentsev.forecast.BuildConfig
import pro.mezentsev.forecast.data.dto.ForecastRequest
import pro.mezentsev.forecast.data.dto.ForecastResponse
import pro.mezentsev.forecast.data.dto.toForecastResponse
import pro.mezentsev.forecast.util.log
import java.net.HttpURLConnection
import java.net.URL

class HttpForecastApi: ForecastApi {
    private companion object {
        private const val FORECAST_URL = "https://api.darksky.net/forecast/%s/%f,%f?units=si"
    }

    override fun getForecast(forecastRequest: ForecastRequest): ForecastResponse {

        val requestedUrl = URL(FORECAST_URL.format(BuildConfig.API_KEY, forecastRequest.latitude, forecastRequest.longitude))
        requestedUrl.toString().log()

        val json = with(requestedUrl.openConnection() as HttpURLConnection) {
            "$responseCode".log()
            inputStream.bufferedReader().use { reader ->
                reader.readText()
            }
        }

        return json.toForecastResponse()
    }
}