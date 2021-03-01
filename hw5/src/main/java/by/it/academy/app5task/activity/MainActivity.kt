package by.it.academy.app5task.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.it.academy.app5task.R
import by.it.academy.app5task.adapter.CarAdapter
import by.it.academy.app5task.database.DatabaseCars
import by.it.academy.app5task.database.DatabaseCarsDAO
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
    private lateinit var carItemAdapter: CarAdapter
    private lateinit var dao: DatabaseCarsDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dao = DatabaseCars.init(this).getCarDatabaseDAO()

        fAButtonAddCar = findViewById(R.id.addCarFloatingActionButton)
        textViewNoCars = findViewById(R.id.textViewNoCarsAdded)

        recyclerView = findViewById(R.id.recyclerViewListOfCars)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        checkCarDatabase()
        visibilityOfTextViewNoCars()
        writeDateToLogFile()

        fAButtonAddCar.setOnClickListener {
            intent = Intent(this, AddCarActivity::class.java)
            startActivityForResult(intent, AddCarRequestCode)
        }

        carItemAdapter.onEditClickListener = {
            val intent = Intent(this, EditCarActivity::class.java)
            intent.putExtra("carItem", it)
            startActivityForResult(intent, EditCarRequestCode)
        }

        carItemAdapter.onCarItemClickListener = {
            val intent = Intent(this, CarWorkListActivity::class.java)
            intent.putExtra("workList", it)
            startActivityForResult(intent, openWorkListForTheCar)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (resultCode == RESULT_OK) {

                checkCarDatabase()
                visibilityOfTextViewNoCars()

                /*по какой-то причине необходимо переинеацилизировать иначе при повторном нажатии
                * выдаёт exception, мол не проинициализировано*/
                carItemAdapter.onEditClickListener = {
                    val intent = Intent(this, EditCarActivity::class.java)
                    intent.putExtra("carItem", it)
                    startActivityForResult(intent, EditCarRequestCode)
                }
                carItemAdapter.onCarItemClickListener = {
                    val intent = Intent(this, CarWorkListActivity::class.java)
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
                carItemAdapter.filter.filter(newText)
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun checkCarDatabase() {
        carItemAdapter = CarAdapter(dao.getAllCarsSorted())
        recyclerView.adapter = carItemAdapter
    }

    private fun visibilityOfTextViewNoCars() {
        if (carItemAdapter.itemCount == 0) {
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