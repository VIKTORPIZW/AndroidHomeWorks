package by.it.academy.app5task.activity

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
import by.it.academy.app5task.R
import by.it.academy.app5task.adapter.CarWorkAdapter
import by.it.academy.app5task.database.DatabaseCars
import by.it.academy.app5task.database.WorkItemDAO
import by.it.academy.app5task.entity.CarItem
import by.it.academy.app5task.entity.WorkItem
import com.google.android.material.floatingactionbutton.FloatingActionButton


private const val AddWorkRequestCode = 11
private const val EditWorkRequestCode = 22

class CarWorkListActivity : AppCompatActivity() {

    private lateinit var workItemAdapter: CarWorkAdapter
    private lateinit var carItem: CarItem
    private lateinit var recycleView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var buttonBack: ImageButton
    private lateinit var dao: WorkItemDAO
    private lateinit var textViewNoWorksAdded: TextView

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var textViewInToolbarCarProducer: TextView
    private lateinit var textViewInToolbarCarModel: TextView
    private lateinit var textViewInToolbarPlate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_work_list)

        setFindViewByIds()
        getIntentExtra()
        setSupportActionBar(toolbar)

        buttonBack.setOnClickListener {
            setResult(RESULT_OK, intent)
            finish()
        }

        dao = DatabaseCars.init(this).getWorkListDatabaseDAO()
        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        checkDataBase(dao.getCarWorkList(carItem.carPlate))

        visibilityOfTextViewNoCars()


        floatingActionButton.setOnClickListener {
            intent = Intent(this, AddWorkActivity::class.java)
            intent.putExtra("carPlate", carItem.carPlate)
            startActivityForResult(intent, AddWorkRequestCode)
        }
        workItemAdapter.onWorkItemClickListener = {
            val intent = Intent(this, EditWorkActivity::class.java)
            intent.putExtra("workItem", it)
            startActivityForResult(intent, EditWorkRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            checkDataBase(dao.getCarWorkList(carItem.carPlate))

            workItemAdapter.onWorkItemClickListener = {
                val intent = Intent(this, EditWorkActivity::class.java)
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
                checkDataBase(dao.getFilteredCarWorkListByWorkType(carItem.carPlate, 1))
            }
            R.id.menu_sort_by_in_progress -> {
                checkDataBase(dao.getFilteredCarWorkListByWorkType(carItem.carPlate, 2))
            }
            R.id.menu_sort_by_completed -> {
                checkDataBase(dao.getFilteredCarWorkListByWorkType(carItem.carPlate, 3))
            }
            R.id.menu_show_all -> {
                checkDataBase(dao.getCarWorkList(carItem.carPlate))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getIntentExtra() {
        carItem = intent.getParcelableExtra("workList") ?: CarItem("", "", "", "", "")
        with(carItem) {
            textViewInToolbarCarProducer.text = producer
            textViewInToolbarCarModel.text = carModel
            textViewInToolbarPlate.text = carPlate
        }
    }

    private fun setFindViewByIds() {
        recycleView = findViewById(R.id.recyclerViewWorkList)
        buttonBack = findViewById(R.id.buttonBack)
        textViewNoWorksAdded = findViewById(R.id.textViewNoWorksAdded)
        textViewInToolbarCarProducer = findViewById(R.id.textViewInToolbarCarProducer)
        textViewInToolbarCarModel = findViewById(R.id.textViewInToolbarCarModel)
        textViewInToolbarPlate = findViewById(R.id.textViewInToolbarPlate)
        floatingActionButton = findViewById(R.id.floatingActionButtonAddWork)
        toolbar = findViewById(R.id.toolbarCarWorkList)
    }

    private fun checkDataBase(list: MutableList<WorkItem>) {
        workItemAdapter = CarWorkAdapter(list)
        recycleView.adapter = workItemAdapter

    }

    private fun visibilityOfTextViewNoCars() {
        if (workItemAdapter.itemCount == 0) {
            textViewNoWorksAdded.visibility = View.VISIBLE
        } else {
            textViewNoWorksAdded.visibility = View.INVISIBLE
        }
    }
}