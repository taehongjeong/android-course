package com.example.week4myweather.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    
    fun createApiService(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val openWeatherApiService: OpenWeatherApiService by lazy {
        createApiService(OpenWeatherApiService.BASE_URL)
            .create(OpenWeatherApiService::class.java)
    }
    
    val kmaWeatherApiService: KmaWeatherApiService by lazy {
        createApiService(KmaWeatherApiService.BASE_URL)
            .create(KmaWeatherApiService::class.java)
    }
}