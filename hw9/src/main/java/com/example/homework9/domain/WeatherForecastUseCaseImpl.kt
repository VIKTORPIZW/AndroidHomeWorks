package com.example.homework9.domain

import com.example.homework9.data.ForecastDataRepository
import com.example.homework9.data.ForecastDataRepositoryImpl
import io.reactivex.Single

class WeatherForecastUseCaseImpl(private val  forecastDataRepository: ForecastDataRepository = ForecastDataRepositoryImpl())
    : WeatherForecastUseCase {
    override fun getFiveDaysForecast(city: String): Single<List<WeatherInfoDomainModel>> =
            forecastDataRepository.getFiveDaysForecast(city,"metric","c0657140a3cfb749f5a3b4103138db8f")
                    .map { domainModelList ->
                        domainModelList.filter { item ->
                            item.date?.contains(" 15:00:00", true) == true
                        }
                    }

}