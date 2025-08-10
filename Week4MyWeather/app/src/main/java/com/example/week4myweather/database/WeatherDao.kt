package com.example.week4myweather.database

import androidx.room.*
import com.example.week4myweather.model.WeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    
    @Query("SELECT * FROM weather_table ORDER BY timestamp DESC")
    fun getAllWeatherData(): Flow<List<WeatherData>>
    
    @Query("SELECT * FROM weather_table WHERE cityName = :cityName ORDER BY timestamp DESC LIMIT 1")
    suspend fun getWeatherByCity(cityName: String): WeatherData?
    
    @Query("""
        SELECT * FROM weather_table w1 
        WHERE w1.timestamp = (
            SELECT MAX(w2.timestamp) 
            FROM weather_table w2 
            WHERE w2.cityName = w1.cityName
        ) 
        ORDER BY w1.timestamp DESC
    """)
    fun getLatestWeatherForEachCity(): Flow<List<WeatherData>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherData(weatherData: WeatherData)
    
    @Update
    suspend fun updateWeatherData(weatherData: WeatherData)
    
    @Query("DELETE FROM weather_table WHERE cityName = :cityName")
    suspend fun deleteWeatherByCity(cityName: String)
    
    @Query("DELETE FROM weather_table")
    suspend fun deleteAllWeatherData()
    
    @Query("DELETE FROM weather_table WHERE timestamp < :timeLimit")
    suspend fun deleteOldWeatherData(timeLimit: Long)
    
    @Query("SELECT * FROM weather_table WHERE cityName LIKE '%' || :searchQuery || '%' ORDER BY timestamp DESC")
    fun searchWeatherByCity(searchQuery: String): Flow<List<WeatherData>>
}