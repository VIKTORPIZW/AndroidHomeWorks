package com.example.homework6_1

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.homework6_1.R
import com.example.homework6_1.data.Car
import com.example.homework6_1.data.CarDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditCar : AppCompatActivity() {
    private lateinit var photoImageViewInEdit: ImageView
    private lateinit var editOwnerNameInEditCar: EditText
    private lateinit var editProducerInEditCar: EditText
    private lateinit var editModelInEditCar: EditText
    private lateinit var editPlateNumberInEditCar: EditText
    private lateinit var buttonApplyInEditCar: ImageButton
    private lateinit var buttonBackToCarList: ImageButton
    private lateinit var buttonEditImageInEditCar: FloatingActionButton

    private lateinit var dao: CarDatabase
    private var car: Car? = null
    private var carId: Int = 0
    private val carID = "carID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_car)

        photoImageViewInEdit = findViewById(R.id.imageViewCarInEditCarLayout)
        editOwnerNameInEditCar = findViewById(R.id.editOwnerNameInEditCarLayout)
        editProducerInEditCar = findViewById(R.id.editProducerInEditCarLayout)
        editModelInEditCar = findViewById(R.id.editModelInEditCarLayout)
        editPlateNumberInEditCar = findViewById(R.id.editNumberInEditCarLayout)
        buttonApplyInEditCar = findViewById(R.id.checkButtonEditCarListActivity)
        buttonEditImageInEditCar = findViewById(R.id.photoFloatingButtonInEditCarLayout)
        buttonBackToCarList = findViewById(R.id.backButtonEditCarListActivity)

        dao = CarDatabase.getDatabase(this)
        getCarExtra(intent)
        if (car != null) {
            setTextInView()
        }
        buttonApplyInEditCar.setOnClickListener {
            val intentUpdateCar = Intent(this, MainActivity::class.java)
            val updateCar = updateCarObject().also { it.id = carId }
            dao.getCarDAO().update(updateCar)
            setResult(RESULT_OK, intentUpdateCar)
            finish()
        }
        buttonBackToCarList.setOnClickListener {
            finish()
        }
    }

    private fun setTextInView() {
        photoImageViewInEdit.setImageResource(R.drawable.ic_baseline_arrow_forward_24)
        car?.let {
//            photoImageViewInEdit.setImage(it.photo)
            editOwnerNameInEditCar.setText(it.nameOwner)
            editProducerInEditCar.setText(it.producer)
            editModelInEditCar.setText(it.model)
            editPlateNumberInEditCar.setText(it.registerNumber)
        }
    }

    private fun getCarExtra(intent: Intent) {
        carId = intent.getIntExtra(carID, 0)
        car = dao.getCarDAO().getCar(carId)
    }

    private fun updateCarObject() =
            Car(
                    nameOwner = editOwnerNameInEditCar.text.toString(),
                    producer = editProducerInEditCar.text.toString(),
                    model = editModelInEditCar.text.toString(),
                    registerNumber = editPlateNumberInEditCar.text.toString(),
                    photo = car?.photo
            )
}