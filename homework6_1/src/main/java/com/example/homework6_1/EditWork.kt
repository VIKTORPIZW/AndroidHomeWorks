package com.example.homework6_1

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.homework6_1.dao.WorkDAO
import com.example.homework6_1.data.CarDatabase
import com.example.homework6_1.data.Work

class EditWork : AppCompatActivity() {
    private lateinit var editWorkName: EditText
    private lateinit var editCost: EditText
    private lateinit var editDescription: EditText
    private lateinit var buttonApplyToEditWork: ImageButton
    private lateinit var buttonBackToWorkList: ImageButton
    private lateinit var buttonDeleteWork: ImageButton
    private lateinit var dateWork: TextView
    private lateinit var textWorkNameTitle: TextView
    private lateinit var imageStatusWorkPending: ImageView
    private lateinit var imageStatusWorkInProgress: ImageView
    private lateinit var imageStatusWorkCompleted: ImageView
    private lateinit var textStatusWorkPending: TextView
    private lateinit var textStatusWorkInProgress: TextView
    private lateinit var textStatusWorkCompleted: TextView
    private lateinit var workDAO: WorkDAO
    private val statusWork = "status"
    private val carID = "carID"
    private var workId: Int = 0
    private var work: Work? = null
    private var color: Int? = null
    private var progress: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_work)
        editWorkName = findViewById(R.id.editWorkNameEditWorkActivity)
        editCost = findViewById(R.id.editCostEditWorkActivity)
        editDescription = findViewById(R.id.editDescriptionEditWorkActivity)
        buttonApplyToEditWork = findViewById(R.id.checkButtonEditWorkListActivity)
        dateWork = findViewById(R.id.dateWorkEditWorkActivity)
        buttonBackToWorkList = findViewById(R.id.backButtonEditWorkListActivity)
        buttonDeleteWork = findViewById(R.id.deleteButtonEditWorkActivity)
        textWorkNameTitle = findViewById(R.id.nameTitleEditWorkListActivity)
        imageStatusWorkPending = findViewById(R.id.statusWorkPendingEditWorkActivity)
        imageStatusWorkInProgress = findViewById(R.id.statusProgressEditWorkActivity)
        imageStatusWorkCompleted = findViewById(R.id.statusCompletedEditWorkActivity)
        textStatusWorkPending = findViewById(R.id.textPendingEditWorkActivity)
        textStatusWorkInProgress = findViewById(R.id.textProgressEditWorkActivity)
        textStatusWorkCompleted = findViewById(R.id.textCompletedEditWorkActivity)
        workDAO = CarDatabase.getDatabase(this).getWorkDAO()
        getWorkData(intent)
        if (work != null) {
            setDataInView()
        }
        buttonApplyToEditWork.setOnClickListener {
            val intentUpdateWork = Intent(this, WorkListActivity::class.java)
            val updateWork = updateWorkObject().also {
                it.id = work?.id
                it.parentCar = work?.parentCar
            }
            workDAO.update(updateWork)
            setResult(RESULT_OK, intentUpdateWork)
            finish()
        }
        buttonBackToWorkList.setOnClickListener {
            finish()
        }
        imageStatusWorkPending.setOnClickListener {
            setStatusPending()
        }
        imageStatusWorkInProgress.setOnClickListener {
            setStatusInProgress()
        }
        imageStatusWorkCompleted.setOnClickListener {
            setStatusCompleted()
        }
    }
    private fun getWorkData(intent: Intent){
        workId = intent.getIntExtra(carID, 0)
        work = workDAO.getWork(workId)
    }
    private fun setDataInView() {
        work?.let {
            textWorkNameTitle.text = it.workName
            editWorkName.setText(it.workName)
            editCost.setText(it.cost)
            editDescription.setText(it.description)
            dateWork.text = it.applicationDate
        }
        when (intent.getStringExtra(statusWork)) {
            getString(R.string.progress_pending) -> setStatusPending()
            getString(R.string.progress_in_progress) -> setStatusInProgress()
            getString(R.string.progress_completed) -> setStatusCompleted()
        }
    }
    private fun setStatusPending() {
        imageStatusWorkPending.setColorFilter(ContextCompat.getColor(this, R.color.red_status))
        imageStatusWorkInProgress.setColorFilter(ContextCompat.getColor(this, R.color.default_status))
        imageStatusWorkCompleted.setColorFilter(ContextCompat.getColor(this, R.color.default_status))
        color = R.color.red_status
        progress = getString(R.string.progress_pending)
    }
    private fun setStatusInProgress() {
        imageStatusWorkPending.setColorFilter(ContextCompat.getColor(this, R.color.default_status))
        imageStatusWorkInProgress.setColorFilter(ContextCompat.getColor(this, R.color.yellow_status))
        imageStatusWorkCompleted.setColorFilter(ContextCompat.getColor(this, R.color.default_status))
        color = R.color.yellow_status
        progress = getString(R.string.progress_in_progress)
    }
    private fun setStatusCompleted() {
        imageStatusWorkPending.setColorFilter(ContextCompat.getColor(this, R.color.default_status))
        imageStatusWorkInProgress.setColorFilter(ContextCompat.getColor(this, R.color.default_status))
        imageStatusWorkCompleted.setColorFilter(ContextCompat.getColor(this, R.color.green_status))
        color = R.color.green_status
        progress = getString(R.string.progress_completed)
    }
    private fun updateWorkObject() =
            Work(
                    workName = editWorkName.text.toString(),
                    cost = editCost.text.toString(),
                    description = editDescription.text.toString(),
                    applicationDate = dateWork.text.toString(),
                    progressWork = progress,
                    colorStatus = color
            )
}