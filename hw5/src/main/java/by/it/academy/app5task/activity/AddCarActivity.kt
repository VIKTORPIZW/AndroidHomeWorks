package by.it.academy.app5task.activity


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import by.it.academy.app5task.R
import by.it.academy.app5task.database.DatabaseCars
import by.it.academy.app5task.database.DatabaseCarsDAO
import by.it.academy.app5task.entity.CarItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

private const val PHOTO_CODE_REQUEST = 10


class AddCarActivity : AppCompatActivity() {

    private lateinit var ownerNameText: EditText
    private lateinit var producerText: EditText
    private lateinit var modelText: EditText
    private lateinit var plateNumberText: EditText
    private lateinit var carPhoto: ImageView
    private lateinit var noPhotoText: TextView
    private lateinit var buttonApply: ImageButton
    private lateinit var buttonBack: ImageButton
    private lateinit var dao: DatabaseCarsDAO
    private lateinit var buttonAddPhoto: FloatingActionButton
    private var newPhotoLoaded = false
    private var photoPath: String? = null

    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        setFindViewByIds()

        dao = DatabaseCars.init(this).getCarDatabaseDAO()

        buttonBack.setOnClickListener { finish() }
        buttonApply.setOnClickListener { setOnClickListenerButtonApply() }

        createDirectory()
        buttonAddPhoto.setOnClickListener {
            intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, PHOTO_CODE_REQUEST)
        }


    }

    override fun onResume() {
        super.onResume()
        if (newPhotoLoaded) {
            noPhotoText.isVisible = false
        }
    }

    private fun createDirectory(): File? {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val carPictureDirectory = File("${applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/CarPictures")
            if (!carPictureDirectory.exists()) {
                carPictureDirectory.mkdir()
            }
            return carPictureDirectory
        }
        return null
    }


    private fun setOnClickListenerButtonApply() {
        val ownerName = ownerNameText.text.toString()
        val producer = producerText.text.toString()
        val model = modelText.text.toString()
        val plateNumber = plateNumberText.text.toString()
        if (newPhotoLoaded) {
            photoPath = currentPhotoPath
        } else {
            photoPath = null
        }
        var success = true

        if (ownerName.isNotEmpty() && producer.isNotEmpty() && model.isNotEmpty() && plateNumber.isNotEmpty()) {
            val car = CarItem(ownerName, producer, model, plateNumber, photoPath)

            try {
                dao.addCarToDatabase(car)
            } catch (e: Exception) {
                Toast.makeText(this, getString(R.string.car_with_this_number_already_exists), Toast.LENGTH_SHORT).show()
                success = false
            }
            if (success) {
                setResult(RESULT_OK, intent)
                finish()
            }
        } else {
            Toast.makeText(this, getString(R.string.all_fields_must_be_filled), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            data.extras?.get("data").run {
                currentPhotoPath = createDirectory()?.let { saveImage(this as Bitmap, carPhoto, it) }.toString()
                newPhotoLoaded = true
            }
        }
    }

    private fun saveImage(photo: Bitmap, imageView: ImageView, carPictureDirectory: File): String {
        val path = "photo_${System.currentTimeMillis()}.jpg"
        val pathToPicture = "${carPictureDirectory.path}/${path}"
        val file = File(carPictureDirectory, path)
        file.createNewFile()
        val stream = FileOutputStream(file)
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        imageView.setImageBitmap(photo)
        stream.flush()
        stream.close()
        return pathToPicture
    }

    private fun setFindViewByIds() {
        ownerNameText = findViewById(R.id.editTextOwnerName)
        producerText = findViewById(R.id.editTextProducer)
        modelText = findViewById(R.id.editTextModel)
        plateNumberText = findViewById(R.id.editTextPlateNumber)
        carPhoto = findViewById(R.id.carPhoto)
        noPhotoText = findViewById(R.id.noPhotoText)
        buttonApply = findViewById(R.id.buttonApply)
        buttonBack = findViewById(R.id.buttonBack)
        buttonAddPhoto = findViewById(R.id.buttonAddPhoto)
    }
}