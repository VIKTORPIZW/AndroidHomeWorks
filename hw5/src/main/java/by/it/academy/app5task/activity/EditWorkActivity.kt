package by.it.academy.app5task.activity

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import by.it.academy.app5task.R
import by.it.academy.app5task.database.DatabaseCars
import by.it.academy.app5task.database.WorkItemDAO
import by.it.academy.app5task.entity.WorkItem

private const val ImageButtonPendingCode = 1
private const val ImageButtonInProgressCode = 2
private const val ImageButtonCompletedCode = 3

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
    private lateinit var date: String
    private lateinit var carPlate: String
    private lateinit var dao: WorkItemDAO
    private lateinit var workItem: WorkItem
    private var workStatus: Int = ImageButtonPendingCode


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_work)

        dao = DatabaseCars.init(this).getWorkListDatabaseDAO()

        setFindViewByIds()
        getIntentData()
        setOnClickListenersImageButtons()
        setImagesProgress()

        buttonBack.setOnClickListener { finish() }

        buttonDelete.setOnClickListener { alertDialogDeleteWork() }

        buttonApply.setOnClickListener {
            val workName = editTextWorkName.text.toString()
            val cost = editTextCost.text.toString()
            val description = editTextDescription.text.toString()
            Log.v("qwe", "workName=$workName cost=$cost description=$description")
            if (workName.isNotEmpty() && cost.isNotEmpty() && description.isNotEmpty()) {
                val workItem = WorkItem(date, workName, description, cost.toFloat(), workStatus, carPlate).also { it.id = workItem.id }
                dao.updateWork(workItem)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.all_fields_must_be_filled), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun alertDialogDeleteWork() {
        val alertDialog = AlertDialog.Builder(this)

        with(alertDialog) {
            setTitle(R.string.warning)
            setMessage(R.string.are_you_sure_wont_to_delete_work)
            setPositiveButton(R.string.ok) { _, _ ->
                dao.deleteWork(workItem)
                setResult(RESULT_OK, intent)
                finish()
            }
            setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.cancel() }
            create()
            show()
        }
    }

    private fun setImagesProgress() {
        when (workStatus) {
            ImageButtonPendingCode -> {
                imageButtonPendingClicked()
            }
            ImageButtonInProgressCode -> {
                imageButtonInProgressClicked()
            }
            ImageButtonCompletedCode -> {
                imageButtonCompletedClicked()
            }

        }
    }

    private fun setOnClickListenersImageButtons() {
        imageButtonPending.setOnClickListener {
            imageButtonPendingClicked()
            workStatus = ImageButtonPendingCode
        }
        imageButtonInProgress.setOnClickListener {
            imageButtonInProgressClicked()
            workStatus = ImageButtonInProgressCode
        }
        imageButtonCompleted.setOnClickListener {
            imageButtonCompletedClicked()
            workStatus = ImageButtonCompletedCode
        }
    }

    private fun imageButtonPendingClicked() {
        imageButtonPending.setImageResource(R.drawable.ic_pending)
        imageButtonInProgress.setImageResource(R.drawable.ic_in_progress_gray)
        imageButtonCompleted.setImageResource(R.drawable.ic_completed_gray)
    }

    private fun imageButtonInProgressClicked() {
        imageButtonInProgress.setImageResource(R.drawable.ic_in_progress)
        imageButtonPending.setImageResource(R.drawable.ic_pending_gray)
        imageButtonCompleted.setImageResource(R.drawable.ic_completed_gray)
    }

    private fun imageButtonCompletedClicked() {
        imageButtonPending.setImageResource(R.drawable.ic_pending_gray)
        imageButtonInProgress.setImageResource(R.drawable.ic_in_progress_gray)
        imageButtonCompleted.setImageResource(R.drawable.ic_completed)
    }

    private fun getIntentData() {
        workItem = intent.getParcelableExtra("workItem")
                ?: WorkItem("", "", "", 0F, 0, "")
        date = workItem.applicationDate
        val dateForTextViewApplicationDate = getString(R.string.application_date) + " " + date
        textViewApplicationDate.text = dateForTextViewApplicationDate
        editTextWorkName.setText(workItem.workType)
        editTextCost.setText(workItem.cost.toString())
        editTextDescription.setText(workItem.description)
        workStatus = workItem.workStatus
        carPlate = workItem.carPlate

    }

    private fun setFindViewByIds() {
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
    }
}