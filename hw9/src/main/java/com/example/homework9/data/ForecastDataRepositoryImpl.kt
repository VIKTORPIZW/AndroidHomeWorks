package com.example.homework9.data

import com.example.homework9.datasource.DayForecastModel
import com.example.homework9.datasource.WeatherDataSource
import com.example.homework9.datasource.WeatherInfoDataModel
import com.example.homework9.datasource.network.ApiController
import com.example.homework9.domain.WeatherInfoDomainModel
import io.reactivex.Single

class ForecastDataRepositoryImpl (
        private val weatherDataSource: WeatherDataSource = ApiController(),
        private val mapper: (List<DayForecastModel>, WeatherInfoDataModel) -> List<WeatherInfoDomainModel> = DaysDomainModelMapper()
        ): ForecastDataRepository {
    override fun getFiveDaysForecast(city: String, units: String, key: String): Single<List<WeatherInfoDomainModel>> =
        weatherDataSource.getFiveDaysForecast(city,units,key)
                .map { dataModel -> mapper(dataModel.list, dataModel) }
}