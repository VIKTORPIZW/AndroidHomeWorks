package by.it.academy.myFishingPlaces.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import by.it.academy.myFishingPlaces.R
import by.it.academy.myFishingPlaces.database.DatabasePlaces
import by.it.academy.myFishingPlaces.entity.PlaceItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

private const val PHOTO_CODE_REQUEST = 10

class EditPlaceActivity : AppCompatActivity() {

    private lateinit var currentPhotoPath: String
    private lateinit var carPhotoImageView: ImageView
    private var pathImage: String? = null
    private var newPhotoLoaded = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_place)

        val ownerNameEditText: EditText = findViewById(R.id.editTextPlaceName)
        val producerEditText: EditText = findViewById(R.id.editTextLocality)
        val modelEditText: EditText = findViewById(R.id.editTextFishingType)
        val plateNumberEditText: EditText = findViewById(R.id.editTextFishSpecies)
        carPhotoImageView = findViewById(R.id.placePhoto)

        val dao = DatabasePlaces.init(this).getPlaceDatabaseDAO()

        val place: PlaceItem? = intent.getParcelableExtra("carItem")
        ownerNameEditText.setText(place?.placeName)
        producerEditText.setText(place?.locality)
        modelEditText.setText(place?.fishingType)
        plateNumberEditText.setText(place?.fishSpecies)
        if (!place?.pathImage.isNullOrEmpty()) {
            carPhotoImageView.setImageURI(place?.pathImage?.toUri())
        }


        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener { finish() }


        findViewById<ImageButton>(R.id.buttonApply).setOnClickListener {
            if (place != null) {

                val ownerName = ownerNameEditText.text.toString()
                val producer = producerEditText.text.toString()
                val model = modelEditText.text.toString()
                val plateNumber = plateNumberEditText.text.toString()
                if (newPhotoLoaded) {
                    pathImage = currentPhotoPath
                } else {
                    if (place.pathImage.isNullOrEmpty()) {
                        pathImage = null
                    } else {
                        pathImage = place.pathImage.toString()
                    }

                }

                var success = true

                if (ownerName.isNotEmpty() && producer.isNotEmpty() && model.isNotEmpty() && plateNumber.isNotEmpty()) {
                    val carItem = PlaceItem(ownerName, producer, model, plateNumber, pathImage)
                    try {
                        dao.updatePlace(carItem)

                    } catch (e: Exception) {
                        Toast.makeText(this, getString(R.string.place_exists), Toast.LENGTH_SHORT).show()
                        success = false
                    }
                    if (success) {
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.all_fields_must_be_filled), Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<FloatingActionButton>(R.id.addPhoto).setOnClickListener {
            intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, PHOTO_CODE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            data.extras?.get("data").run {
                currentPhotoPath = createDirectory()?.let { saveImage(this as Bitmap, carPhotoImageView, it) }.toString()
                newPhotoLoaded = true
            }
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
}