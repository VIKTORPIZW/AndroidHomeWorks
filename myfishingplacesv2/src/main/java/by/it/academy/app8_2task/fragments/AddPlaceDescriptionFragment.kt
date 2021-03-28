package by.it.academy.app8_2task.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import by.it.academy.app8_2task.R
import by.it.academy.app8_2task.customview.FishingStatusCustomView
import by.it.academy.app8_2task.entity.PlaceDescriptionItem
import by.it.academy.app8_2task.functions.getCurrentDate
import by.it.academy.app8_2task.repositories.DatabasePlaceDescriptionRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val ImageButtonBadBiteCode = 1
const val ImageButtonNormalBiteCode = 2
const val ImageButtonGoodBiteCode = 3

class AddPlaceDescriptionFragment : Fragment(R.layout.fragment_add_place_description) {

    private lateinit var buttonBack: ImageButton
    private lateinit var buttonApply: ImageButton
    private lateinit var textViewApplicationDate: TextView
    private lateinit var editTextRoadToWater: EditText
    private lateinit var editTextDistance: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var imageButtonBadBite: ImageButton
    private lateinit var imageButtonNormalBite: ImageButton
    private lateinit var imageButtonGoodBite: ImageButton
    private lateinit var date: String
    private lateinit var fishSpecies: String
    private lateinit var databasePlaceDescriptionRepository: DatabasePlaceDescriptionRepository
    private lateinit var ioScope: CoroutineScope
    private lateinit var fishingStatusCustomView: FishingStatusCustomView
    private var fishingStatus: Int = ImageButtonBadBiteCode

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            buttonBack = findViewById(R.id.buttonBack)
            buttonApply = findViewById(R.id.buttonApply)
            textViewApplicationDate = findViewById(R.id.textViewApplicationDate)
            editTextRoadToWater = findViewById(R.id.editTextRoadToWater)
            editTextDistance = findViewById(R.id.editTextDistance)
            editTextDescription = findViewById(R.id.editTextDescription)
            imageButtonBadBite = findViewById(R.id.imageButtonBadBite)
            imageButtonNormalBite = findViewById(R.id.imageButtonNormalBite)
            imageButtonGoodBite = findViewById(R.id.imageButtonGoodBite)
            fishingStatusCustomView = findViewById(R.id.customViewFishingStatus)

            databasePlaceDescriptionRepository = DatabasePlaceDescriptionRepository(context)
            ioScope = CoroutineScope(Dispatchers.IO)
            fishSpecies = arguments?.getString("fishSpecies").toString()

            setCurrentDateInTextView()

            fishingStatusCustomView.customClickListenerSetFishingStatus = { status -> fishingStatus = status }

            buttonBack.setOnClickListener { returnToPlaceDescriptionListFragment(bundleOf("place_description_List" to arguments?.get("placeItem"))) }

            buttonApply.setOnClickListener {
                val roadToWater = editTextRoadToWater.text.toString()
                val distance = editTextDistance.text.toString()
                val description = editTextDescription.text.toString()
                ioScope.launch {
                    if (roadToWater.isNotEmpty() && distance.isNotEmpty() && description.isNotEmpty()) {

                        val workItem = PlaceDescriptionItem(date, roadToWater, description, distance.toFloat(), fishingStatus, fishSpecies)
                        databasePlaceDescriptionRepository.addPlaceDescription(workItem)
                        returnToPlaceDescriptionListFragment(bundleOf("place_description_List" to arguments?.get("placeItem")))
                    } else {
                        Snackbar.make(buttonApply, getString(R.string.all_fields_must_be_filled), Snackbar.LENGTH_SHORT).show()
                    }
                }
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

    private fun returnToPlaceDescriptionListFragment(bundle: Bundle) {
        parentFragmentManager.commit {
            addToBackStack(null)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            setReorderingAllowed(true)
            replace(R.id.mainContainer, PlaceDescriptionListFragment::class.java, bundle)
        }
    }

    private fun setCurrentDateInTextView() {
        date = getCurrentDate()
        val dateForTextView = getString(R.string.application_date, date)
        textViewApplicationDate.text = dateForTextView
    }


}