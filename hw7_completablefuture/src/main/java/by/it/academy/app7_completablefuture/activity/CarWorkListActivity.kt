package by.it.academy.app7_completablefuture.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.it.academy.app7_completablefuture.R
import by.it.academy.app7_completablefuture.adapter.CarWorkAdapter
import by.it.academy.app7_completablefuture.entity.CarItem
import by.it.academy.app7_completablefuture.repositories.DatabaseWorksRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton


private const val AddWorkRequestCode = 11
private const val EditWorkRequestCode = 22

class CarWorkListActivity : AppCompatActivity() {

    private var workItemAdapter = CarWorkAdapter(mutableListOf())
    private var carItem: CarItem? = null
    private var carPlate: String = ""
    private lateinit var recycleView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var buttonBack: ImageButton

    private lateinit var databaseWorksRepository: DatabaseWorksRepository
    private lateinit var textViewNoWorksAdded: TextView

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var textViewInToolbarCarProducer: TextView
    private lateinit var textViewInToolbarCarModel: TextView
    private lateinit var textViewInToolbarPlate: TextView

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

        databaseWorksRepository = DatabaseWorksRepository(applicationContext)

        setCarInfoInToolbarTextViews()
        setSupportActionBar(toolbar)

        buttonBack.setOnClickListener {
            setResult(RESULT_OK, intent)
            finish()
        }

        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            setValuesInAdapter()
        } else {
            setValuesInAdapterLessThan28Api()
        }

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                setValuesInAdapter()
            } else {
                setValuesInAdapterLessThan28Api()
            }
            workItemAdapter.notifyDataSetChanged()
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


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sort_by_pending ->
                carItem?.carPlate?.let { databaseWorksRepository.getFilteredCarWorkListByWorkType(it, 1) }?.thenAcceptAsync({
                    recycleView.adapter = CarWorkAdapter(it)
                }, mainExecutor)

            R.id.menu_sort_by_in_progress ->
                carItem?.carPlate?.let { databaseWorksRepository.getFilteredCarWorkListByWorkType(it, 2) }?.thenAcceptAsync({
                    recycleView.adapter = CarWorkAdapter(it)
                }, mainExecutor)


            R.id.menu_sort_by_completed ->
                carItem?.carPlate?.let { databaseWorksRepository.getFilteredCarWorkListByWorkType(it, 3) }?.thenAcceptAsync({
                    recycleView.adapter = CarWorkAdapter(it)
                }, mainExecutor)

            R.id.menu_show_all ->
                carItem?.carPlate?.let { databaseWorksRepository.getCarWorkList(it) }?.thenAcceptAsync({
                    recycleView.adapter = CarWorkAdapter(it)
                }, mainExecutor)

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

    private fun setValuesInAdapterLessThan28Api() {
        databaseWorksRepository.getCarWorkList(carPlate).thenAcceptAsync { workList ->
            if (workList.isNotEmpty()) {
                workList.run {
                    workItemAdapter = CarWorkAdapter(workList)

                    recycleView.adapter = workItemAdapter
                    workItemAdapter.notifyDataSetChanged()
                    visibilityOfTextViewNoCars()

                    workItemAdapter.onWorkItemClickListener = {
                        val intent = Intent(this@CarWorkListActivity, EditWorkActivity::class.java)
                        intent.putExtra("workItem", it)
                        startActivityForResult(intent, EditWorkRequestCode)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setValuesInAdapter() {
        databaseWorksRepository.getCarWorkList(carPlate).thenAcceptAsync({ workList ->
            if (workList.isNotEmpty()) {
                workList.run {
                    workItemAdapter = CarWorkAdapter(workList)

                    recycleView.adapter = workItemAdapter
                    workItemAdapter.notifyDataSetChanged()
                    visibilityOfTextViewNoCars()

                    workItemAdapter.onWorkItemClickListener = {
                        val intent = Intent(this@CarWorkListActivity, EditWorkActivity::class.java)
                        intent.putExtra("workItem", it)
                        startActivityForResult(intent, EditWorkRequestCode)
                    }
                }
            }
        }, mainExecutor)
    }

    private fun visibilityOfTextViewNoCars() {

        textViewNoWorksAdded.visibility = if (workItemAdapter.itemCount == 0) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }
}
