package com.example.homework9.datasource.network

import com.example.homework9.datasource.WeatherDataSource
import com.example.homework9.datasource.WeatherInfoDataModel
import com.example.homework9.datasource.network.ApiController.RetrofitHolder.retrofit
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiController: WeatherDataSource {
    override fun getFiveDaysForecast(city: String, units: String, key: String): Single<WeatherInfoDataModel> =
          retrofit.create(WeatherForecastApi::class.java).getFiveDaysForecast(city,units,key)
                .subscribeOn(Schedulers.io())

    private object RetrofitHolder{
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}