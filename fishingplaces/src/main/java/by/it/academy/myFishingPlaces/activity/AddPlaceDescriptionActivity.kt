package by.it.academy.myFishingPlaces.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import by.it.academy.myFishingPlaces.R
import by.it.academy.myFishingPlaces.database.DatabasePlaces
import by.it.academy.myFishingPlaces.database.PlaceDescriptionItemDAO
import by.it.academy.myFishingPlaces.entity.PlaceDescriptionItem
import java.text.SimpleDateFormat
import java.util.Date

private const val ImageButtonPendingCode = 1
private const val ImageButtonInProgressCode = 2
private const val ImageButtonCompletedCode = 3

class AddPlaceDescriptionActivity : AppCompatActivity() {

    private lateinit var buttonBack: ImageButton
    private lateinit var buttonApply: ImageButton
    private lateinit var textViewApplicationDate: TextView
    private lateinit var editRoadToWater: EditText
    private lateinit var editTextDistance: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var imageButtonBadBite: ImageButton
    private lateinit var imageButtonNormalBite: ImageButton
    private lateinit var imageButtonGoodBite: ImageButton
    private lateinit var date: String
    private lateinit var fishSpecies: String
    private lateinit var dao: PlaceDescriptionItemDAO
    private var fishingStatus: Int = ImageButtonPendingCode


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place_description)

        dao = DatabasePlaces.init(this).getPlaceDescriptionListDatabaseDAO()
        setFindViewByIds()
        getCurrentDate()
        setOnClickListenersImageButtons()

        buttonBack.setOnClickListener { finish() }

        fishSpecies = intent.getStringExtra("fishSpecies").toString()


        buttonApply.setOnClickListener {
            val roadToWaterName = editRoadToWater.text.toString()
            val distance = editTextDistance.text.toString()
            val description = editTextDescription.text.toString()
            if (roadToWaterName.isNotEmpty() && distance.isNotEmpty() && description.isNotEmpty()) {

                val placeDescriptionItem = PlaceDescriptionItem(date, roadToWaterName, description, distance.toFloat(), fishingStatus, fishSpecies)
                dao.addPlaceDescription(placeDescriptionItem)
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
        imageButtonBadBite.setOnClickListener {
            imageButtonBadBite.setImageResource(R.drawable.molluscs)
            imageButtonNormalBite.setImageResource(R.drawable.fishing)
            imageButtonGoodBite.setImageResource(R.drawable.fishing)
            fishingStatus = ImageButtonPendingCode
        }

        imageButtonNormalBite.setOnClickListener {
            imageButtonNormalBite.setImageResource(R.drawable.fish2)
            imageButtonBadBite.setImageResource(R.drawable.fishing)
            imageButtonGoodBite.setImageResource(R.drawable.fishing)
            fishingStatus = ImageButtonInProgressCode
        }

        imageButtonGoodBite.setOnClickListener {
            imageButtonBadBite.setImageResource(R.drawable.fishing)
            imageButtonNormalBite.setImageResource(R.drawable.fishing)
            imageButtonGoodBite.setImageResource(R.drawable.fish1)
            fishingStatus = ImageButtonCompletedCode
        }
    }

    private fun setFindViewByIds() {
        buttonBack = findViewById(R.id.buttonBack)
        buttonApply = findViewById(R.id.buttonApply)
        textViewApplicationDate = findViewById(R.id.textViewApplicationDate)
        editRoadToWater = findViewById(R.id.editTextRoadToWater)
        editTextDistance = findViewById(R.id.editTextDistance)
        editTextDescription = findViewById(R.id.editTextDescription)
        imageButtonBadBite = findViewById(R.id.imageButtonBadBite)
        imageButtonNormalBite = findViewById(R.id.imageButtonNormalBite)
        imageButtonGoodBite = findViewById(R.id.imageButtonGoodBite)
    }
}