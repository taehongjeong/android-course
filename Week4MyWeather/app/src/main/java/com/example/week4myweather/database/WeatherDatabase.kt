package com.example.week4myweather.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.week4myweather.model.WeatherData

@Database(
    entities = [WeatherData::class],
    version = 2,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    
    companion object {
        private var INSTANCE: WeatherDatabase? = null
        
        fun getDatabase(context: Context): WeatherDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
            }
            return INSTANCE!!
        }
    }
}