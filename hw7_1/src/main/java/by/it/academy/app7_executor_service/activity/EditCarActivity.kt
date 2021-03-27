package by.it.academy.app7_executor_service.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import by.it.academy.app7_executor_service.R
import by.it.academy.app7_executor_service.entity.CarItem
import by.it.academy.app7_executor_service.functions.createDirectory
import by.it.academy.app7_executor_service.functions.saveImage
import by.it.academy.app7_executor_service.repositories.DatabaseCarsRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

private const val PHOTO_CODE_REQUEST = 10

class EditCarActivity : AppCompatActivity() {

    private lateinit var currentPhotoPath: String
    private lateinit var carPhotoImageView: ImageView
    private var pathImage: String? = null
    private var newPhotoLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_car)

        val ownerNameEditText = findViewById<EditText>(R.id.editTextOwnerName)
        val producerEditText = findViewById<EditText>(R.id.editTextProducer)
        val modelEditText = findViewById<EditText>(R.id.editTextModel)
        val plateNumberEditText = findViewById<EditText>(R.id.editTextPlateNumber)
        carPhotoImageView = findViewById(R.id.carPhoto)

        val databaseCarsRepository = DatabaseCarsRepository(applicationContext)

        val car: CarItem? = intent.getParcelableExtra("carItem")
        if (car != null) {
            with(car) {
                ownerNameEditText.setText(customerName)
                producerEditText.setText(producer)
                modelEditText.setText(carModel)
                plateNumberEditText.setText(carPlate)
                if (!pathImage.isNullOrEmpty()) {
                    carPhotoImageView.setImageURI(pathImage.toUri())
                }
            }
        }

        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener { finish() }

        findViewById<ImageButton>(R.id.buttonApply).setOnClickListener {
            if (car != null) {

                val ownerName = ownerNameEditText.text.toString()
                val producer = producerEditText.text.toString()
                val model = modelEditText.text.toString()
                val plateNumber = plateNumberEditText.text.toString()
                pathImage = if (newPhotoLoaded) {
                    currentPhotoPath
                } else {
                    if (car.pathImage.isNullOrEmpty()) {
                        null
                    } else {
                        car.pathImage.toString()
                    }
                }

                if (ownerName.isNotEmpty() && producer.isNotEmpty() && model.isNotEmpty() && plateNumber.isNotEmpty()) {
                    val carItem = CarItem(ownerName, producer, model, plateNumber, pathImage)
                    try {
                        databaseCarsRepository.updateCar(carItem)
//                        dao.updateCar(carItem)
                        finishActivity()
                    } catch (e: Exception) {
                        Snackbar.make(carPhotoImageView, getString(R.string.car_with_this_number_already_exists), Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    Snackbar.make(carPhotoImageView, getString(R.string.all_fields_must_be_filled), Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<FloatingActionButton>(R.id.addPhoto).setOnClickListener {
            intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, PHOTO_CODE_REQUEST)
        }
    }

    private fun finishActivity() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.extras?.get("data").run {
            currentPhotoPath = createDirectory(applicationContext)?.let { saveImage(this as Bitmap, carPhotoImageView, it) }.toString()
            newPhotoLoaded = true

        }
    }

}