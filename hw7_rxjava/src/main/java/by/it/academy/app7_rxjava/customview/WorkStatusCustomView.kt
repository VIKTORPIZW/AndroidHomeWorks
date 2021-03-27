package by.it.academy.app7_rxjava.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.LinearLayout
import by.it.academy.app7_rxjava.R
import by.it.academy.app7_rxjava.activity.ImageButtonCompletedCode
import by.it.academy.app7_rxjava.activity.ImageButtonInProgressCode
import by.it.academy.app7_rxjava.activity.ImageButtonPendingCode


class WorkStatusCustomView(context: Context?, attrs: AttributeSet) : LinearLayout(context, attrs) {
    lateinit var customClickListenerSetWorkStatus: (workStatus: Int) -> Unit

    init {
        inflate(context, R.layout.item_work_status_custom_view, this)

        val imageButtonPending: ImageButton = findViewById(R.id.imageButtonPending)
        val imageButtonInProgress: ImageButton = findViewById(R.id.imageButtonInProgress)
        val imageButtonCompleted: ImageButton = findViewById(R.id.imageButtonCompleted)

        imageButtonPending.setImageResource(R.drawable.ic_pending)
        imageButtonInProgress.setImageResource(R.drawable.ic_in_progress_gray)
        imageButtonCompleted.setImageResource(R.drawable.ic_completed_gray)

        imageButtonPending.setOnClickListener {
            imageButtonPending.setImageResource(R.drawable.ic_pending)
            imageButtonInProgress.setImageResource(R.drawable.ic_in_progress_gray)
            imageButtonCompleted.setImageResource(R.drawable.ic_completed_gray)
            customClickListenerSetWorkStatus(ImageButtonPendingCode)
        }

        imageButtonInProgress.setOnClickListener {
            imageButtonInProgress.setImageResource(R.drawable.ic_in_progress)
            imageButtonPending.setImageResource(R.drawable.ic_pending_gray)
            imageButtonCompleted.setImageResource(R.drawable.ic_completed_gray)
            customClickListenerSetWorkStatus(ImageButtonInProgressCode)
        }

        imageButtonCompleted.setOnClickListener {
            imageButtonPending.setImageResource(R.drawable.ic_pending_gray)
            imageButtonInProgress.setImageResource(R.drawable.ic_in_progress_gray)
            imageButtonCompleted.setImageResource(R.drawable.ic_completed)
            customClickListenerSetWorkStatus(ImageButtonCompletedCode)
        }
    }

}