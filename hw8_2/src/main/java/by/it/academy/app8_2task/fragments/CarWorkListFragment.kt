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
import by.it.academy.app8_2task.adapter.CarWorkAdapter
import by.it.academy.app8_2task.entity.CarItem
import by.it.academy.app8_2task.entity.WorkItem
import by.it.academy.app8_2task.repositories.DatabaseWorksRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CarWorkListFragment : Fragment(R.layout.fragment_car_works_list) {

    private lateinit var workItemAdapter: CarWorkAdapter
    private var carItem: CarItem? = null
    private lateinit var recycleView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var buttonBack: ImageButton
    private lateinit var databaseWorksRepository: DatabaseWorksRepository
    private lateinit var mainScope: CoroutineScope
    private lateinit var textViewNoWorksAdded: TextView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var textViewInToolbarCarProducer: TextView
    private lateinit var textViewInToolbarCarModel: TextView
    private lateinit var textViewInToolbarPlate: TextView
    private lateinit var carPlate: String
    private lateinit var list: Deferred<MutableList<WorkItem>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            recycleView = findViewById(R.id.recyclerViewWorkList)
            buttonBack = findViewById(R.id.buttonBack)
            textViewNoWorksAdded = findViewById(R.id.textViewNoWorksAdded)
            textViewInToolbarCarProducer = findViewById(R.id.textViewInToolbarCarProducer)
            textViewInToolbarCarModel = findViewById(R.id.textViewInToolbarCarModel)
            textViewInToolbarPlate = findViewById(R.id.textViewInToolbarPlate)
            floatingActionButton = findViewById(R.id.floatingActionButtonAddWork)
            toolbar = findViewById(R.id.toolbarCarWorkList)
            workItemAdapter = CarWorkAdapter(mutableListOf())

            setCarInfoInToolbarTextViews()

            buttonBack.setOnClickListener {
                returnToCarListFragment()
            }
            databaseWorksRepository = DatabaseWorksRepository(context)
            mainScope = CoroutineScope(Dispatchers.Main + Job())

            recycleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            setValuesInAdapter()

            floatingActionButton.setOnClickListener {
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    addToBackStack(null)
                    replace(R.id.mainContainer, AddWorkFragment::class.java, bundleOf("carPlate" to carItem?.carPlate, "carItem" to carItem))
                }

            }
            workItemAdapter.onWorkItemClickListener = {

                parentFragmentManager.commit {
                    addToBackStack(null)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    setReorderingAllowed(true)
                    replace(R.id.mainContainer, EditWorkFragment::class.java, bundleOf("workItem" to it, "carItem" to carItem))
                }

            }
        }
    }


    /*
    *
    *
    * Так тут и не смог нормально настроить тулбар... Есть идеи?
    *
    *
    * */

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {

        menuInflater.inflate(R.menu.car_work_list_menu, menu)

        val searchView = menu.findItem(R.id.toolbarSearchWorkList)?.actionView as SearchView
        searchView.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    workItemAdapter.filter.filter(newText)
                    return false
                }

            })
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mainScope.launch {
            when (item.itemId) {
                R.id.menu_sort_by_pending -> list = mainScope.async { databaseWorksRepository.getFilteredCarWorkListByWorkType(carPlate, 1) }

                R.id.menu_sort_by_in_progress -> list = mainScope.async { databaseWorksRepository.getFilteredCarWorkListByWorkType(carPlate, 2) }

                R.id.menu_sort_by_completed -> list = mainScope.async { databaseWorksRepository.getFilteredCarWorkListByWorkType(carPlate, 3) }

                R.id.menu_show_all -> setValuesInAdapter()

            }
            setValuesInAdapterUsingList(list.await())
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setCarInfoInToolbarTextViews() {
        carItem = arguments?.get("workList") as CarItem
        textViewInToolbarCarProducer.text = carItem?.producer ?: ""
        textViewInToolbarCarModel.text = carItem?.carModel ?: ""
        carPlate = carItem?.carPlate ?: ""
        textViewInToolbarPlate.text = carPlate
    }

    private fun setValuesInAdapterUsingList(list: MutableList<WorkItem>) {
        mainScope.launch {
            workItemAdapter = CarWorkAdapter(list)
            recycleView.adapter = workItemAdapter

            workItemAdapter.onWorkItemClickListener = {
                parentFragmentManager.commit {
                    addToBackStack(null)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    setReorderingAllowed(true)
                    replace(R.id.mainContainer, EditWorkFragment::class.java, bundleOf("workItem" to it, "carItem" to carItem))
                }

            }
            visibilityOfTextViewNoCars()
        }
    }

    private fun setValuesInAdapter() {
        mainScope.launch {

            workItemAdapter = CarWorkAdapter(databaseWorksRepository.getCarWorkList(carPlate))
            recycleView.adapter = workItemAdapter

            workItemAdapter.onWorkItemClickListener = {

                parentFragmentManager.commit {
                    addToBackStack(null)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    setReorderingAllowed(true)
                    replace(R.id.mainContainer, EditWorkFragment::class.java, bundleOf("workItem" to it, "carItem" to carItem))
                }
            }
            visibilityOfTextViewNoCars()
        }
    }

    private fun visibilityOfTextViewNoCars() {

        textViewNoWorksAdded.visibility = if (workItemAdapter.itemCount == 0) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun returnToCarListFragment() {
        parentFragmentManager.commit {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            addToBackStack(null)
            setReorderingAllowed(true)
            replace(R.id.mainContainer, CarListFragment())
        }
    }
}