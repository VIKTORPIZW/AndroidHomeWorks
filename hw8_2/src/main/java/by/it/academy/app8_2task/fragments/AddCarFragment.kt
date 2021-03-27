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
import by.it.academy.app8_2task.entity.CarItem
import by.it.academy.app8_2task.functions.createDirectory
import by.it.academy.app8_2task.functions.saveImage
import by.it.academy.app8_2task.repositories.DatabaseCarsRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val PHOTO_CODE_REQUEST = 10

class AddCarFragment : Fragment(R.layout.fragment_add_car) {
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
    private lateinit var databaseCarsRepository: DatabaseCarsRepository
    private lateinit var ioScope: CoroutineScope
    private lateinit var currentPhotoPath: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            ownerNameText = findViewById(R.id.editTextOwnerName)
            producerText = findViewById(R.id.editTextProducer)
            modelText = findViewById(R.id.editTextModel)
            plateNumberText = findViewById(R.id.editTextPlateNumber)
            carPhoto = findViewById(R.id.carPhoto)
            noPhotoText = findViewById(R.id.noPhotoText)
            buttonApply = findViewById(R.id.buttonApply)
            buttonBack = findViewById(R.id.buttonBack)
            buttonAddPhoto = findViewById(R.id.buttonAddPhoto)

            databaseCarsRepository = DatabaseCarsRepository(context)
            ioScope = CoroutineScope(Dispatchers.IO + Job())

            buttonBack.setOnClickListener { returnToCarListFragment() }
            buttonApply.setOnClickListener { addingCarToDatabaseAndReturnToCarListFragment() }

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

    private fun addingCarToDatabaseAndReturnToCarListFragment() {
        val ownerName = ownerNameText.text.toString()
        val producer = producerText.text.toString()
        val model = modelText.text.toString()
        val plateNumber = plateNumberText.text.toString()

        photoPath = if (newPhotoLoaded) currentPhotoPath else null

        ioScope.launch {
            if (ownerName.isNotEmpty() && producer.isNotEmpty() && model.isNotEmpty() && plateNumber.isNotEmpty()) {
                val car = CarItem(ownerName, producer, model, plateNumber, photoPath)

                try {
                    databaseCarsRepository.addCar(car)
                    returnToCarListFragment()

                } catch (e: Exception) {
                    Snackbar.make(buttonApply, R.string.car_with_this_number_already_exists, Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(buttonApply, R.string.all_fields_must_be_filled, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun returnToCarListFragment() {
        parentFragmentManager.commit {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            addToBackStack(null)
            setReorderingAllowed(true)
            replace(R.id.mainContainer, CarListFragment())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.get("data").run {
            currentPhotoPath = context?.let { createDirectory(it)?.let { saveImage(this as Bitmap, carPhoto, it) }.toString() }.toString()
            newPhotoLoaded = true
        }
    }

}

