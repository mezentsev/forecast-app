package pro.mezentsev.forecast.current

import pro.mezentsev.forecast.data.ForecastRepository
import pro.mezentsev.forecast.data.dto.ForecastRequest
import pro.mezentsev.forecast.data.dto.ForecastResponse
import pro.mezentsev.forecast.util.log
import pro.mezentsev.forecast.util.loge
import pro.mezentsev.reactive.CompositeSubscription
import pro.mezentsev.reactive.InterruptedExecutor
import pro.mezentsev.reactive.Observable
import pro.mezentsev.reactive.Subscriber

open class CurrentForecastInteractor(private val repository: ForecastRepository) {
    private val compositeSubscription = CompositeSubscription()
    private val interruptedExecutor = InterruptedExecutor()
    private val mainThreadExecutor = Observable.MainThreadExecutor.get()

    fun loadCurrentForecast(
        forecastRequest: ForecastRequest,
        onLoad: (forecast: ForecastResponse?) -> Unit,
        onError: (t: Throwable) -> Unit
    ) {
        val subscription = repository.loadForecast(forecastRequest)
            .subscribeOn(interruptedExecutor)
            .observeOn(mainThreadExecutor)
            .subscribe(object : Subscriber<ForecastResponse> {
                override fun onNext(forecast: ForecastResponse?) {
                    "Forecast: $forecast".log()
                    onLoad(forecast)
                }

                override fun onError(t: Throwable) {
                    "Error".loge(t)
                    onError(t)
                }

                override fun onComplete() {
                    "Complete".log()
                    cancel()
                }
            })

        compositeSubscription.add(subscription)
    }

    fun cancel() {
        compositeSubscription.unsubscribe()
        interruptedExecutor.cancel()
    }
}