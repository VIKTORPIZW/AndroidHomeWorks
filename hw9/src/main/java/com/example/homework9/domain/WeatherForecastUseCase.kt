package com.example.homework9.domain

import io.reactivex.Single

interface WeatherForecastUseCase {

    fun getFiveDaysForecast(city: String): Single<List<WeatherInfoDomainModel>>
}