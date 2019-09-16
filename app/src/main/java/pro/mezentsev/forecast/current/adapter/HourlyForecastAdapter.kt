package pro.mezentsev.forecast.current.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import pro.mezentsev.forecast.R
import pro.mezentsev.forecast.model.HourlyForecast
import pro.mezentsev.forecast.util.asDegree
import pro.mezentsev.forecast.util.asIconResource
import pro.mezentsev.forecast.util.asTime

class HourlyForecastAdapter constructor(private val context: Context) :
    RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastHolder>() {

    private var forecast: List<HourlyForecast> = mutableListOf()

    fun setForecast(forecast: List<HourlyForecast>?) {
        forecast ?: return

        this.forecast = forecast
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastHolder {
        return HourlyForecastHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.forecast_hour_view, parent, false)
        )
    }

    override fun getItemCount() = forecast.size

    override fun onBindViewHolder(holder: HourlyForecastHolder, position: Int) {
        holder.bind(forecast[position])
    }

    class HourlyForecastHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        private val icon = view.findViewById<ImageView>(R.id.hour_weather_icon)
        private val temperature = view.findViewById<TextView>(R.id.hour_temperature_view)
        private val time = view.findViewById<TextView>(R.id.hour_view)

        fun bind(forecast: HourlyForecast) {
            icon.setImageResource(forecast.icon.asIconResource(0))
            icon.setColorFilter(Color.BLACK)
            temperature.text = forecast.temperature?.asDegree()
            time.text = forecast.time.toLong().asTime(false)
        }
    }
}