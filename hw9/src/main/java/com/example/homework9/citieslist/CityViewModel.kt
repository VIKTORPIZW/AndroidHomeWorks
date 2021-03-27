package com.example.homework9.citieslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homework9.domain.WeatherForecastUseCase
import com.example.homework9.domain.WeatherForecastUseCaseImpl
import com.example.homework9.domain.WeatherInfoDomainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class CityViewModel(
        private val compositeDisposable: CompositeDisposable = CompositeDisposable(),
        private val weatherForecastUseCase: WeatherForecastUseCase = WeatherForecastUseCaseImpl(),
        private val mapper: (List<WeatherInfoDomainModel>) -> CityItem = CityItemMapper()
): ViewModel() {


    private val mutableCityLiveData = MutableLiveData<CityItem>()
    val cityLiveData: LiveData<CityItem> = mutableCityLiveData

    private val mutableCityErrorLiveData = MutableLiveData<String>()
    val cityErrorLiveData: LiveData<String> = mutableCityErrorLiveData

    fun getCityName(city: String){
        weatherForecastUseCase.getFiveDaysForecast(city)
                .map { forecastDomainList -> mapper(forecastDomainList) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {item -> mutableCityLiveData.value = item},
                        { error -> Log.d("ERR1", error.toString())
                            mutableCityErrorLiveData.value = "ERROR"}
                ).also { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


}