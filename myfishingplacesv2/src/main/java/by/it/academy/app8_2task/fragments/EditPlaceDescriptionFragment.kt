package by.it.academy.app8_2task.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import by.it.academy.app8_2task.R
import by.it.academy.app8_2task.customview.FishingStatusCustomView
import by.it.academy.app8_2task.entity.PlaceDescriptionItem
import by.it.academy.app8_2task.repositories.DatabasePlaceDescriptionRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaceDescriptionFragment : Fragment(R.layout.fragment_edit_place_description) {

    private lateinit var buttonBack: ImageButton
    private lateinit var buttonDeletePlaceDescription: ImageButton
    private lateinit var buttonApply: ImageButton
    private lateinit var textViewApplicationDate: TextView
    private lateinit var editTextRoadToWater: EditText
    private lateinit var editTextDistance: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var imageButtonBadBite: ImageButton
    private lateinit var imageButtonNormalBite: ImageButton
    private lateinit var imageButtonGoodBite: ImageButton
    private lateinit var customViewFishingStatus: FishingStatusCustomView
    private lateinit var date: String
    private lateinit var fishSpecies: String
    private lateinit var databasePlaceDescriptionRepository: DatabasePlaceDescriptionRepository
    private lateinit var ioScope: CoroutineScope
    private lateinit var placeDescriptionItem: PlaceDescriptionItem
    private var fishingStatus: Int = ImageButtonBadBiteCode

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            buttonBack = findViewById(R.id.buttonBack)
            buttonDeletePlaceDescription = findViewById(R.id.buttonDeletePlaceDescription)
            buttonApply = findViewById(R.id.buttonApply)
            textViewApplicationDate = findViewById(R.id.textViewApplicationDate)
            editTextRoadToWater = findViewById(R.id.editTextRoadToWater)
            editTextDistance = findViewById(R.id.editTextDistance)
            editTextDescription = findViewById(R.id.editTextDescription)
            imageButtonBadBite = findViewById(R.id.imageButtonBadBite)
            imageButtonNormalBite = findViewById(R.id.imageButtonNormalBite)
            imageButtonGoodBite = findViewById(R.id.imageButtonGoodBite)
            customViewFishingStatus = findViewById(R.id.customViewFishingStatus)

            databasePlaceDescriptionRepository = DatabasePlaceDescriptionRepository(context)
            ioScope = CoroutineScope(Dispatchers.IO)
            getIntentDataAndSetInfoToEditTexts()

            customViewFishingStatus.customClickListenerSetFishingStatus = { status -> fishingStatus = status }

            buttonBack.setOnClickListener { returnToPlaceDescriptionListFragment(bundleOf("placeDescriptionList" to arguments?.get("placeItem"))) }

            buttonDeletePlaceDescription.setOnClickListener { alertDialogDeletePlaceDescription() }

            buttonApply.setOnClickListener {
                val roadToWater= editTextRoadToWater.text.toString()
                val distance = editTextDistance.text.toString()
                val description = editTextDescription.text.toString()
                ioScope.launch {
                    if (roadToWater.isNotEmpty() && distance.isNotEmpty() && description.isNotEmpty()) {
                        val workItem = PlaceDescriptionItem(date, roadToWater, description, distance.toFloat(), fishingStatus, fishSpecies).apply { id = placeDescriptionItem.id }
                        databasePlaceDescriptionRepository.updatePlaceDescription(workItem)
                        returnToPlaceDescriptionListFragment(bundleOf("placeDescriptionList" to arguments?.get("placeItem")))
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

    private fun alertDialogDeletePlaceDescription() {
        val context = context
        if (context != null) {
            val alertDialog = AlertDialog.Builder(context)

            with(alertDialog) {
                setTitle(R.string.warning)
                setMessage(R.string.are_you_sure_wont_to_delete_place_description)
                setPositiveButton(R.string.ok) { _, _ ->
                    ioScope.launch {
                        databasePlaceDescriptionRepository.deletePlaceDescription(placeDescriptionItem)
                        returnToPlaceDescriptionListFragment(bundleOf("placeDescriptionList" to arguments?.get("placeItem")))
                    }
                }
                setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.cancel() }
                create()
                show()
            }
        }

    }

    private fun returnToPlaceDescriptionListFragment(bundle: Bundle) {
        parentFragmentManager.commit {
            addToBackStack(null)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            setReorderingAllowed(true)
            replace(R.id.mainContainer, PlaceDescriptionListFragment::class.java, bundle)
        }
    }

    private fun getIntentDataAndSetInfoToEditTexts() {
        placeDescriptionItem = (arguments?.get("placeDescriptionItem")
                ?: PlaceDescriptionItem("", "", "", 0F, 0, "")) as PlaceDescriptionItem
        date = placeDescriptionItem.applicationDate
        val dateForTextViewApplicationDate = getString(R.string.application_date, date)
        textViewApplicationDate.text = dateForTextViewApplicationDate
        editTextRoadToWater.setText(placeDescriptionItem.roadToWater)
        editTextDistance.setText(placeDescriptionItem.distance.toString())
        editTextDescription.setText(placeDescriptionItem.description)
        fishingStatus = placeDescriptionItem.fishingStatus
        fishSpecies = placeDescriptionItem.fishSpecies

    }
}