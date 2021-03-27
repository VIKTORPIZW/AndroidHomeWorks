package by.it.academy.myFishingPlaces.activity


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
import by.it.academy.myFishingPlaces.R
import by.it.academy.myFishingPlaces.database.DatabasePlaces
import by.it.academy.myFishingPlaces.database.DatabasePlacesDAO
import by.it.academy.myFishingPlaces.entity.PlaceItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

private const val PHOTO_CODE_REQUEST = 10


class AddPlaceActivity : AppCompatActivity() {

    private lateinit var placeNameText: EditText
    private lateinit var localityText: EditText
    private lateinit var fishingTypeText: EditText
    private lateinit var fishSpeciesText: EditText
    private lateinit var placePhoto: ImageView
    private lateinit var noPhotoText: TextView
    private lateinit var buttonApply: ImageButton
    private lateinit var buttonBack: ImageButton
    private lateinit var dao: DatabasePlacesDAO
    private lateinit var buttonAddPhoto: FloatingActionButton
    private var newPhotoLoaded = false
    private var photoPath: String? = null

    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)

        setFindViewByIds()

        dao = DatabasePlaces.init(this).getPlaceDatabaseDAO()

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
            val placePictureDirectory = File("${applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/PlacePictures")
            if (!placePictureDirectory.exists()) {
                placePictureDirectory.mkdir()
            }
            return placePictureDirectory
        }
        return null
    }


    private fun setOnClickListenerButtonApply() {
        val placeName = placeNameText.text.toString()
        val locality = localityText.text.toString()
        val fishingType = fishingTypeText.text.toString()
        val fishSpecies = fishSpeciesText.text.toString()
        if (newPhotoLoaded) {
            photoPath = currentPhotoPath
        } else {
            photoPath = null
        }
        var success = true

        if (placeName.isNotEmpty() && locality.isNotEmpty() && fishingType.isNotEmpty() && fishSpecies.isNotEmpty()) {
            val place = PlaceItem(placeName, locality, fishingType, fishSpecies, photoPath)

            try {
                dao.addPlaceToDatabase(place)
            } catch (e: Exception) {
                Toast.makeText(this, getString(R.string.place_exists), Toast.LENGTH_SHORT).show()
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
                currentPhotoPath = createDirectory()?.let { saveImage(this as Bitmap, placePhoto, it) }.toString()
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
        placeNameText = findViewById(R.id.editTextPlaceName)
        localityText = findViewById(R.id.editTextLocality)
        fishingTypeText = findViewById(R.id.editTextFishingType)
        fishSpeciesText = findViewById(R.id.editTextFishSpecies)
        placePhoto = findViewById(R.id.placePhoto)
        noPhotoText = findViewById(R.id.noPhotoText)
        buttonApply = findViewById(R.id.buttonApply)
        buttonBack = findViewById(R.id.buttonBack)
        buttonAddPhoto = findViewById(R.id.buttonAddPhoto)
    }
}