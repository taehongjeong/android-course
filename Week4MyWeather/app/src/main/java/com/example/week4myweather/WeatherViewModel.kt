package com.example.week4myweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week4myweather.model.ApiMode
import com.example.week4myweather.model.WeatherData
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {
    
    private val _weatherList = MutableLiveData<List<WeatherData>>(emptyList())
    val weatherList: LiveData<List<WeatherData>> = _weatherList
    
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> = _successMessage
    
    private val _apiMode = MutableLiveData(WeatherApplication.DEFAULT_API_MODE.displayName)
    val apiMode: LiveData<String> = _apiMode
    
    private var allWeatherData = listOf<WeatherData>()
    private var searchQuery = ""
    
    init {
        loadWeatherData()
        observeWeatherData()
        
        viewModelScope.launch {
            repository.cleanupOldData()
        }
    }
    
    private fun observeWeatherData() {
        viewModelScope.launch {
            repository.getLatestWeatherForEachCity().collect { weatherData ->
                allWeatherData = weatherData
                filterWeatherList()
                _isLoading.value = false
            }
        }
    }
    
    private fun filterWeatherList() {
        _weatherList.value = if (searchQuery.isBlank()) {
            allWeatherData
        } else {
            allWeatherData.filter {
                it.cityName.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    fun addCity(cityName: String) {
        if (cityName.isBlank()) {
            _errorMessage.value = WeatherApplication.MSG_CITY_NAME_EMPTY
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            val weatherData = repository.fetchAndSaveWeatherData(cityName.trim())
            
            if (weatherData != null) {
                _successMessage.value = String.format(WeatherApplication.MSG_CITY_ADDED, weatherData.cityName)
            } else {
                _errorMessage.value = WeatherApplication.MSG_ERROR_UNKNOWN
            }
            
            _isLoading.value = false
        }
    }
    
    fun refreshWeatherData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // 현재 도시 목록 저장
            val currentCities = allWeatherData.map { it.cityName }.distinct()
            
            // API 전환 시 기존 데이터 모두 삭제
            repository.deleteAllWeatherData()
            
            // 새 API로 모든 도시 데이터 다시 가져오기
            currentCities.forEach { cityName ->
                repository.fetchAndSaveWeatherData(cityName)
            }
            
            _isLoading.value = false
            _successMessage.value = WeatherApplication.MSG_WEATHER_UPDATED
        }
    }
    
    fun deleteCity(cityName: String) {
        viewModelScope.launch {
            repository.deleteWeatherByCity(cityName)
            _successMessage.value = String.format(WeatherApplication.MSG_CITY_DELETED, cityName)
        }
    }
    
    fun updateSearchQuery(query: String) {
        searchQuery = query
        filterWeatherList()
    }
    
    private fun loadWeatherData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            val defaultCities = WeatherApplication.DEFAULT_CITIES
            
            defaultCities.forEach { city ->
                if (repository.getWeatherByCity(city) == null) {
                    repository.fetchAndSaveWeatherData(city)
                }
            }
            
            _isLoading.value = false
        }
    }
    
    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
    
    fun setApiMode(mode: ApiMode) {
        _apiMode.value = mode.displayName
        repository.apiMode = mode
        // API 모드 변경 시 데이터 새로고침
        refreshWeatherData()
    }
}