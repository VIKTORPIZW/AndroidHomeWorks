package by.it.academy.app7_completablefuture.activity


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import by.it.academy.app7_completablefuture.R
import by.it.academy.app7_completablefuture.entity.CarItem
import by.it.academy.app7_completablefuture.functions.createDirectory
import by.it.academy.app7_completablefuture.functions.saveImage
import by.it.academy.app7_completablefuture.repositories.DatabaseCarsRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

private const val PHOTO_CODE_REQUEST = 10


class AddCarActivity : AppCompatActivity() {

    private lateinit var databaseCarsRepository: DatabaseCarsRepository
    private lateinit var ownerNameText: EditText
    private lateinit var producerText: EditText
    private lateinit var modelText: EditText
    private lateinit var plateNumberText: EditText
    private lateinit var carPhoto: ImageView
    private lateinit var noPhotoText: TextView
    private lateinit var buttonApply: ImageButton
    private lateinit var buttonBack: ImageButton
    private lateinit var buttonAddPhoto: FloatingActionButton
    private var newPhotoLoaded = false
    private var photoPath: String? = null

    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        ownerNameText = findViewById(R.id.editTextOwnerName)
        producerText = findViewById(R.id.editTextProducer)
        modelText = findViewById(R.id.editTextModel)
        plateNumberText = findViewById(R.id.editTextPlateNumber)
        carPhoto = findViewById(R.id.carPhoto)
        noPhotoText = findViewById(R.id.noPhotoText)
        buttonApply = findViewById(R.id.buttonApply)
        buttonBack = findViewById(R.id.buttonBack)
        buttonAddPhoto = findViewById(R.id.buttonAddPhoto)

        databaseCarsRepository = DatabaseCarsRepository(applicationContext)

        buttonBack.setOnClickListener { finish() }
        buttonApply.setOnClickListener { addingCarToDatabaseAndFinishingActivity() }

        createDirectory(applicationContext)
        buttonAddPhoto.setOnClickListener {
            intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, PHOTO_CODE_REQUEST)
        }
    }

    override fun onResume() {
        super.onResume()
        if (newPhotoLoaded) {
            noPhotoText.isVisible = !newPhotoLoaded
        }
    }

    private fun addingCarToDatabaseAndFinishingActivity() {
        val ownerName = ownerNameText.text.toString()
        val producer = producerText.text.toString()
        val model = modelText.text.toString()
        val plateNumber = plateNumberText.text.toString()

        photoPath = if (newPhotoLoaded) currentPhotoPath else null

        if (ownerName.isNotEmpty() && producer.isNotEmpty() && model.isNotEmpty() && plateNumber.isNotEmpty()) {
            val car = CarItem(ownerName, producer, model, plateNumber, photoPath)

            try {
                databaseCarsRepository.addCar(car)
                finishActivity()
            } catch (e: Exception) {
                Snackbar.make(buttonApply, R.string.car_with_this_number_already_exists, Snackbar.LENGTH_SHORT).show()
            }
        } else {
            Snackbar.make(buttonApply, R.string.all_fields_must_be_filled, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun finishActivity() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.get("data").run {
            currentPhotoPath = createDirectory(applicationContext)?.let { saveImage(this as Bitmap, carPhoto, it) }.toString()
            newPhotoLoaded = true
        }
    }

}