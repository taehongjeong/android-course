package com.example.week4myweather

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week4myweather.database.WeatherDatabase
import com.example.week4myweather.model.ApiMode
import com.example.week4myweather.network.NetworkManager
import com.example.week4myweather.databinding.ActivityMainBinding
import com.example.week4myweather.adapter.WeatherAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var weatherAdapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        supportActionBar?.hide()
        
        initViewModel()
        setupRecyclerView()
        setupClickListeners()
        setupSearchFunctionality()
        setupBottomNavigation()
        observeViewModel()
    }
    
    private fun initViewModel() {
        val database = WeatherDatabase.getDatabase(this)
        val repository = WeatherRepository(
            context = applicationContext,  // Use applicationContext to avoid activity leak
            weatherDao = database.weatherDao(),
            openWeatherApi = NetworkManager.openWeatherApiService,
            kmaWeatherApi = NetworkManager.kmaWeatherApiService
        )
        viewModel = WeatherViewModel(repository)
    }

    private fun setupRecyclerView() {
        weatherAdapter = WeatherAdapter { weatherData ->
            viewModel.deleteCity(weatherData.cityName)
        }
        
        binding.rvWeatherList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = weatherAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnAddCity.setOnClickListener {
            val cityName = binding.etCityName.text.toString()
            if (cityName.isNotBlank()) {
                viewModel.addCity(cityName)
                binding.etCityName.text?.clear()
            }
        }
        
        binding.btnRefresh.setOnClickListener {
            viewModel.refreshWeatherData()
        }
        
        binding.etCityName.setOnEditorActionListener { _, _, _ ->
            binding.btnAddCity.performClick()
            true
        }
    }

    private fun setupSearchFunctionality() {
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.updateSearchQuery(text?.toString() ?: "")
        }
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_kma -> {
                    viewModel.setApiMode(ApiMode.KMA_WEATHER)
                    true
                }
                R.id.navigation_openweather -> {
                    viewModel.setApiMode(ApiMode.OPEN_WEATHER)
                    true
                }
                else -> false
            }
        }
        
        binding.bottomNavigation.selectedItemId = when (WeatherApplication.DEFAULT_API_MODE) {
            ApiMode.KMA_WEATHER -> R.id.navigation_kma
            ApiMode.OPEN_WEATHER -> R.id.navigation_openweather
        }
    }

    private fun observeViewModel() {
        viewModel.weatherList.observe(this) { weatherList ->
            weatherAdapter.submitList(weatherList)
            binding.layoutEmpty.isVisible = weatherList.isEmpty()
            binding.rvWeatherList.isVisible = weatherList.isNotEmpty()
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
        
        viewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessages()
            }
        }
        
        viewModel.successMessage.observe(this) { successMessage ->
            successMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessages()
            }
        }
        
        viewModel.apiMode.observe(this) { apiMode ->
            binding.tvApiMode.text = apiMode
        }
    }
}