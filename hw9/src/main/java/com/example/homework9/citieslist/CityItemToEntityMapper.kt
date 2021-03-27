package com.example.homework9.citieslist

import com.example.homework9.datasource.database.CityEntity

class CityItemToEntityMapper: (CityItem) -> CityEntity {
    override fun invoke(cityItem: CityItem): CityEntity =
            CityEntity(
                    id = 0,
                    cityName = cityItem.city
            )
    }
