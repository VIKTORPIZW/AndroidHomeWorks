package by.it.academy.app8_2task.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import by.it.academy.app8_2task.R
import by.it.academy.app8_2task.entity.PlaceItem
import by.it.academy.app8_2task.functions.createDirectory
import by.it.academy.app8_2task.functions.saveImage
import by.it.academy.app8_2task.repositories.DatabasePlaceRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception

private const val PHOTO_CODE_REQUEST = 10

class EditPlaceFragment:Fragment(R.layout.fragment_edit_place) {
    private lateinit var currentPhotoPath: String
    private lateinit var placePhotoImageView: ImageView
    private var pathImage: String? = null
    private var newPhotoLoaded = false
    private lateinit var databasePlaceRepository: DatabasePlaceRepository
    private lateinit var ioScope: CoroutineScope
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view){
            val editTextPlaceName = findViewById<EditText>(R.id.editTextPlaceName)
            val editTextLocality = findViewById<EditText>(R.id.editTextLocality)
            val editTextFishingType = findViewById<EditText>(R.id.editTextFishingType)
            val editTextFishSpecies = findViewById<EditText>(R.id.editTextFishSpecies)
            placePhotoImageView = findViewById(R.id.placePhoto)
            databasePlaceRepository = DatabasePlaceRepository(context)
            ioScope = CoroutineScope(Dispatchers.IO + Job())
            val place: PlaceItem? = arguments?.getParcelable("carItem")
            if (place != null) {
                with(place) {
                    editTextPlaceName.setText(placeName)
                    editTextLocality.setText(locality)
                    editTextFishingType.setText(fishingType)
                    editTextFishSpecies.setText(fishSpecies)
                    if (!pathImage.isNullOrEmpty()) {
                        placePhotoImageView.setImageURI(pathImage.toUri())
                    }
                }
            }
            findViewById<ImageButton>(R.id.buttonBack).setOnClickListener { finishActivity() }
            findViewById<ImageButton>(R.id.buttonApply).setOnClickListener {
                if (place != null) {
                    val ownerName = editTextPlaceName.text.toString()
                    val producer = editTextLocality.text.toString()
                    val model = editTextFishingType.text.toString()
                    val plateNumber = editTextFishSpecies.text.toString()
                    pathImage = if (newPhotoLoaded) {
                        currentPhotoPath
                    } else {
                        if (place.pathImage.isNullOrEmpty()) {
                            null
                        } else {
                            place.pathImage.toString()
                        }
                    }
                    ioScope.launch {
                        if (ownerName.isNotEmpty() && producer.isNotEmpty() && model.isNotEmpty() && plateNumber.isNotEmpty()) {
                            val carItem = PlaceItem(ownerName, producer, model, plateNumber, pathImage)
                            try {
                                databasePlaceRepository.updatePlace(carItem)
                                finishActivity()
                            } catch (e: Exception) {
                                Snackbar.make(placePhotoImageView, getString(R.string.place_already_exists), Snackbar.LENGTH_SHORT).show()
                            }
                        } else {
                            Snackbar.make(placePhotoImageView, getString(R.string.all_fields_must_be_filled), Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            findViewById<FloatingActionButton>(R.id.addPhoto).setOnClickListener {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, PHOTO_CODE_REQUEST)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
    private fun finishActivity() {
                ioScope.cancel()
        parentFragmentManager.commit {
            addToBackStack(null)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            setReorderingAllowed(true)
            replace(R.id.mainContainer, PlaceListFragment())
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.get("data").run {
            currentPhotoPath = context?.let { createDirectory(it)?.let { saveImage(this as Bitmap, placePhotoImageView, it) }.toString() }.toString()
            newPhotoLoaded = true
        }
    }
}