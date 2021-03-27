package by.it.academy.app7_rxjava.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import by.it.academy.app7_rxjava.R
import by.it.academy.app7_rxjava.customview.WorkStatusCustomView
import by.it.academy.app7_rxjava.entity.WorkItem
import by.it.academy.app7_rxjava.repositories.DatabaseWorksRepository
import com.google.android.material.snackbar.Snackbar


class EditWorkActivity : AppCompatActivity() {

    private lateinit var buttonBack: ImageButton
    private lateinit var buttonDelete: ImageButton
    private lateinit var buttonApply: ImageButton
    private lateinit var textViewApplicationDate: TextView
    private lateinit var editTextWorkName: EditText
    private lateinit var editTextCost: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var imageButtonPending: ImageButton
    private lateinit var imageButtonInProgress: ImageButton
    private lateinit var imageButtonCompleted: ImageButton
    private lateinit var workStatusCustomView: WorkStatusCustomView
    private lateinit var date: String
    private lateinit var carPlate: String
    private lateinit var databaseWorksRepository: DatabaseWorksRepository
    private lateinit var workItem: WorkItem
    private var workStatus: Int = ImageButtonPendingCode


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_work)

        buttonBack = findViewById(R.id.buttonBack)
        buttonDelete = findViewById(R.id.buttonDeleteWork)
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
        getIntentDataAndSetInfoToEditTexts()

        workStatusCustomView.customClickListenerSetWorkStatus = { status -> workStatus = status }

        buttonBack.setOnClickListener { finish() }

        buttonDelete.setOnClickListener { alertDialogDeleteWork() }

        buttonApply.setOnClickListener {
            val workName = editTextWorkName.text.toString()
            val cost = editTextCost.text.toString()
            val description = editTextDescription.text.toString()
            if (workName.isNotEmpty() && cost.isNotEmpty() && description.isNotEmpty()) {
                val workItem = WorkItem(date, workName, description, cost.toFloat(), workStatus, carPlate).apply { id = workItem.id }
                databaseWorksRepository.updateWork(workItem)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Snackbar.make(buttonApply, getString(R.string.all_fields_must_be_filled), Snackbar.LENGTH_SHORT).show()
            }

        }
    }

    private fun alertDialogDeleteWork() {
        val alertDialog = AlertDialog.Builder(this)

        with(alertDialog) {
            setTitle(R.string.warning)
            setMessage(R.string.are_you_sure_wont_to_delete_work)
            setPositiveButton(R.string.ok) { _, _ ->
                databaseWorksRepository.deleteWork(workItem)
                setResult(RESULT_OK, intent)
                finish()
            }
            setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.cancel() }
            create()
            show()
        }
    }

    private fun getIntentDataAndSetInfoToEditTexts() {
        workItem = intent.getParcelableExtra("workItem")
                ?: WorkItem("", "", "", 0F, 0, "")
        date = workItem.applicationDate
        val dateForTextViewApplicationDate = getString(R.string.application_date, date)
        textViewApplicationDate.text = dateForTextViewApplicationDate
        editTextWorkName.setText(workItem.workType)
        editTextCost.setText(workItem.cost.toString())
        editTextDescription.setText(workItem.description)
        workStatus = workItem.workStatus
        carPlate = workItem.carPlate

    }

}