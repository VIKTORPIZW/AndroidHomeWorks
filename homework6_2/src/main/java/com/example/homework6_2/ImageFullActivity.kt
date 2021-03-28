package com.example.homework6_2

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ImageFullActivity: AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var fileName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image)

        findViewById<View>(R.id.arrow_back).setOnClickListener { finish() }

        imageView = findViewById(R.id.full_image)
        fileName = findViewById(R.id.toolbar_text_file_name)

        getImage()
    }

    private fun getImage(){
        val path = intent.getStringExtra("filePath")
        Glide.with(this)
                .load(path)
                .into(imageView)
        val name = intent.getStringExtra("fileName")
        fileName.text = name
    }
}