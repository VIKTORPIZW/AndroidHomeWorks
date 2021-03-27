package com.example.homework9.forecast

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.homework9.DEFAULT_VALUE
import com.example.homework9.R
import com.example.homework9.citieslist.CitiesListFragment
import com.example.homework9.databinding.FragmentForecastBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date

class ForecastFragment: Fragment(R.layout.fragment_forecast) {

    private lateinit var binding: FragmentForecastBinding
    var viewModelFactory: ViewModelProvider.Factory = ForecastViewModelFactory()
    private lateinit var viewModel: ForecastViewModel
    private var snackbar: Snackbar? = null
    private var markedCity: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentForecastBinding.bind(view)
        binding.currentDate.text = SimpleDateFormat.getDateInstance().format(Date())
        binding.changeCityForecast.setOnClickListener {
            val fragment = CitiesListFragment()
            val fragmentManager = (context as AppCompatActivity?)?.supportFragmentManager
            val fragmentTarns = fragmentManager?.beginTransaction()
            fragmentTarns?.replace(R.id.action_fragment,fragment, "ForecastFragmentTag")
                    ?.addToBackStack(null)
                    ?.commit()
        }

        val args = arguments
        markedCity = args?.getString("CITY")

        viewModel = ViewModelProvider(this,viewModelFactory).get(ForecastViewModel::class.java)
        with(viewModel){
            forecastLiveData.observe(viewLifecycleOwner, Observer { data -> showForecast(data) })
            forecastErrorLiveData.observe(viewLifecycleOwner, Observer { err -> showError(err) })
        }
    }

    private fun showForecast(forecastList: List<ForecastItem>){
        snackbar?.dismiss()
        for (index in forecastList.indices){
            binding.cityCountry.text = forecastList.get(0).title
            binding.degreesCenter.text = forecastList.get(0).temperature.toString()
            binding.weatherDescriptionCenter.text = forecastList.get(0).description
            showIcon(forecastList.get(0),binding.iconWeatherCenter)
            binding.dateBottomFirst.text = forecastList.get(1).date?.split(" ")?.get(0) ?: ""
            binding.degreesBottomFirst.text = forecastList.get(1).temperature.toString()
            binding.weatherDescriptionBottomFirst.text = forecastList.get(1).description
            showIcon(forecastList.get(1),binding.iconWeatherBottomFirst)
            binding.dateBottomSecond.text = forecastList.get(2).date?.split(" ")?.get(0) ?: ""
            binding.degreesBottomSecond.text = forecastList.get(2).temperature.toString()
            binding.weatherDescriptionBottomSecond.text = forecastList.get(2).description
            showIcon(forecastList.get(2),binding.iconWeatherBottomSecond)
            binding.dateBottomThird.text = forecastList.get(3).date?.split(" ")?.get(0) ?: ""
            binding.degreesBottomThird.text = forecastList.get(3).temperature.toString()
            binding.weatherDescriptionBottomThird.text = forecastList.get(3).description
            showIcon(forecastList.get(3),binding.iconWeatherBottomThird)
            binding.dateBottomForth.text = forecastList.get(4).date?.split(" ")?.get(0) ?: ""
            binding.degreesBottomForth.text = forecastList.get(4).temperature.toString()
            binding.weatherDescriptionBottomForth.text = forecastList.get(4).description
            showIcon(forecastList.get(4),binding.iconWeatherBottomForth)
        }
    }

    private fun showIcon(forecastItem: ForecastItem, view: ImageView){
         Glide.with(requireContext())
                .load(forecastItem.iconId)
                .placeholder(forecastItem.errorImageId)
                .centerCrop()
                .into(view)
    }

    private fun showError(errorMessage: String) {
        snackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
                .also { it.show() }
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        markedCity = sharedPref.getString(getString(R.string.marked_city_key), DEFAULT_VALUE)
        viewModel.fetchForecastList(markedCity)
    }


}