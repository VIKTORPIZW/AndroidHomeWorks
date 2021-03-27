package com.example.homework9.citieslist

import com.example.homework9.datasource.database.CityEntity

class EntityListToItemListMapper: (List<CityEntity>) -> List<CityItem> {
    override fun invoke(entityList: List<CityEntity>): List<CityItem> =
            entityList.map { item ->
                CityItem(
                        city = item.cityName
                )
            }
}