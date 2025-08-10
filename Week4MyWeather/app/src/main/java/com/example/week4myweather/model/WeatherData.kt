package com.example.week4myweather.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Entity(tableName = "weather_table")
@Parcelize
data class WeatherData(
    @PrimaryKey
    val id: String,
    val cityName: String,
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int = 1013,
    val icon: String,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable