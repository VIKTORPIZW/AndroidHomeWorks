package by.it.academy.app7_rxjava.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.it.academy.app7_rxjava.R
import by.it.academy.app7_rxjava.adapter.CarAdapter
import by.it.academy.app7_rxjava.repositories.DatabaseCarsRepository
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

    private lateinit var databaseCarsRepository: DatabaseCarsRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseCarsRepository = DatabaseCarsRepository(applicationContext)

        fAButtonAddCar = findViewById(R.id.addCarFloatingActionButton)
        textViewNoCars = findViewById(R.id.textViewNoCarsAdded)

        recyclerView = findViewById(R.id.recyclerViewCarsList)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        setValuesInAdapter()
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

        if (resultCode == RESULT_OK) {
            setValuesInAdapter()

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

    private fun setValuesInAdapter() {
        carItemAdapter = CarAdapter(mutableListOf())

        databaseCarsRepository.getAllCarsSortedByProducer()?.subscribe{

            list -> carItemAdapter = CarAdapter(list)
            recyclerView.adapter = carItemAdapter

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
            visibilityOfTextViewNoCars()

        }
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