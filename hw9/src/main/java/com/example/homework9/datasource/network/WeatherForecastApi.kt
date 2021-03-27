package com.example.homework9.datasource.network

import com.example.homework9.datasource.WeatherInfoDataModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherForecastApi {

//    @Headers("X-Api-Key:c0657140a3cfb749f5a3b4103138db8f")
    @GET("data/2.5/forecast")
    fun getFiveDaysForecast(
            @Query("q") city: String,
            @Query("units") units: String,
            @Query("appid") key: String): Single<WeatherInfoDataModel>

}