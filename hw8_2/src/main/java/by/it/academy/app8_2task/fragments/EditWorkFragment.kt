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
import by.it.academy.app8_2task.customview.WorkStatusCustomView
import by.it.academy.app8_2task.entity.WorkItem
import by.it.academy.app8_2task.repositories.DatabaseWorksRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditWorkFragment : Fragment(R.layout.fragment_edit_work) {

    private lateinit var buttonBack: ImageButton
    private lateinit var buttonDelete: ImageButton
    private lateinit var buttonApply: ImageButton
    private lateinit var textViewApplicationDate: TextView
    private lateinit var editTextWorkName: EditText
    private lateinit var editTextCost: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var imageButtonPending: ImageButton
    private lateinit var imageButtonInProgress: ImageButton
    private lateinit var imageButtonCompleted: ImageButton
    private lateinit var workStatusCustomView: WorkStatusCustomView
    private lateinit var date: String
    private lateinit var carPlate: String
    private lateinit var databaseWorksRepository: DatabaseWorksRepository
    private lateinit var ioScope: CoroutineScope
    private lateinit var workItem: WorkItem
    private var workStatus: Int = ImageButtonPendingCode

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            buttonBack = findViewById(R.id.buttonBack)
            buttonDelete = findViewById(R.id.buttonDeleteWork)
            buttonApply = findViewById(R.id.buttonApply)
            textViewApplicationDate = findViewById(R.id.textViewApplicationDate)
            editTextWorkName = findViewById(R.id.editTextWorkName)
            editTextCost = findViewById(R.id.editTextCost)
            editTextDescription = findViewById(R.id.editTextDescription)
            imageButtonPending = findViewById(R.id.imageButtonPending)
            imageButtonInProgress = findViewById(R.id.imageButtonInProgress)
            imageButtonCompleted = findViewById(R.id.imageButtonCompleted)
            workStatusCustomView = findViewById(R.id.customViewWorkStatus)

            databaseWorksRepository = DatabaseWorksRepository(context)
            ioScope = CoroutineScope(Dispatchers.IO)
            getIntentDataAndSetInfoToEditTexts()

            workStatusCustomView.customClickListenerSetWorkStatus = { status -> workStatus = status }

            buttonBack.setOnClickListener { returnToCarWorkListFragment(bundleOf("workList" to arguments?.get("carItem"))) }

            buttonDelete.setOnClickListener { alertDialogDeleteWork() }

            buttonApply.setOnClickListener {
                val workName = editTextWorkName.text.toString()
                val cost = editTextCost.text.toString()
                val description = editTextDescription.text.toString()
                ioScope.launch {
                    if (workName.isNotEmpty() && cost.isNotEmpty() && description.isNotEmpty()) {
                        val workItem = WorkItem(date, workName, description, cost.toFloat(), workStatus, carPlate).apply { id = workItem.id }
                        databaseWorksRepository.updateWork(workItem)
                        returnToCarWorkListFragment(bundleOf("workList" to arguments?.get("carItem")))
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

    private fun alertDialogDeleteWork() {
        val context = context
        if (context != null) {
            val alertDialog = AlertDialog.Builder(context)

            with(alertDialog) {
                setTitle(R.string.warning)
                setMessage(R.string.are_you_sure_wont_to_delete_work)
                setPositiveButton(R.string.ok) { _, _ ->
                    ioScope.launch {
                        databaseWorksRepository.deleteWork(workItem)
                        returnToCarWorkListFragment(bundleOf("workList" to arguments?.get("carItem")))
                    }
                }
                setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.cancel() }
                create()
                show()
            }
        }

    }

    private fun returnToCarWorkListFragment(bundle: Bundle) {
        parentFragmentManager.commit {
            addToBackStack(null)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            setReorderingAllowed(true)
            replace(R.id.mainContainer, CarWorkListFragment::class.java, bundle)
        }
    }

    private fun getIntentDataAndSetInfoToEditTexts() {
        workItem = (arguments?.get("workItem")
                ?: WorkItem("", "", "", 0F, 0, "")) as WorkItem
        date = workItem.applicationDate
        val dateForTextViewApplicationDate = getString(R.string.application_date, date)
        textViewApplicationDate.text = dateForTextViewApplicationDate
        editTextWorkName.setText(workItem.workType)
        editTextCost.setText(workItem.cost.toString())
        editTextDescription.setText(workItem.description)
        workStatus = workItem.workStatus
        carPlate = workItem.carPlate

    }
}