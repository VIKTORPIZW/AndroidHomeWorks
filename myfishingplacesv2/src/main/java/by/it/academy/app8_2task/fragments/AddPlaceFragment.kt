package by.it.academy.app8_2task.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
import kotlinx.coroutines.launch

private const val PHOTO_CODE_REQUEST = 10

class AddPlaceFragment : Fragment(R.layout.fragment_add_place) {
    private lateinit var placeNameText: EditText
    private lateinit var localityText: EditText
    private lateinit var fishingTypeText: EditText
    private lateinit var fishSpeciesText: EditText
    private lateinit var placePhoto: ImageView
    private lateinit var noPhotoText: TextView
    private lateinit var buttonApply: ImageButton
    private lateinit var buttonBack: ImageButton
    private lateinit var buttonAddPhoto: FloatingActionButton
    private var newPhotoLoaded = false
    private var photoPath: String? = null
    private lateinit var databasePlaceRepository: DatabasePlaceRepository
    private lateinit var ioScope: CoroutineScope
    private lateinit var currentPhotoPath: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            placeNameText = findViewById(R.id.editTextPlaceName)
            localityText = findViewById(R.id.editTextLocality)
            fishingTypeText = findViewById(R.id.editTextFishingType)
            fishSpeciesText = findViewById(R.id.editTextFishSpecies)
            placePhoto = findViewById(R.id.placePhoto)
            noPhotoText = findViewById(R.id.noPhotoText)
            buttonApply = findViewById(R.id.buttonApply)
            buttonBack = findViewById(R.id.buttonBack)
            buttonAddPhoto = findViewById(R.id.buttonAddPhoto)

            databasePlaceRepository = DatabasePlaceRepository(context)
            ioScope = CoroutineScope(Dispatchers.IO + Job())

            buttonBack.setOnClickListener { returnToPlaceListFragment() }
            buttonApply.setOnClickListener { addingPlaceToDatabaseAndReturnToPlacesListFragment() }

            createDirectory(context)
            buttonAddPhoto.setOnClickListener {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, PHOTO_CODE_REQUEST)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (newPhotoLoaded) {
            noPhotoText.isVisible = !newPhotoLoaded
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

    private fun addingPlaceToDatabaseAndReturnToPlacesListFragment() {
        val placeName = placeNameText.text.toString()
        val locality = localityText.text.toString()
        val fishingType = fishingTypeText.text.toString()
        val fishSpecies = fishSpeciesText.text.toString()

        photoPath = if (newPhotoLoaded) currentPhotoPath else null

        ioScope.launch {
            if (placeName.isNotEmpty() && locality.isNotEmpty() && fishingType.isNotEmpty() && fishSpecies.isNotEmpty()) {
                val place = PlaceItem(placeName, locality, fishingType, fishSpecies, photoPath)

                try {
                    databasePlaceRepository.addPlace(place)
                    returnToPlaceListFragment()

                } catch (e: Exception) {
                    Snackbar.make(buttonApply, R.string.place_already_exists, Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(buttonApply, R.string.all_fields_must_be_filled, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun returnToPlaceListFragment() {
        parentFragmentManager.commit {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            addToBackStack(null)
            setReorderingAllowed(true)
            replace(R.id.mainContainer, PlaceListFragment())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.get("data").run {
            currentPhotoPath = context?.let { createDirectory(it)?.let { saveImage(this as Bitmap, placePhoto, it) }.toString() }.toString()
            newPhotoLoaded = true
        }
    }

}

