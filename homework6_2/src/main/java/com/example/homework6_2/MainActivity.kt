package com.example.homework6_2

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private val imageList = mutableListOf<ImageItem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageItemAdapter: ImageItemAdapter

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.images_list_recycler)
        imageItemAdapter = ImageItemAdapter(imageList)
        recyclerView.adapter = imageItemAdapter
        recyclerView.layoutManager  = GridLayoutManager(this, 3)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1);
        } else{
            getAllImages()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getAllImages() {

        val imageSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        val cursor: Cursor? = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, imageSortOrder)
        cursor?.moveToFirst()
        while (cursor!!.moveToNext()) {
            val idColumn = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID))
            val fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
            val uriImage = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + idColumn)
            imageList.add(ImageItem(uriImage.toString(),fileName))
        }
        cursor.close()
    }
}
