package by.it.academy.app8_2task.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.it.academy.app8_2task.R
import by.it.academy.app8_2task.adapter.PlaceDescriptionAdapter
import by.it.academy.app8_2task.entity.PlaceItem
import by.it.academy.app8_2task.entity.PlaceDescriptionItem
import by.it.academy.app8_2task.repositories.DatabasePlaceDescriptionRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PlaceDescriptionListFragment : Fragment(R.layout.fragment_place_description_list) {

    private lateinit var placeDescriptionItemAdapter: PlaceDescriptionAdapter
    private var placeItem: PlaceItem? = null
    private lateinit var recycleView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var buttonBack: ImageButton
    private lateinit var databasePlaceDescriptionRepository: DatabasePlaceDescriptionRepository
    private lateinit var mainScope: CoroutineScope
    private lateinit var textViewNoPlaceDescriptionsAdded: TextView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var textViewInToolbarLocality: TextView
    private lateinit var textViewInToolbarDistance: TextView
    private lateinit var textViewInToolbarFishingType: TextView
    private lateinit var fishSpecies: String
    private lateinit var list: Deferred<MutableList<PlaceDescriptionItem>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            recycleView = findViewById(R.id.recyclerViewPlaceDescriptionList)
            buttonBack = findViewById(R.id.buttonBack)
            textViewNoPlaceDescriptionsAdded = findViewById(R.id.textViewNoPlaceDescriptionsAdded)
            textViewInToolbarLocality = findViewById(R.id.textViewInToolbarLocality)
            textViewInToolbarFishingType = findViewById(R.id.textViewInToolbarFishingType)
            textViewInToolbarDistance= findViewById(R.id.textViewInToolbarDistance)
            floatingActionButton = findViewById(R.id.floatingActionButtonAddPlaceDescription)
            toolbar = findViewById(R.id.toolbarPlaceDescriptionList)
            placeDescriptionItemAdapter = PlaceDescriptionAdapter(mutableListOf())

            setPlaceInfoInToolbarTextViews()

            buttonBack.setOnClickListener {
                returnToPlaceListFragment()
            }
            databasePlaceDescriptionRepository = DatabasePlaceDescriptionRepository(context)
            mainScope = CoroutineScope(Dispatchers.Main + Job())

            recycleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            setValuesInAdapter()

            floatingActionButton.setOnClickListener {
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    addToBackStack(null)
                    replace(R.id.mainContainer, AddPlaceDescriptionFragment::class.java, bundleOf("placePlate" to placeItem?.fishSpecies, "placeItem" to placeItem))
                }

            }
            placeDescriptionItemAdapter.onPlaceDescriptionItemClickListener = {

                parentFragmentManager.commit {
                    addToBackStack(null)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    setReorderingAllowed(true)
                    replace(R.id.mainContainer, EditPlaceDescriptionFragment::class.java, bundleOf("placeDescriptionItem" to it, "placeItem" to placeItem))
                }

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {

        menuInflater.inflate(R.menu.place_description_list_menu, menu)

        val searchView = menu.findItem(R.id.toolbarSearchPlaceDescriptionList)?.actionView as SearchView
        searchView.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    placeDescriptionItemAdapter.filter.filter(newText)
                    return false
                }

            })
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mainScope.launch {
            when (item.itemId) {
                R.id.menu_sort_by_bad_bite -> list = mainScope.async { databasePlaceDescriptionRepository.getFilteredPlaceDescriptionListByPlaceDescriptionType(fishSpecies, 1) }

                R.id.menu_sort_by_normal_bite -> list = mainScope.async { databasePlaceDescriptionRepository.getFilteredPlaceDescriptionListByPlaceDescriptionType(fishSpecies, 2) }

                R.id.menu_sort_by_good_bite -> list = mainScope.async { databasePlaceDescriptionRepository.getFilteredPlaceDescriptionListByPlaceDescriptionType(fishSpecies, 3) }

                R.id.menu_show_all -> setValuesInAdapter()

            }
            setValuesInAdapterUsingList(list.await())
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setPlaceInfoInToolbarTextViews() {
        placeItem = arguments?.get("placeDescriptionList") as PlaceItem
        textViewInToolbarLocality.text = placeItem?.locality ?: ""
        textViewInToolbarDistance.text = placeItem?.fishingType ?: ""
        fishSpecies = placeItem?.fishSpecies ?: ""
        textViewInToolbarFishingType.text = fishSpecies
    }

    private fun setValuesInAdapterUsingList(list: MutableList<PlaceDescriptionItem>) {
        mainScope.launch {
            placeDescriptionItemAdapter = PlaceDescriptionAdapter(list)
            recycleView.adapter = placeDescriptionItemAdapter

            placeDescriptionItemAdapter.onPlaceDescriptionItemClickListener = {
                parentFragmentManager.commit {
                    addToBackStack(null)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    setReorderingAllowed(true)
                    replace(R.id.mainContainer, EditPlaceDescriptionFragment::class.java, bundleOf("placeDescriptionItem" to it, "placeItem" to placeItem))
                }

            }
            visibilityOfTextViewNoPlaces()
        }
    }

    private fun setValuesInAdapter() {
        mainScope.launch {

            placeDescriptionItemAdapter = PlaceDescriptionAdapter(databasePlaceDescriptionRepository.getPlaceDescriptionList(fishSpecies))
            recycleView.adapter = placeDescriptionItemAdapter

            placeDescriptionItemAdapter.onPlaceDescriptionItemClickListener = {

                parentFragmentManager.commit {
                    addToBackStack(null)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    setReorderingAllowed(true)
                    replace(R.id.mainContainer, EditPlaceDescriptionFragment::class.java, bundleOf("placeDescriptionItem" to it, "placeItem" to placeItem))
                }
            }
            visibilityOfTextViewNoPlaces()
        }
    }

    private fun visibilityOfTextViewNoPlaces() {

        textViewNoPlaceDescriptionsAdded.visibility = if (placeDescriptionItemAdapter.itemCount == 0) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun returnToPlaceListFragment() {
        parentFragmentManager.commit {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            addToBackStack(null)
            setReorderingAllowed(true)
            replace(R.id.mainContainer, PlaceListFragment())
        }
    }
}