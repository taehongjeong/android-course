package com.example.week4myweather.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.week4myweather.R
import com.example.week4myweather.model.WeatherData
import com.example.week4myweather.network.OpenWeatherApiService
import com.example.week4myweather.databinding.ItemWeatherBinding
import java.util.Locale

class WeatherAdapter(
    private val onDeleteClick: (WeatherData) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    
    private var weatherList = listOf<WeatherData>()
    
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<WeatherData>) {
        weatherList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return WeatherViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }
    
    override fun getItemCount(): Int = weatherList.size

    class WeatherViewHolder(
        private val binding: ItemWeatherBinding,
        private val onDeleteClick: (WeatherData) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(weatherData: WeatherData) {
            binding.apply {
                tvCityName.text = weatherData.cityName
                tvTemperature.text = String.format(Locale.getDefault(), "%.0f°", weatherData.temperature)
                tvDescription.text = weatherData.description
                tvHumidity.text = root.context.getString(
                    R.string.format_humidity,
                    weatherData.humidity
                )
                tvWindSpeed.text = root.context.getString(
                    R.string.format_wind_speed,
                    weatherData.windSpeed
                )
                loadWeatherIcon(weatherData.icon)
                btnDelete.setOnClickListener {
                    onDeleteClick(weatherData)
                }
            }
        }
        
        private fun loadWeatherIcon(iconCode: String) {
            val iconUrl = OpenWeatherApiService.getIconUrl(iconCode)
            
            Glide.with(binding.ivWeatherIcon.context)
                .load(iconUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into(binding.ivWeatherIcon)
        }
    }
}