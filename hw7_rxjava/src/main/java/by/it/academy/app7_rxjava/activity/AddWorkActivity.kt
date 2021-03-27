package by.it.academy.app7_rxjava.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import by.it.academy.app7_rxjava.R
import by.it.academy.app7_rxjava.customview.WorkStatusCustomView
import by.it.academy.app7_rxjava.entity.WorkItem
import by.it.academy.app7_rxjava.functions.getCurrentDate
import by.it.academy.app7_rxjava.repositories.DatabaseWorksRepository

const val ImageButtonPendingCode = 1
const val ImageButtonInProgressCode = 2
const val ImageButtonCompletedCode = 3


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
    private lateinit var workStatusCustomView: WorkStatusCustomView
    private lateinit var databaseWorksRepository: DatabaseWorksRepository
    private var workStatus: Int = ImageButtonPendingCode


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work)

        buttonBack = findViewById(R.id.buttonBack)
        buttonApply = findViewById(R.id.buttonApply)
        textViewApplicationDate = findViewById(R.id.textViewApplicationDate)
        editTextWorkName = findViewById(R.id.editTextWorkName)
        editTextCost = findViewById(R.id.editTextCost)
        editTextDescription = findViewById(R.id.editTextDescription)
        imageButtonPending = findViewById(R.id.imageButtonPending)
        imageButtonInProgress = findViewById(R.id.imageButtonInProgress)
        imageButtonCompleted = findViewById(R.id.imageButtonCompleted)
        workStatusCustomView = findViewById(R.id.customViewWorkStatus)

        databaseWorksRepository = DatabaseWorksRepository(applicationContext)
        carPlate = intent.getStringExtra("carPlate").toString()

        setCurrentDateInTextView()

        workStatusCustomView.customClickListenerSetWorkStatus = { status -> workStatus = status }

        buttonBack.setOnClickListener { finish() }

        buttonApply.setOnClickListener {
            val workName = editTextWorkName.text.toString()
            val cost = editTextCost.text.toString()
            val description = editTextDescription.text.toString()
            if (workName.isNotEmpty() && cost.isNotEmpty() && description.isNotEmpty()) {

                val workItem = WorkItem(date, workName, description, cost.toFloat(), workStatus, carPlate)
                databaseWorksRepository.addWork(workItem)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.all_fields_must_be_filled), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setCurrentDateInTextView() {
        date = getCurrentDate()
        val dateForTextView = getString(R.string.application_date, date)
        textViewApplicationDate.text = dateForTextView
    }

}