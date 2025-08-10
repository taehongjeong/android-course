package com.example.week4myweather

import android.app.Application
import com.example.week4myweather.model.ApiMode

class WeatherApplication : Application() {
    companion object {
        // API Configuration
        const val OPEN_WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"
        val OPEN_WEATHER_API_KEY: String get() = BuildConfig.OPEN_WEATHER_API_KEY
        const val KMA_WEATHER_BASE_URL = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"
        val KMA_WEATHER_API_KEY: String get() = BuildConfig.KMA_WEATHER_API_KEY
        val DEFAULT_API_MODE = ApiMode.KMA_WEATHER
        const val API_UNITS_METRIC = "metric"
        const val API_LANG_KR = "kr"
        
        // Database Configuration
        const val OLD_DATA_THRESHOLD_DAYS = 7
        
        // Messages
        const val MSG_CITY_NAME_EMPTY = "도시 이름을 입력해주세요."
        const val MSG_CITY_ADDED = "%s 날씨를 추가했습니다."
        const val MSG_CITY_DELETED = "%s이(가) 삭제되었습니다."
        const val MSG_WEATHER_UPDATED = "날씨 정보가 업데이트되었습니다."
        const val MSG_ERROR_UNKNOWN = "알 수 없는 오류가 발생했습니다."
        const val MSG_ERROR_NETWORK = "네트워크 연결을 확인해주세요."

        // Default Cities - 한글과 영어 모두 지원
        val DEFAULT_CITIES = listOf("서울", "부산", "인천")
    }
}