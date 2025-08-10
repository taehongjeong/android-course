package com.example.week4myweather.model

import com.google.gson.annotations.SerializedName

// OpenWeather API Response Models
data class OpenWeatherResponse(
    @SerializedName("name")
    val cityName: String,
    
    @SerializedName("main")
    val main: Main,
    
    @SerializedName("weather")
    val weather: List<Weather>,
    
    @SerializedName("wind")
    val wind: Wind,
    
    @SerializedName("dt")
    val timestamp: Long
)

data class Main(
    @SerializedName("temp")
    val temperature: Double,
    
    @SerializedName("humidity")
    val humidity: Int,
    
    @SerializedName("pressure")
    val pressure: Int = 1013
)

data class Weather(
    @SerializedName("main")
    val main: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("icon")
    val icon: String
)

data class Wind(
    @SerializedName("speed")
    val speed: Double
)

fun OpenWeatherResponse.toWeatherData(): WeatherData {
    return WeatherData(
        id = "${cityName}_${System.currentTimeMillis()}",
        cityName = cityName,
        temperature = main.temperature,
        description = weather.firstOrNull()?.description ?: "Clear",
        humidity = main.humidity,
        windSpeed = wind.speed,
        pressure = main.pressure,
        icon = weather.firstOrNull()?.icon ?: "01d"
    )
}

// KmaWeather API Response Models
data class KmaWeatherResponse(
    val response: KmaResponse
)

data class KmaResponse(
    val header: KmaHeader,
    val body: KmaBody?
)

data class KmaHeader(
    val resultCode: String,
    val resultMsg: String
)

data class KmaBody(
    val dataType: String,
    val items: KmaItems
)

data class KmaItems(
    val item: List<KmaWeatherItem>
)

data class KmaWeatherItem(
    val baseDate: String,
    val baseTime: String,
    val category: String,
    val nx: Int,
    val ny: Int,
    val obsrValue: String? = null,
    val fcstValue: String? = null
)