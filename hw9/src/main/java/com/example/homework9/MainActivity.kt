package com.example.homework9

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.homework9.citieslist.CitiesListFragment
import com.example.homework9.forecast.ForecastFragment

const val DEFAULT_VALUE = "NO_CITY"

class MainActivity : AppCompatActivity() {

    private var markedCity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        markedCity = sharedPref.getString(getString(R.string.marked_city_key), DEFAULT_VALUE)

        if (markedCity == DEFAULT_VALUE){
            supportFragmentManager.commit{
                setReorderingAllowed(true)
                add<CitiesListFragment>(R.id.action_fragment)
            }
        }else{
            val bundle = Bundle()
            val fragment = ForecastFragment()
            bundle.putString("CITY",markedCity)
            fragment.arguments = bundle
            val fragmentManager = this.supportFragmentManager
            val fragmentTarns = fragmentManager.beginTransaction()
            fragmentTarns.replace(R.id.action_fragment,fragment)
                    .commit()
        }
    }
}