package com.example.homework9.citieslist

import com.example.homework9.domain.WeatherInfoDomainModel

class CityItemMapper: (List<WeatherInfoDomainModel>) -> CityItem {
    override fun invoke(weatherInfoList: List<WeatherInfoDomainModel>): CityItem {
        var title: String? = null
        for (index in weatherInfoList.indices){
            title = weatherInfoList.get(index).title
        }
        return CityItem(
                city = title
        )
    }

}