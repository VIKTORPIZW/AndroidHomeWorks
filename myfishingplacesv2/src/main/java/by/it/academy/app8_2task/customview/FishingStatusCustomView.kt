package by.it.academy.app8_2task.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.LinearLayout
import by.it.academy.app8_2task.R
import by.it.academy.app8_2task.fragments.ImageButtonGoodBiteCode
import by.it.academy.app8_2task.fragments.ImageButtonNormalBiteCode
import by.it.academy.app8_2task.fragments.ImageButtonBadBiteCode


class FishingStatusCustomView(context: Context?, attrs: AttributeSet) : LinearLayout(context, attrs) {
    lateinit var customClickListenerSetFishingStatus: (workStatus: Int) -> Unit

    init {
        inflate(context, R.layout.item_fishing_status_custom_view, this)

        val imageButtonBadBite: ImageButton = findViewById(R.id.imageButtonBadBite)
        val imageButtonInProgress: ImageButton = findViewById(R.id.imageButtonNormalBite)
        val imageButtonCompleted: ImageButton = findViewById(R.id.imageButtonGoodBite)

        imageButtonBadBite.setImageResource(R.drawable.fishing)
        imageButtonInProgress.setImageResource(R.drawable.fishing)
        imageButtonCompleted.setImageResource(R.drawable.fishing)

        imageButtonBadBite.setOnClickListener {
            imageButtonBadBite.setImageResource(R.drawable.fish3)
            imageButtonInProgress.setImageResource(R.drawable.fishing)
            imageButtonCompleted.setImageResource(R.drawable.fishing)
            customClickListenerSetFishingStatus(ImageButtonBadBiteCode)
        }

        imageButtonInProgress.setOnClickListener {
            imageButtonInProgress.setImageResource(R.drawable.fish2)
            imageButtonBadBite.setImageResource(R.drawable.fishing)
            imageButtonCompleted.setImageResource(R.drawable.fishing)
            customClickListenerSetFishingStatus(ImageButtonNormalBiteCode)
        }

        imageButtonCompleted.setOnClickListener {
            imageButtonBadBite.setImageResource(R.drawable.fishing)
            imageButtonInProgress.setImageResource(R.drawable.fishing)
            imageButtonCompleted.setImageResource(R.drawable.fish1)
            customClickListenerSetFishingStatus(ImageButtonGoodBiteCode)
        }
    }

}