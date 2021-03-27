package by.it.academy.app7_coroutines.activity

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
import by.it.academy.app7_coroutines.R
import by.it.academy.app7_coroutines.adapter.CarWorkAdapter
import by.it.academy.app7_coroutines.entity.CarItem
import by.it.academy.app7_coroutines.entity.WorkItem
import by.it.academy.app7_coroutines.repositories.DatabaseWorksRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


private const val AddWorkRequestCode = 11
private const val EditWorkRequestCode = 22

class CarWorkListActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_work_list)

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
        setSupportActionBar(toolbar)

        buttonBack.setOnClickListener {
            setResult(RESULT_OK, intent)
            finish()
        }
        databaseWorksRepository = DatabaseWorksRepository(applicationContext)
        mainScope = CoroutineScope(Dispatchers.Main + Job())

        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        setValuesInAdapter()

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddWorkActivity::class.java)
            intent.putExtra("carPlate", carItem?.carPlate)
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
            setValuesInAdapter()
        }
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
        carItem = intent.getParcelableExtra("workList")
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
                val intent = Intent(this@CarWorkListActivity, EditWorkActivity::class.java)
                intent.putExtra("workItem", it)
                startActivityForResult(intent, EditWorkRequestCode)
            }
            visibilityOfTextViewNoCars()
        }
    }

    private fun setValuesInAdapter() {
        mainScope.launch {

            workItemAdapter = CarWorkAdapter(databaseWorksRepository.getCarWorkList(carPlate))
            recycleView.adapter = workItemAdapter

            workItemAdapter.onWorkItemClickListener = {
                val intent = Intent(this@CarWorkListActivity, EditWorkActivity::class.java)
                intent.putExtra("workItem", it)
                startActivityForResult(intent, EditWorkRequestCode)
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
}