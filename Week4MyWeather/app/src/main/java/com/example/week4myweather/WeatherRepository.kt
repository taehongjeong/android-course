package com.example.week4myweather

import android.content.Context
import android.util.Log
import com.example.week4myweather.database.WeatherDao
import com.example.week4myweather.model.ApiMode
import com.example.week4myweather.model.WeatherData
import com.example.week4myweather.model.toWeatherData
import com.example.week4myweather.network.OpenWeatherApiService
import com.example.week4myweather.network.KmaWeatherApiService
import com.example.week4myweather.util.NetworkUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WeatherRepository(
    private val context: Context,
    private val weatherDao: WeatherDao,
    private val openWeatherApi: OpenWeatherApiService,
    private val kmaWeatherApi: KmaWeatherApiService? = null
) {
    var apiMode: ApiMode = WeatherApplication.DEFAULT_API_MODE
    
    companion object {
        private const val TAG = "WeatherRepository"
    }
    
    fun getLatestWeatherForEachCity(): Flow<List<WeatherData>> {
        return weatherDao.getLatestWeatherForEachCity()
    }
    
    suspend fun fetchAndSaveWeatherData(cityName: String): WeatherData? {
        return withContext(Dispatchers.IO) {
            if (!NetworkUtil.isNetworkAvailable(context)) {
                Log.e(TAG, "Network not available")
                return@withContext null
            }
            
            Log.d(TAG, "Fetching weather for $cityName using $apiMode")
            
            val weatherData = when (apiMode) {
                ApiMode.KMA_WEATHER -> {
                    Log.d(TAG, "Using KMA API for $cityName")
                    fetchWeatherFromKma(cityName)
                }
                ApiMode.OPEN_WEATHER -> {
                    Log.d(TAG, "Using OpenWeather API for $cityName")
                    fetchWeatherFromOpenWeather(cityName)
                }
            }
            
            weatherData?.let {
                Log.d(TAG, "Saving weather data: ${it.id}, ${it.description}, temp: ${it.temperature}")
                weatherDao.insertWeatherData(it)
            }
            
            weatherData
        }
    }
    
    private suspend fun fetchWeatherFromOpenWeather(cityName: String): WeatherData? {
        return try {
            // 한글 도시명을 영어로 변환 (간단한 매핑)
            val englishCityName = when(cityName) {
                "서울" -> "Seoul"
                "부산" -> "Busan"
                "인천" -> "Incheon"
                "대구" -> "Daegu"
                "대전" -> "Daejeon"
                "광주" -> "Gwangju"
                "울산" -> "Ulsan"
                "수원" -> "Suwon"
                "창원" -> "Changwon"
                "성남" -> "Seongnam"
                else -> cityName // 이미 영어이거나 매핑이 없는 경우 그대로 사용
            }
            
            val response = openWeatherApi.getCurrentWeather(
                cityName = englishCityName,
                apiKey = OpenWeatherApiService.API_KEY
            )
            
            if (response.isSuccessful) {
                val data = response.body()?.toWeatherData()?.copy(
                    id = "${apiMode.name}_${cityName}_${System.currentTimeMillis()}",
                    cityName = cityName // 원래 입력된 도시명 유지
                )
                Log.d(TAG, "OpenWeather response: ${data?.description}, temp: ${data?.temperature}")
                data
            } else {
                Log.e(TAG, "OpenWeather API error: ${response.code()} for city: $englishCityName")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "OpenWeather exception for city: $cityName", e)
            null
        }
    }
    
    private suspend fun fetchWeatherFromKma(cityName: String): WeatherData? {
        if (kmaWeatherApi == null) {
            Log.w(TAG, "KMA API service is null")
            return null
        }
        
        val gridCoords = KmaWeatherApiService.Companion.CityGrid.CITIES[cityName]
        if (gridCoords == null) {
            Log.w(TAG, "No grid coordinates for city: $cityName")
            return null
        }
        
        val (nx, ny) = gridCoords
        val (baseDate, baseTime) = getCurrentDateTimeForKma()
        
        Log.d(TAG, "KMA request: city=$cityName, nx=$nx, ny=$ny, date=$baseDate, time=$baseTime")
        
        return try {
            val response = kmaWeatherApi.getCurrentWeather(
                serviceKey = KmaWeatherApiService.API_KEY,
                baseDate = baseDate,
                baseTime = baseTime,
                nx = nx,
                ny = ny
            )
            
            if (response.isSuccessful) {
                val kmaResponse = response.body()
                Log.d(TAG, "KMA raw response received")
                
                kmaResponse?.let {
                    val parsed = parseKmaResponse(it, cityName)
                    Log.d(TAG, "KMA parsed: ${parsed?.description}, temp: ${parsed?.temperature}")
                    parsed
                }
            } else {
                Log.e(TAG, "KMA API error: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "KMA exception", e)
            null
        }
    }
    
    private fun getCurrentDateTimeForKma(): Pair<String, String> {
        val now = Calendar.getInstance(Locale.KOREA)
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        val baseDate = dateFormat.format(now.time)
        
        val hour = now.get(Calendar.HOUR_OF_DAY)
        val minute = now.get(Calendar.MINUTE)
        
        val baseHour = if (minute < 40) {
            if (hour == 0) 23 else hour - 1
        } else {
            hour
        }
        
        val baseTime = String.format(Locale.getDefault(), "%02d00", baseHour)
        
        return Pair(baseDate, baseTime)
    }
    
    private fun parseKmaResponse(response: com.example.week4myweather.model.KmaWeatherResponse, cityName: String): WeatherData? {
        val items = response.response.body?.items?.item ?: return null
        
        var temperature = 0.0
        var humidity = 0
        var windSpeed = 0.0
        var skyStatus = "1"  // 기본값: 맑음
        var precipitationType = "0"  // 기본값: 강수없음
        
        Log.d(TAG, "KMA items count: ${items.size}")
        
        items.forEach { item ->
            when (item.category) {
                "T1H", "TMP" -> {
                    val temp = item.obsrValue?.toDoubleOrNull() ?: item.fcstValue?.toDoubleOrNull()
                    if (temp != null) {
                        temperature = temp
                        Log.d(TAG, "KMA temperature: $temperature")
                    }
                }
                "REH" -> {
                    val hum = item.obsrValue?.toIntOrNull() ?: item.fcstValue?.toIntOrNull()
                    if (hum != null) {
                        humidity = hum
                        Log.d(TAG, "KMA humidity: $humidity")
                    }
                }
                "WSD" -> {
                    val wind = item.obsrValue?.toDoubleOrNull() ?: item.fcstValue?.toDoubleOrNull()
                    if (wind != null) {
                        windSpeed = wind
                        Log.d(TAG, "KMA windSpeed: $windSpeed")
                    }
                }
                "SKY" -> {
                    val value = item.obsrValue ?: item.fcstValue
                    if (!value.isNullOrEmpty()) {
                        skyStatus = value
                        Log.d(TAG, "KMA sky: $skyStatus")
                    }
                }
                "PTY" -> {
                    val value = item.obsrValue ?: item.fcstValue
                    if (!value.isNullOrEmpty()) {
                        precipitationType = value
                        Log.d(TAG, "KMA precipitation: $precipitationType")
                    }
                }
            }
        }
        
        val description = getWeatherDescription(skyStatus, precipitationType)
        val icon = getWeatherIcon(skyStatus, precipitationType)
        
        Log.d(TAG, "KMA final: desc=$description, temp=$temperature")
        
        return WeatherData(
            id = "${apiMode.name}_${cityName}_${System.currentTimeMillis()}",
            cityName = cityName,
            temperature = temperature,
            description = description,
            humidity = humidity,
            windSpeed = windSpeed,
            pressure = 1013,
            icon = icon,
            timestamp = System.currentTimeMillis()
        )
    }
    
    private fun getWeatherDescription(sky: String, precipitation: String): String {
        return when (precipitation) {
            "0" -> when (sky) {
                "1" -> "맑음"
                "3" -> "구름많음"
                "4" -> "흐림"
                else -> "알 수 없음"
            }
            "1" -> "비"
            "2" -> "비/눈"
            "3" -> "눈"
            "4" -> "소나기"
            "5" -> "빗방울"
            "6" -> "빗방울/눈날림"
            "7" -> "눈날림"
            else -> "알 수 없음"
        }
    }
    
    private fun getWeatherIcon(sky: String, precipitation: String): String {
        return when {
            precipitation != "0" -> when (precipitation) {
                "1", "4", "5" -> "10d"
                "3", "7" -> "13d"
                else -> "09d"
            }
            else -> when (sky) {
                "1" -> "01d"
                "3" -> "02d"
                "4" -> "03d"
                else -> "01d"
            }
        }
    }
    
    suspend fun getWeatherByCity(cityName: String): WeatherData? {
        return withContext(Dispatchers.IO) {
            weatherDao.getWeatherByCity(cityName)
        }
    }
    
    suspend fun deleteWeatherByCity(cityName: String) {
        withContext(Dispatchers.IO) {
            weatherDao.deleteWeatherByCity(cityName)
        }
    }
    
    suspend fun deleteAllWeatherData() {
        withContext(Dispatchers.IO) {
            weatherDao.deleteAllWeatherData()
        }
    }
    
    suspend fun cleanupOldData() {
        withContext(Dispatchers.IO) {
            val sevenDaysAgo = System.currentTimeMillis() - (WeatherApplication.OLD_DATA_THRESHOLD_DAYS * 24 * 60 * 60 * 1000L)
            weatherDao.deleteOldWeatherData(sevenDaysAgo)
        }
    }
}