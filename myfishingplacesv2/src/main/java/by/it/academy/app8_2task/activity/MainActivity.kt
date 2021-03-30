package by.it.academy.app8_2task.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.it.academy.app8_2task.R
import by.it.academy.app8_2task.fragments.PlaceListFragment
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        writeDateToLogFile()
        showPlaceListFragment()
    }
    private fun showPlaceListFragment() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.mainContainer, PlaceListFragment())
                .setReorderingAllowed(true).commit()
    }
    private fun writeDateToLogFile() {
        val dateTimeInstance = SimpleDateFormat.getDateTimeInstance()

        openFileOutput("logWithDate.txt", MODE_APPEND).apply {
            val date = dateTimeInstance.format(Date())
            write(date.toByteArray())
            close()
        }
    }
}