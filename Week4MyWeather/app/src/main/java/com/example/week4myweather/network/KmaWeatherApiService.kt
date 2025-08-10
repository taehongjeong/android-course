package com.example.week4myweather.network

import com.example.week4myweather.model.KmaWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KmaWeatherApiService {
    
    @GET("getUltraSrtNcst")
    suspend fun getCurrentWeather(
        @Query("ServiceKey") serviceKey: String,
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 100,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): Response<KmaWeatherResponse>
    
    companion object {
        val BASE_URL: String
            get() = com.example.week4myweather.WeatherApplication.KMA_WEATHER_BASE_URL
        val API_KEY: String
            get() = com.example.week4myweather.WeatherApplication.KMA_WEATHER_API_KEY
        
        object CityGrid {
            val CITIES = mapOf(
                "Seoul" to Pair(60, 127),
                "서울" to Pair(60, 127),
                "Busan" to Pair(98, 76),
                "부산" to Pair(98, 76),
                "Incheon" to Pair(55, 124),
                "인천" to Pair(55, 124),
                "Daegu" to Pair(89, 90),
                "대구" to Pair(89, 90),
                "Daejeon" to Pair(67, 100),
                "대전" to Pair(67, 100),
                "Gwangju" to Pair(58, 74),
                "광주" to Pair(58, 74),
                "Ulsan" to Pair(102, 84),
                "울산" to Pair(102, 84),
                "Suwon" to Pair(60, 121),
                "수원" to Pair(60, 121),
                "Changwon" to Pair(91, 77),
                "창원" to Pair(91, 77),
                "Seongnam" to Pair(62, 123),
                "성남" to Pair(62, 123),
                "Goyang" to Pair(57, 128),
                "고양" to Pair(57, 128),
                "Yongin" to Pair(64, 119),
                "용인" to Pair(64, 119),
                "Bucheon" to Pair(56, 125),
                "부천" to Pair(56, 125),
                "Ansan" to Pair(52, 121),
                "안산" to Pair(52, 121),
                "Cheongju" to Pair(69, 107),
                "청주" to Pair(69, 107),
                "Jeonju" to Pair(63, 89),
                "전주" to Pair(63, 89),
                "Cheonan" to Pair(63, 110),
                "천안" to Pair(63, 110),
                "Namyangju" to Pair(64, 128),
                "남양주" to Pair(64, 128),
                "Pohang" to Pair(102, 94),
                "포항" to Pair(102, 94),
                "Jeju" to Pair(52, 38),
                "제주" to Pair(52, 38),
                "Gimhae" to Pair(95, 77),
                "김해" to Pair(95, 77),
                "Uijeongbu" to Pair(61, 130),
                "의정부" to Pair(61, 130),
                "Siheung" to Pair(55, 123),
                "시흥" to Pair(55, 123),
                "Gumi" to Pair(84, 96),
                "구미" to Pair(84, 96),
                "Paju" to Pair(56, 131),
                "파주" to Pair(56, 131),
                "Gyeongsan" to Pair(91, 90),
                "경산" to Pair(91, 90),
                "Jinju" to Pair(81, 75),
                "진주" to Pair(81, 75),
                "Wonju" to Pair(91, 114),
                "원주" to Pair(91, 114),
                "Yangsan" to Pair(97, 79),
                "양산" to Pair(97, 79),
                "Asan" to Pair(60, 110),
                "아산" to Pair(60, 110),
                "Gwangmyeong" to Pair(58, 125),
                "광명" to Pair(58, 125),
                "Iksan" to Pair(60, 91),
                "익산" to Pair(60, 91),
                "Chuncheon" to Pair(73, 134),
                "춘천" to Pair(73, 134),
                "Gyeongju" to Pair(100, 91),
                "경주" to Pair(100, 91),
                "Gunpo" to Pair(59, 122),
                "군포" to Pair(59, 122),
                "Gangneung" to Pair(92, 131),
                "강릉" to Pair(92, 131),
                "Mokpo" to Pair(50, 67),
                "목포" to Pair(50, 67),
                "Yeosu" to Pair(73, 66),
                "여수" to Pair(73, 66)
            )
        }
    }
}