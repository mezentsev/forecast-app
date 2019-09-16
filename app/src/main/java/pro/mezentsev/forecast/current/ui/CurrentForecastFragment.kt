package pro.mezentsev.forecast.current.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout.HORIZONTAL
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import pro.mezentsev.forecast.R
import pro.mezentsev.forecast.current.CurrentForecastContract
import pro.mezentsev.forecast.current.adapter.HourlyForecastAdapter
import pro.mezentsev.forecast.data.dto.ForecastResponse
import pro.mezentsev.forecast.model.CurrentlyForecast
import pro.mezentsev.forecast.model.HourlyForecastBlock
import pro.mezentsev.forecast.ui.BaseFragment
import pro.mezentsev.forecast.util.*
import kotlin.math.roundToInt

class CurrentForecastFragment : BaseFragment<CurrentForecastContract.Presenter>(),
    CurrentForecastContract.View {

    private lateinit var geocoder: Geocoder
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var hourlyForecastAdapter: HourlyForecastAdapter
    private lateinit var hourlyForecastLayoutManager: LinearLayoutManager
    private lateinit var progressBar: ProgressBar

    private lateinit var forecastContainer: ViewGroup

    private lateinit var placeView: TextView
    private lateinit var timeView: TextView
    private lateinit var weatherIcon: ImageView
    private lateinit var summaryView: TextView
    private lateinit var temperatureView: TextView
    private lateinit var windView: TextView
    private lateinit var humidityView: TextView
    private lateinit var dewView: TextView
    private lateinit var uvView: TextView
    private lateinit var visibilityView: TextView
    private lateinit var pressureView: TextView

    private var place: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        geocoder = Geocoder(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        askLocationPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.attach(this)

        val view = inflater.inflate(R.layout.fragment_list, container, false)
        progressBar = view.findViewById(R.id.progress_view)

        hourlyForecastAdapter = HourlyForecastAdapter(view.context)
        hourlyForecastLayoutManager = LinearLayoutManager(context)
        hourlyForecastLayoutManager.orientation = HORIZONTAL

        view.findViewById<RecyclerView>(R.id.hourly_forecast_list).apply {
            adapter = hourlyForecastAdapter
            layoutManager = hourlyForecastLayoutManager
        }

        placeView = view.findViewById(R.id.forecast_place)
        timeView = view.findViewById(R.id.forecast_time)

        weatherIcon = view.findViewById(R.id.weather_icon)
        forecastContainer = view.findViewById(R.id.forecast_container)

        summaryView = view.findViewById(R.id.summary_view)
        temperatureView = view.findViewById(R.id.temperature_view)

        windView = view.findViewById(R.id.wind_view)
        humidityView = view.findViewById(R.id.humidity_view)
        dewView = view.findViewById(R.id.dew_view)
        uvView = view.findViewById(R.id.uv_view)
        visibilityView = view.findViewById(R.id.visibility_view)
        pressureView = view.findViewById(R.id.pressure_view)

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
        askForecastWithLocation()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_LOCATION_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            askForecastWithLocation()
        }
    }

    override fun showForecast(forecast: ForecastResponse) {
        hideProgress()
        bindCurrentlyForecast(forecast.currently)
        bindHourlyForecast(forecast.hourlyBlock)

        forecastContainer.animate().alphaBy(1f).start()
    }

    private fun bindHourlyForecast(hourlyBlock: HourlyForecastBlock?) {
        hourlyBlock ?: return

        hourlyForecastAdapter.setForecast(hourlyBlock.data)
    }

    private fun bindCurrentlyForecast(currentlyForecast: CurrentlyForecast?) {
        currentlyForecast ?: return
        val fallback = requireContext().resources.getString(R.string.app_name)

        placeView.text = place

        timeView.text = currentlyForecast.time.toLong().asTime()
        summaryView.text = currentlyForecast.summary
        weatherIcon.setImageResource(currentlyForecast.icon.asIconResource(0))
        weatherIcon.setColorFilter(Color.BLACK)
        temperatureView.text = currentlyForecast.temperature.asDegree(fallback)
        windView.text = currentlyForecast.windSpeed.asUnits("kph", fallback)
        humidityView.text = currentlyForecast.humidity.asPercentage(fallback)
        dewView.text = currentlyForecast.dewPoint.asDegree(fallback)
        uvView.text = currentlyForecast.uvIndex?.roundToInt()?.toString() ?: fallback
        visibilityView.text = currentlyForecast.visibility.asUnits("km", fallback)
        pressureView.text = currentlyForecast.visibility.asUnits("hPa", fallback)
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }


    override fun showError(e: Throwable) {
        hideProgress()
        Toast.makeText(
            context,
            "${getString(R.string.error_loading_forecast)}: ${e.message}",
            Toast.LENGTH_LONG
        )
            .show()
    }

    override fun onDestroyView() {
        presenter.detach()
        super.onDestroyView()
    }

    private fun askLocationPermission() {
        if (LOCATION_PERMISSIONS.any { permission ->
                ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_DENIED
            }) {
            requestPermissions(LOCATION_PERMISSIONS, PERMISSION_LOCATION_REQUEST)
        }
    }

    private fun askForecastWithLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                presenter.setLocation(location)

                try {
                    val address =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (address.isNotEmpty()) {
                        val addressFragments = with(address[0]) {
                            (0..maxAddressLineIndex).map { getAddressLine(it) }
                        }
                        place = addressFragments.joinToString(separator = " ")
                    }
                } catch (e: Exception) {
                    "Can't decode location".log()
                }
            }
            .addOnFailureListener {
                presenter.loadForecast()
            }
    }

    companion object {
        private const val PERMISSION_LOCATION_REQUEST = 123
        private val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        @JvmStatic
        fun newInstance() = CurrentForecastFragment()
    }
}
