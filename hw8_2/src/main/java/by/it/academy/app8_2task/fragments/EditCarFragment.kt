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
import by.it.academy.app8_2task.entity.CarItem
import by.it.academy.app8_2task.functions.createDirectory
import by.it.academy.app8_2task.functions.saveImage
import by.it.academy.app8_2task.repositories.DatabaseCarsRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception

private const val PHOTO_CODE_REQUEST = 10

class EditCarFragment:Fragment(R.layout.fragment_edit_car) {

    private lateinit var currentPhotoPath: String
    private lateinit var carPhotoImageView: ImageView
    private var pathImage: String? = null
    private var newPhotoLoaded = false
    private lateinit var databaseCarsRepository: DatabaseCarsRepository
    private lateinit var ioScope: CoroutineScope

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view){
            val ownerNameEditText = findViewById<EditText>(R.id.editTextOwnerName)
            val producerEditText = findViewById<EditText>(R.id.editTextProducer)
            val modelEditText = findViewById<EditText>(R.id.editTextModel)
            val plateNumberEditText = findViewById<EditText>(R.id.editTextPlateNumber)
            carPhotoImageView = findViewById(R.id.carPhoto)

            databaseCarsRepository = DatabaseCarsRepository(context)
            ioScope = CoroutineScope(Dispatchers.IO + Job())

            val car: CarItem? = arguments?.getParcelable("carItem")
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

            findViewById<ImageButton>(R.id.buttonBack).setOnClickListener { finishActivity() }

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
                    ioScope.launch {
                        if (ownerName.isNotEmpty() && producer.isNotEmpty() && model.isNotEmpty() && plateNumber.isNotEmpty()) {
                            val carItem = CarItem(ownerName, producer, model, plateNumber, pathImage)
                            try {
                                databaseCarsRepository.updateCar(carItem)
                                finishActivity()
                            } catch (e: Exception) {
                                Snackbar.make(carPhotoImageView, getString(R.string.car_with_this_number_already_exists), Snackbar.LENGTH_SHORT).show()
                            }
                        } else {
                            Snackbar.make(carPhotoImageView, getString(R.string.all_fields_must_be_filled), Snackbar.LENGTH_SHORT).show()
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
            replace(R.id.mainContainer, CarListFragment())
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.extras?.get("data").run {
            currentPhotoPath = context?.let { createDirectory(it)?.let { saveImage(this as Bitmap, carPhotoImageView, it) }.toString() }.toString()
            newPhotoLoaded = true

        }
    }
}