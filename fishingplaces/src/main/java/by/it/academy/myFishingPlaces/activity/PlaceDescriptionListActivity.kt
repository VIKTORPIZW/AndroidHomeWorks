package by.it.academy.myFishingPlaces.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.it.academy.myFishingPlaces.R
import by.it.academy.myFishingPlaces.adapter.PlaceDescriptionAdapter
import by.it.academy.myFishingPlaces.database.DatabasePlaces
import by.it.academy.myFishingPlaces.database.PlaceDescriptionItemDAO
import by.it.academy.myFishingPlaces.entity.PlaceItem
import by.it.academy.myFishingPlaces.entity.PlaceDescriptionItem
import com.google.android.material.floatingactionbutton.FloatingActionButton


private const val AddWorkRequestCode = 11
private const val EditWorkRequestCode = 22

class PlaceDescriptionListActivity : AppCompatActivity() {

    private lateinit var workItemAdapter: PlaceDescriptionAdapter
    private lateinit var placeItem: PlaceItem
    private lateinit var recycleView: RecyclerView
    private lateinit var floatingActionButtonAddPlaceDescription: FloatingActionButton
    private lateinit var buttonBack: ImageButton
    private lateinit var dao: PlaceDescriptionItemDAO
    private lateinit var textViewNoPlaceDescriptionAdded: TextView

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var textViewInToolbarPlaceLocality: TextView
    private lateinit var textViewInToolbarPlaceFishingType: TextView
    private lateinit var textViewInToolbarPlate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_description_list)

        setFindViewByIds()
        getIntentExtra()
        setSupportActionBar(toolbar)

        buttonBack.setOnClickListener {
            setResult(RESULT_OK, intent)
            finish()
        }

        dao = DatabasePlaces.init(this).getPlaceDescriptionListDatabaseDAO()
        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        checkDataBase(dao.getPlaceDescriptionList(placeItem.fishSpecies))

        visibilityOfTextViewNoCars()


        floatingActionButtonAddPlaceDescription.setOnClickListener {
            intent = Intent(this, AddPlaceDescriptionActivity::class.java)
            intent.putExtra("carPlate", placeItem.fishSpecies)
            startActivityForResult(intent, AddWorkRequestCode)
        }
        workItemAdapter.onFishDescriptionItemClickListener = {
            val intent = Intent(this, EditPlaceDescriptionActivity::class.java)
            intent.putExtra("placeDescriptionItem", it)
            startActivityForResult(intent, EditWorkRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            checkDataBase(dao.getPlaceDescriptionList(placeItem.fishSpecies))

            workItemAdapter.onFishDescriptionItemClickListener = {
                val intent = Intent(this, EditPlaceDescriptionActivity::class.java)
                intent.putExtra("workItem", it)
                startActivityForResult(intent, EditWorkRequestCode)
            }
        }
        visibilityOfTextViewNoCars()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.car_work_list_menu, menu)

        val searchView = menu?.findItem(R.id.toolbarSearchWorkList)?.actionView as SearchView
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
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sort_by_pending -> {
                checkDataBase(dao.getFilteredPlaceDescriptionListByPlaceType(placeItem.fishSpecies, 1))
            }
            R.id.menu_sort_by_in_progress -> {
                checkDataBase(dao.getFilteredPlaceDescriptionListByPlaceType(placeItem.fishSpecies, 2))
            }
            R.id.menu_sort_by_completed -> {
                checkDataBase(dao.getFilteredPlaceDescriptionListByPlaceType(placeItem.fishSpecies, 3))
            }
            R.id.menu_show_all -> {
                checkDataBase(dao.getPlaceDescriptionList(placeItem.fishSpecies))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getIntentExtra() {
        placeItem = intent.getParcelableExtra("workList") ?: PlaceItem("", "", "", "", "")
        with(placeItem) {
            textViewInToolbarPlaceLocality.text = locality
            textViewInToolbarPlaceFishingType.text = fishingType
            textViewInToolbarPlate.text = fishSpecies
        }
    }

    private fun setFindViewByIds() {
        recycleView = findViewById(R.id.recyclerViewPlaceDescriptionList)
        buttonBack = findViewById(R.id.buttonBack)
        textViewNoPlaceDescriptionAdded = findViewById(R.id.textViewNoPlaceDescriptionAdded)
        textViewInToolbarPlaceLocality = findViewById(R.id.textViewInToolbarPlaceLocality)
        textViewInToolbarPlaceFishingType = findViewById(R.id.textViewInToolbarPlaceFishingType)
        textViewInToolbarPlate = findViewById(R.id.textViewInToolbarPlate)
        floatingActionButtonAddPlaceDescription = findViewById(R.id.floatingActionButtonAddPlaceDescription)
        toolbar = findViewById(R.id.toolbarPlaceDescriptionList)
    }

    private fun checkDataBase(list: MutableList<PlaceDescriptionItem>) {
        workItemAdapter = PlaceDescriptionAdapter(list)
        recycleView.adapter = workItemAdapter

    }

    private fun visibilityOfTextViewNoCars() {
        if (workItemAdapter.itemCount == 0) {
            textViewNoPlaceDescriptionAdded.visibility = View.VISIBLE
        } else {
            textViewNoPlaceDescriptionAdded.visibility = View.INVISIBLE
        }
    }
}