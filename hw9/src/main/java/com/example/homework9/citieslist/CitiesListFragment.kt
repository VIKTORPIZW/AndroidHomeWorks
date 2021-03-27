package com.example.homework9.citieslist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homework9.R
import com.example.homework9.databinding.FragmentChooseCityBinding
import com.example.homework9.datasource.database.CityEntity
import com.example.homework9.datasource.database.DataBaseInfo
import com.example.homework9.forecast.ForecastFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CitiesListFragment: Fragment(R.layout.fragment_choose_city), CityItemAdapter.OnItemClickListener {

    private lateinit var binding: FragmentChooseCityBinding
    private lateinit var viewModel: CityViewModel
    private var citiesList = mutableListOf<CityItem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var cityItemAdapter: CityItemAdapter
    private var compositeDisposable =  CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())
                .get(CityViewModel::class.java)

        binding = FragmentChooseCityBinding.bind(view)
        binding.arrowBackChooseCity.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            fragmentManager?.popBackStack()
        }
        binding.addCityFab.setOnClickListener { showDialog() }

        viewModel.cityLiveData.observe(viewLifecycleOwner, Observer {
            data -> citiesList.add(data)
            putCityToDatabase(data)
            cityItemAdapter.notifyDataSetChanged()
        })

        recyclerView = view.findViewById(R.id.recycler_choose_city)
        cityItemAdapter = CityItemAdapter(citiesList,this)
        recyclerView.adapter = cityItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        getAllCities()
    }

    private fun showDialog(){
        val dialog = context?.let { Dialog(it) }
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.custom_dialog_layout)

        dialog?.findViewById<View>(R.id.cancel)?.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.findViewById<View>(R.id.Ok)?.setOnClickListener {
            val city = dialog.findViewById<EditText>(R.id.dialog_edit_text).text.toString()
            viewModel.getCityName(city)
            dialog.dismiss()
        }
        dialog?.show()
    }

    override fun onItemClick(position: Int) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putString(getString(R.string.marked_city_key),citiesList.get(position).city?.split(" ")?.get(0) ?: "")
            apply()
        }
        val fragment = ForecastFragment()
        val fragmentManager = (context as AppCompatActivity?)?.supportFragmentManager
        val fragmentTarns = fragmentManager?.beginTransaction()
        fragmentTarns?.replace(R.id.action_fragment,fragment, "CitiesListFragmentTag")
                ?.addToBackStack(null)
                ?.commit()
    }

    private fun putCityToDatabase(cityItem: CityItem){
        val database = DataBaseInfo.getInstance(requireContext())
        val mapper = CityItemToEntityMapper()
        val cityEntity: CityEntity = mapper(cityItem)
        Single.create<CityEntity>{ emitter ->
                database?.getCityInfoDao()?.insertCityEntity(cityEntity)
                emitter.onSuccess(cityEntity)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun getAllCities(){
        val database = DataBaseInfo.getInstance(requireContext())
        val mapper = EntityListToItemListMapper()
        Single.create<List<CityEntity>>{ emitter ->
           val cityEntityList = (database?.getCityInfoDao()?.getAllCities())
            if (cityEntityList != null) {
                emitter.onSuccess(cityEntityList)
            }
        }.subscribeOn(Schedulers.io())
                .map { items -> mapper(items) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    citiesList.clear()
                    citiesList.addAll(list)
                    if (citiesList.size == 0){
                        showDialog()
                    }
                    cityItemAdapter.notifyDataSetChanged()
                }.also { compositeDisposable.add(it) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}
