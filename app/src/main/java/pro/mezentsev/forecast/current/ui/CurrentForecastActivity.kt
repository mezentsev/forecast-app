package pro.mezentsev.forecast.current.ui

import android.os.Bundle
import pro.mezentsev.forecast.ForecastApp
import pro.mezentsev.forecast.R
import pro.mezentsev.forecast.current.CurrentForecastInteractor
import pro.mezentsev.forecast.current.presentation.CurrentForecastPresenter
import pro.mezentsev.forecast.ui.BaseActivity

class CurrentForecastActivity : BaseActivity() {
    private val currentForecastInteractor = CurrentForecastInteractor(ForecastApp.repository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val forecastPresenter = CurrentForecastPresenter(currentForecastInteractor)
        val frameId = R.id.frame
        (supportFragmentManager.findFragmentById(frameId) as CurrentForecastFragment?)
            ?.apply { presenter = forecastPresenter }
            ?: CurrentForecastFragment.newInstance().apply {
                presenter = forecastPresenter
                replaceFragment(frameId, this)
            }
    }
}
