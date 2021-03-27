package com.example.homework9.forecast

import com.example.homework9.R
import com.example.homework9.domain.WeatherInfoDomainModel

class ForecastItemMapper: (List<WeatherInfoDomainModel>) -> List<ForecastItem> {

    override fun invoke(weatherImfoDataList: List<WeatherInfoDomainModel>): List<ForecastItem> =
            weatherImfoDataList.map { item ->
                ForecastItem(
                        title = item.title,
                        iconId = item.iconId,
                        temperature = "${item.temperature}°",
                        date = item.date,
                        description = item.description,
                        errorImageId = R.drawable.ic_baseline_error_outline_24
                )
            }
}