package by.it.academy.app8_2task.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.it.academy.app8_2task.R
import by.it.academy.app8_2task.adapter.PlaceAdapter
import by.it.academy.app8_2task.repositories.DatabasePlaceRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PlaceListFragment : Fragment(R.layout.fragment_place_list) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var fAButtonAddCar: FloatingActionButton
    private lateinit var textViewNoCars: TextView
    private var placeItemAdapter = PlaceAdapter(mutableListOf())
    private lateinit var databasePlaceRepository: DatabasePlaceRepository
    private lateinit var mainScope: CoroutineScope
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            databasePlaceRepository = DatabasePlaceRepository(context)
            mainScope = CoroutineScope(Dispatchers.Main + Job())
            fAButtonAddCar = findViewById(R.id.addPlaceFloatingActionButton)
            textViewNoCars = findViewById(R.id.textViewNoPlacesAdded)
            recyclerView = findViewById(R.id.recyclerViewPlaceList)
            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setValuesInAdapter()
            fAButtonAddCar.setOnClickListener {
                parentFragmentManager.commit {
                    addToBackStack(null)
                    setReorderingAllowed(true)
                    replace(R.id.mainContainer, AddPlaceFragment())
                }
            }
        }
    }
    private fun setValuesInAdapter() {
        mainScope.launch {
            placeItemAdapter = PlaceAdapter(databasePlaceRepository.getAllPlacesSortedByLocality())
            recyclerView.adapter = placeItemAdapter
            placeItemAdapter.onEditClickListener = {
                parentFragmentManager.commit {
                    addToBackStack(null)
                    setReorderingAllowed(true)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    replace(R.id.mainContainer, EditPlaceFragment::class.java, bundleOf("placeItem" to it))
                }
            }
            placeItemAdapter.onPlaceItemClickListener = {
                parentFragmentManager.commit {
                    addToBackStack(null)
                    setReorderingAllowed(true)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    replace(R.id.mainContainer, PlaceDescriptionListFragment::class.java, bundleOf("placeDescriptionList" to it))
                }
            }
            visibilityOfTextViewNoPlaces()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_toolbar, menu)
        val searchView = menu.findItem(R.id.toolbarSearch)?.actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                placeItemAdapter.filter.filter(newText)
                return false
            }
        })
    }
    private fun visibilityOfTextViewNoPlaces() {
        if (placeItemAdapter.itemCount == 0) {
            textViewNoCars.visibility = View.VISIBLE
        } else {
            textViewNoCars.visibility = View.INVISIBLE
        }
    }
}