package pro.mezentsev.forecast.current

import android.location.Location
import pro.mezentsev.forecast.data.dto.ForecastResponse
import pro.mezentsev.forecast.ui.Contract

interface CurrentForecastContract {
    interface View : Contract.BaseView {
        fun showForecast(forecast: ForecastResponse)
        fun showProgress()
        fun showError(e: Throwable)
    }

    abstract class Presenter : Contract.BasePresenter<View>() {
        abstract fun loadForecast()
        abstract fun setLocation(lastKnownLocation: Location)
    }
}