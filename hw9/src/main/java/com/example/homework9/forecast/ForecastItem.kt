package com.example.homework9.forecast

import androidx.annotation.DrawableRes

class ForecastItem(
        val title: String?,
        val iconId: String?,
        val temperature: String?,
        val date: String?,
        val description: String?,
        @DrawableRes val errorImageId: Int
)