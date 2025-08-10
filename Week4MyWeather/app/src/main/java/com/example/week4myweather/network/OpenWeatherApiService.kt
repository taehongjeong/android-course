package com.example.week4myweather.network

import com.example.week4myweather.model.OpenWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {
    
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = com.example.week4myweather.WeatherApplication.API_UNITS_METRIC,
        @Query("lang") lang: String = com.example.week4myweather.WeatherApplication.API_LANG_KR
    ): Response<OpenWeatherResponse>
    
    companion object {
        val BASE_URL: String
            get() = com.example.week4myweather.WeatherApplication.OPEN_WEATHER_BASE_URL
        val API_KEY: String
            get() = com.example.week4myweather.WeatherApplication.OPEN_WEATHER_API_KEY
        
        fun getIconUrl(iconCode: String) = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
    }
}