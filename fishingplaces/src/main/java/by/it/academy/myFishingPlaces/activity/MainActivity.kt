package by.it.academy.myFishingPlaces.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.it.academy.myFishingPlaces.R
import by.it.academy.myFishingPlaces.adapter.PlaceAdapter
import by.it.academy.myFishingPlaces.database.DatabasePlaces
import by.it.academy.myFishingPlaces.database.DatabasePlacesDAO
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date


private const val AddCarRequestCode = 1
private const val EditCarRequestCode = 2
private const val openWorkListForTheCar = 3

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fAButtonAddCar: FloatingActionButton
    private lateinit var textViewNoCars: TextView
    private lateinit var placeItemAdapter: PlaceAdapter
    private lateinit var dao: DatabasePlacesDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dao = DatabasePlaces.init(this).getPlaceDatabaseDAO()

        fAButtonAddCar = findViewById(R.id.addPlaceFloatingActionButton)
        textViewNoCars = findViewById(R.id.textViewNoPlacesAdded)

        recyclerView = findViewById(R.id.recyclerViewListOfPlaces)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        checkPlaceDatabase()
        visibilityOfTextViewNoCars()
        writeDateToLogFile()

        fAButtonAddCar.setOnClickListener {
            intent = Intent(this, AddPlaceActivity::class.java)
            startActivityForResult(intent, AddCarRequestCode)
        }

        placeItemAdapter.onEditClickListener = {
            val intent = Intent(this, EditPlaceActivity::class.java)
            intent.putExtra("carItem", it)
            startActivityForResult(intent, EditCarRequestCode)
        }

        placeItemAdapter.onCarItemClickListener = {
            val intent = Intent(this, PlaceDescriptionListActivity::class.java)
            intent.putExtra("workList", it)
            startActivityForResult(intent, openWorkListForTheCar)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (resultCode == RESULT_OK) {

                checkPlaceDatabase()
                visibilityOfTextViewNoCars()

                /*по какой-то причине необходимо переинеацилизировать иначе при повторном нажатии
                * выдаёт exception, мол не проинициализировано*/
                placeItemAdapter.onEditClickListener = {
                    val intent = Intent(this, EditPlaceActivity::class.java)
                    intent.putExtra("carItem", it)
                    startActivityForResult(intent, EditCarRequestCode)
                }
                placeItemAdapter.onCarItemClickListener = {
                    val intent = Intent(this, PlaceDescriptionListActivity::class.java)
                    intent.putExtra("workList", it)
                    startActivityForResult(intent, openWorkListForTheCar)
                }


            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)

        val searchView = menu?.findItem(R.id.toolbarSearch)?.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                placeItemAdapter.filter.filter(newText)
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun checkPlaceDatabase() {
        placeItemAdapter = PlaceAdapter(dao.getAllPlacesSorted())
        recyclerView.adapter = placeItemAdapter
    }

    private fun visibilityOfTextViewNoCars() {
        if (placeItemAdapter.itemCount == 0) {
            textViewNoCars.visibility = VISIBLE
        } else {
            textViewNoCars.visibility = INVISIBLE
        }
    }

    private fun writeDateToLogFile() {
        val dateTimeInstance = SimpleDateFormat.getDateTimeInstance()

        openFileOutput("logWithDate.txt", MODE_APPEND).apply {
            val date = dateTimeInstance.format(Date())
            write(date.toByteArray())
            close()
        }
    }


}