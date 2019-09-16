package pro.mezentsev.forecast.current.presentation

import android.location.Location
import pro.mezentsev.forecast.current.CurrentForecastContract
import pro.mezentsev.forecast.current.CurrentForecastInteractor
import pro.mezentsev.forecast.data.dto.ForecastRequest
import pro.mezentsev.forecast.data.dto.ForecastResponse
import pro.mezentsev.forecast.util.asUnits
import pro.mezentsev.forecast.util.log

class CurrentForecastPresenter(private val currentForecastInteractor: CurrentForecastInteractor) :
    CurrentForecastContract.Presenter() {

    override fun onResume() {
    }

    override fun onPause() {
        currentForecastInteractor.cancel()
    }

    override fun setLocation(lastKnownLocation: Location) {
        loadForecast(
            ForecastRequest(lastKnownLocation.latitude, lastKnownLocation.longitude)
        )
    }

    override fun loadForecast() {
        loadForecast(ForecastRequest())
    }

    private fun loadForecast(request: ForecastRequest) {
        view?.showProgress()
        currentForecastInteractor.loadCurrentForecast(request, this::onLoad, this::onError)
    }

    private fun onLoad(currentForecast: ForecastResponse?) {
        if (currentForecast == null) {
            onError(IllegalStateException("No forecast today"))
            return
        }

        view?.showForecast(currentForecast)
    }

    private fun onError(t: Throwable) {
        view?.showError(t)
    }
}