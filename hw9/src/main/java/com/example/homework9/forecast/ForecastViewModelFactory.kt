package com.example.homework9.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.homework9.domain.WeatherForecastUseCaseImpl
import io.reactivex.disposables.CompositeDisposable

class ForecastViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ForecastViewModel::class.java)){
            return ForecastViewModel(
                    compositeDisposable = CompositeDisposable(),
                    weatherForecastUseCase = WeatherForecastUseCaseImpl(),
                    mapper = ForecastItemMapper()
            ) as T
        }
        throw IllegalArgumentException(
                "Unknown class fro the view model. Passed ${modelClass.canonicalName} " +
                        "but required ForecastViewModel"
        )
    }
}