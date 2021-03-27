package com.example.homework9.data

import com.example.homework9.domain.WeatherInfoDomainModel
import io.reactivex.Single

interface ForecastDataRepository {
    fun getFiveDaysForecast(city: String, units: String, key: String): Single <List<WeatherInfoDomainModel>>
}