package by.it.academy.app5task.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import by.it.academy.app5task.R
import by.it.academy.app5task.database.DatabaseCars
import by.it.academy.app5task.database.WorkItemDAO
import by.it.academy.app5task.entity.WorkItem
import java.text.SimpleDateFormat
import java.util.Date

private const val ImageButtonPendingCode = 1
private const val ImageButtonInProgressCode = 2
private const val ImageButtonCompletedCode = 3

class AddWorkActivity : AppCompatActivity() {

    private lateinit var buttonBack: ImageButton
    private lateinit var buttonApply: ImageButton
    private lateinit var textViewApplicationDate: TextView
    private lateinit var editTextWorkName: EditText
    private lateinit var editTextCost: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var imageButtonPending: ImageButton
    private lateinit var imageButtonInProgress: ImageButton
    private lateinit var imageButtonCompleted: ImageButton
    private lateinit var date: String
    private lateinit var carPlate: String
    private lateinit var dao: WorkItemDAO
    private var workStatus: Int = ImageButtonPendingCode


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work)

        dao = DatabaseCars.init(this).getWorkListDatabaseDAO()
        setFindViewByIds()
        getCurrentDate()
        setOnClickListenersImageButtons()

        buttonBack.setOnClickListener { finish() }

        carPlate = intent.getStringExtra("carPlate").toString()


        buttonApply.setOnClickListener {
            val workName = editTextWorkName.text.toString()
            val cost = editTextCost.text.toString()
            val description = editTextDescription.text.toString()
            if (workName.isNotEmpty() && cost.isNotEmpty() && description.isNotEmpty()) {

                val workItem = WorkItem(date, workName, description, cost.toFloat(), workStatus, carPlate)
                dao.addWork(workItem)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.all_fields_must_be_filled), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getCurrentDate() {
        val simpleDateFormat = SimpleDateFormat.getDateInstance()
        date = simpleDateFormat.format(Date())
        val dateForTextView = getString(R.string.application_date) + " " + date
        textViewApplicationDate.text = dateForTextView
    }

    private fun setOnClickListenersImageButtons() {
        imageButtonPending.setOnClickListener {
            imageButtonPending.setImageResource(R.drawable.ic_pending)
            imageButtonInProgress.setImageResource(R.drawable.ic_in_progress_gray)
            imageButtonCompleted.setImageResource(R.drawable.ic_completed_gray)
            workStatus = ImageButtonPendingCode
        }

        imageButtonInProgress.setOnClickListener {
            imageButtonInProgress.setImageResource(R.drawable.ic_in_progress)
            imageButtonPending.setImageResource(R.drawable.ic_pending_gray)
            imageButtonCompleted.setImageResource(R.drawable.ic_completed_gray)
            workStatus = ImageButtonInProgressCode
        }

        imageButtonCompleted.setOnClickListener {
            imageButtonPending.setImageResource(R.drawable.ic_pending_gray)
            imageButtonInProgress.setImageResource(R.drawable.ic_in_progress_gray)
            imageButtonCompleted.setImageResource(R.drawable.ic_completed)
            workStatus = ImageButtonCompletedCode
        }
    }

    private fun setFindViewByIds() {
        buttonBack = findViewById(R.id.buttonBack)
        buttonApply = findViewById(R.id.buttonApply)
        textViewApplicationDate = findViewById(R.id.textViewApplicationDate)
        editTextWorkName = findViewById(R.id.editTextWorkName)
        editTextCost = findViewById(R.id.editTextCost)
        editTextDescription = findViewById(R.id.editTextDescription)
        imageButtonPending = findViewById(R.id.imageButtonPending)
        imageButtonInProgress = findViewById(R.id.imageButtonInProgress)
        imageButtonCompleted = findViewById(R.id.imageButtonCompleted)
    }
}