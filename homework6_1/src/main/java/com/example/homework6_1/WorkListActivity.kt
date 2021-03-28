package com.example.homework6_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homework6_1.adapter.OnWorkClickListener
import com.example.homework6_1.R
import com.example.homework6_1.adapter.WorkAdapter
import com.example.homework6_1.dao.WorkDAO
import com.example.homework6_1.data.CarDatabase
import com.example.homework6_1.data.Work
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WorkListActivity : AppCompatActivity() {
    private lateinit var workAdapter: WorkAdapter
    private lateinit var buttonAddWork: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var onWorkClickListener: OnWorkClickListener
    private lateinit var buttonBackToCarList: ImageButton
    private lateinit var textTitleCar: TextView
    private lateinit var textTitleNumberCar:TextView
    private lateinit var workDAO: WorkDAO

    private var parentCar: String? = null
    private var carId: Int = 0
    private val carModel = "model"
    private val carNumber = "number"
    private val carProducer = "producer"
    private val statusWork = "status"
    private val carID = "carID"
    private val parentCarName = "parentCarName"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_works)

        recyclerView = findViewById(R.id.recyclerViewWorksAtTheCar)
        buttonAddWork = findViewById(R.id.worksFloatingButtonAddWorks)
        buttonBackToCarList = findViewById(R.id.backButtonWorkListActivity)
        textTitleCar = findViewById(R.id.nameCarWorkListActivity)
        textTitleNumberCar = findViewById(R.id.numberCarWorkListActivity)

        getIntentData(intent)
        getTitleCar()
        workDAO = CarDatabase.getDatabase(this).getWorkDAO()

        buttonAddWork.setOnClickListener {
            Intent(this, AddWork::class.java).apply {
                putExtra(parentCarName, parentCar)
                startActivityForResult(this, 1)
            }
        }

        buttonBackToCarList.setOnClickListener {
            finish()
        }

        onWorkClickListener = object : OnWorkClickListener {
            override fun invoke(work: Work, position: Int) {
                Intent(applicationContext, EditWork::class.java).apply {
                    putExtra(carID, work.id)
                    putExtra(statusWork, work.progressWork)
                    startActivityForResult(this, 2)
                }
            }
        }

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        workAdapter = WorkAdapter(arrayListOf(), onWorkClickListener)
        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = workAdapter
        }
        checkDataBase()
    }

    private fun checkDataBase() {
        val workList = workDAO.getWorkFromParentsCar(parentCar)
        workAdapter.workList = workList as ArrayList<Work>
        workAdapter.notifyDataSetChanged()
    }

    private fun getIntentData(intent: Intent) {
        carId = intent.getIntExtra(carID, 0)
        parentCar = intent.getStringExtra(carModel)
    }

    private fun getTitleCar() {
        val carModel = intent.getStringExtra(carModel)
        val carRegisterNumber = intent.getStringExtra(carNumber)
        val carProducer = intent.getStringExtra(carProducer)
        val titleCarProducerAndModel = "$carProducer $carModel"
        textTitleCar.text = titleCarProducerAndModel
        textTitleNumberCar.text = carRegisterNumber
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        checkDataBase()
    }
}