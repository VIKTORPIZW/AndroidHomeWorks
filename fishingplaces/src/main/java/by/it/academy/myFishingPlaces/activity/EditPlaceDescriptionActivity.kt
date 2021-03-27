package by.it.academy.myFishingPlaces.activity

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import by.it.academy.myFishingPlaces.R
import by.it.academy.myFishingPlaces.database.DatabasePlaces
import by.it.academy.myFishingPlaces.database.PlaceDescriptionItemDAO
import by.it.academy.myFishingPlaces.entity.PlaceDescriptionItem

private const val ImageButtonPendingCode = 1
private const val ImageButtonInProgressCode = 2
private const val ImageButtonCompletedCode = 3

class EditPlaceDescriptionActivity : AppCompatActivity() {

    private lateinit var buttonBack: ImageButton
    private lateinit var buttonDelete: ImageButton
    private lateinit var buttonApply: ImageButton
    private lateinit var textViewApplicationDate: TextView
    private lateinit var editTextRoadToWater: EditText
    private lateinit var editTextDistance: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var imageButtonPending: ImageButton
    private lateinit var imageButtonInProgress: ImageButton
    private lateinit var imageButtonCompleted: ImageButton
    private lateinit var date: String
    private lateinit var carPlate: String
    private lateinit var dao: PlaceDescriptionItemDAO
    private lateinit var placeDescriptionItem: PlaceDescriptionItem
    private var workStatus: Int = ImageButtonPendingCode


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_palce_description)

        dao = DatabasePlaces.init(this).getPlaceDescriptionListDatabaseDAO()

        setFindViewByIds()
        getIntentData()
        setOnClickListenersImageButtons()
        setImagesProgress()

        buttonBack.setOnClickListener { finish() }

        buttonDelete.setOnClickListener { alertDialogDeleteWork() }

        buttonApply.setOnClickListener {
            val workName = editTextRoadToWater.text.toString()
            val cost = editTextDistance.text.toString()
            val description = editTextDescription.text.toString()
            Log.v("qwe", "workName=$workName cost=$cost description=$description")
            if (workName.isNotEmpty() && cost.isNotEmpty() && description.isNotEmpty()) {
                val workItem = PlaceDescriptionItem(date, workName, description, cost.toFloat(), workStatus, carPlate).also { it.id = placeDescriptionItem.id }
                dao.updatePlaceDescription(workItem)
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
                dao.deletePlaceDescription(placeDescriptionItem)
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
        imageButtonPending.setImageResource(R.drawable.molluscs)
        imageButtonInProgress.setImageResource(R.drawable.fishing)
        imageButtonCompleted.setImageResource(R.drawable.fishing)
    }

    private fun imageButtonInProgressClicked() {
        imageButtonInProgress.setImageResource(R.drawable.fish2)
        imageButtonPending.setImageResource(R.drawable.fishing)
        imageButtonCompleted.setImageResource(R.drawable.fishing)
    }

    private fun imageButtonCompletedClicked() {
        imageButtonPending.setImageResource(R.drawable.fishing)
        imageButtonInProgress.setImageResource(R.drawable.fishing)
        imageButtonCompleted.setImageResource(R.drawable.fish1)
    }

    private fun getIntentData() {
        placeDescriptionItem = intent.getParcelableExtra("workItem")
                ?: PlaceDescriptionItem("", "", "", 0F, 0, "")
        date = placeDescriptionItem.applicationDate
        val dateForTextViewApplicationDate = getString(R.string.application_date) + " " + date
        textViewApplicationDate.text = dateForTextViewApplicationDate
        editTextRoadToWater.setText(placeDescriptionItem.roadToWater)
        editTextDistance.setText(placeDescriptionItem.distance.toString())
        editTextDescription.setText(placeDescriptionItem.description)
        workStatus = placeDescriptionItem.fishingStatus
        carPlate = placeDescriptionItem.fishSpecies

    }

    private fun setFindViewByIds() {
        buttonBack = findViewById(R.id.buttonBack)
        buttonDelete = findViewById(R.id.buttonDeletePlaceDescription)
        buttonApply = findViewById(R.id.buttonApply)
        textViewApplicationDate = findViewById(R.id.textViewApplicationDate)
        editTextRoadToWater = findViewById(R.id.editTextRoadToWater)
        editTextDistance = findViewById(R.id.editTextDistance)
        editTextDescription = findViewById(R.id.editTextDescription)
        imageButtonPending = findViewById(R.id.imageButtonBadBite)
        imageButtonInProgress = findViewById(R.id.imageButtonNormalBite)
        imageButtonCompleted = findViewById(R.id.imageButtonGoodBite)
    }
}