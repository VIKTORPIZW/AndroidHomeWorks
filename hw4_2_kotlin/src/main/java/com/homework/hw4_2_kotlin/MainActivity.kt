package com.homework.hw4_2_kotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val customView: CustomButtonView = findViewById(R.id.customView)

        customView.customListener = object : ICustomListener {
            override fun notification(colorOfClick: Int, message: String?, isSnackBar: Boolean) {
                if (isSnackBar) {
                    val snackBar = Snackbar.make(customView,
                            message.toString(),
                            Snackbar.LENGTH_SHORT)
                    snackBar.setTextColor(colorOfClick).show()
                } else {
                    Toast.makeText(applicationContext,
                            customView.coordinates(),
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
        val switchMaterial = findViewById<SwitchMaterial>(R.id.toastOrSnackBarSwitch)

        customView.setOnClickListener {
            (customView.customListener as ICustomListener).notification(
                    customView.getColorOfClick(),
                    customView.coordinates(),
                    switchMaterial.isChecked)
        }
    }
}
