package com.example.homework9.forecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homework9.domain.WeatherForecastUseCase
import com.example.homework9.domain.WeatherInfoDomainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class ForecastViewModel(
        private val compositeDisposable: CompositeDisposable,
        private val weatherForecastUseCase: WeatherForecastUseCase,
        private val mapper: (List<WeatherInfoDomainModel>) -> List<ForecastItem>
        ): ViewModel() {

    private val mutableForecastLiveData = MutableLiveData<List<ForecastItem>>()
    val forecastLiveData: LiveData<List<ForecastItem>> = mutableForecastLiveData

    private val mutableForecastErrorLiveData = MutableLiveData<String>()
    val forecastErrorLiveData: LiveData<String> = mutableForecastErrorLiveData

    fun fetchForecastList(city: String?){
        if (city != null) {
            weatherForecastUseCase.getFiveDaysForecast(city)
                    .map { forecastDomainList -> mapper(forecastDomainList) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { forecastList -> Log.d("forecastList",forecastList.size.toString())
                                mutableForecastLiveData.value = forecastList },
                            { error -> Log.d("ERR1", error.toString())
                                mutableForecastErrorLiveData.value = "ERROR"}
                    ).also { compositeDisposable.add(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}