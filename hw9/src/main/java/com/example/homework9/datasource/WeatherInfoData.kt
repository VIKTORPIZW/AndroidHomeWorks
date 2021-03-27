package com.example.homework9.datasource

data class WeatherInfoDataModel(
        val cod: String?,
        val list: List<DayForecastModel>,
        val city: City

)

data class DayForecastModel(
        val main: Temperature,
        val weather: List<WeatherModel>,
        val sys: SysModel,
        val dt_txt: String?
)

data class Temperature(
        val temp_min: Double,
        val temp_max: Double
)

data class WeatherModel(
        val main: String?,
        val description: String?,
        val icon: String?
)

data class SysModel(
        val pod: String?
)

data class City(
        val name: String?,
        val country: String?
)