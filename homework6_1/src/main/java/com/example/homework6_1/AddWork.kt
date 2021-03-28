package com.example.homework6_1

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
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class AddWork : AppCompatActivity() {
    private lateinit var dateWork: TextView
    private lateinit var nameWork: EditText
    private lateinit var cost: EditText
    private lateinit var description: EditText
    private lateinit var buttonAddWork: ImageButton
    private lateinit var buttonBackToWorkList: ImageButton
    private lateinit var imagePendingWork: ImageView
    private lateinit var imageInProgressWork: ImageView
    private lateinit var imageCompletedWork: ImageView
    private lateinit var textPendingWork: TextView
    private lateinit var textInProgressWork: TextView
    private lateinit var textCompletedWork: TextView
    private lateinit var workDAO: WorkDAO
    private val parentCarName = "parentCarName"
    private var color: Int? = null
    private var progress: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_work)

        dateWork = findViewById(R.id.dateWorkAddWork)
        nameWork = findViewById(R.id.editWorkName)
        cost = findViewById(R.id.editCost)
        description = findViewById(R.id.editDescription)
        imagePendingWork = findViewById(R.id.statusWorkPendingAddWorkActivity)
        imageInProgressWork = findViewById(R.id.statusProgressAddWorkActivity)
        imageCompletedWork = findViewById(R.id.statusCompletedAddWorkActivity)
        textPendingWork = findViewById(R.id.textPendingAddWorkActivity)
        textInProgressWork = findViewById(R.id.textProgressAddWorkActivity)
        textCompletedWork = findViewById(R.id.textCompletedAddWorkActivity)
        buttonAddWork = findViewById(R.id.checkButtonAddWorkListActivity)
        buttonBackToWorkList = findViewById(R.id.backButtonAddWorkListActivity)

        dateWork.text = getTimeWork()
        workDAO = CarDatabase.getDatabase(this).getWorkDAO()
        setProgress()

        buttonAddWork.setOnClickListener {
            createWorkObject().apply {
                parentCar = intent.getStringExtra(parentCarName)
                workDAO.insertAll(this)
                finish()
            }
        }
        buttonBackToWorkList.setOnClickListener {
            finish()
        }
    }

    private fun setProgress() {
        imagePendingWork.setColorFilter(ContextCompat.getColor(this, R.color.red_status))
        textPendingWork.setTextColor(ContextCompat.getColor(this, R.color.red_status))
        color = R.color.red_status
        progress = getString(R.string.pending_status)
    }

    private fun getTimeWork() = SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault()).format(Date())

    private fun createWorkObject() =
            Work(
                    workName = nameWork.text.toString(),
                    cost = cost.text.toString(),
                    description = description.text.toString(),
                    applicationDate = dateWork.text.toString(),
                    progressWork = progress,
                    colorStatus = color
            )
}