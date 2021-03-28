package by.it.academy.app8_2task.functions

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.widget.ImageView
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

fun createDirectory(applicationContext: Context): File? {
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        File("${applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/PlacePictures")
                .also {
                    if (!it.exists()) {
                        it.mkdir()
                    }
                }
    }
    return null
}

fun saveImage(photo: Bitmap, imageView: ImageView, placePictureDirectory: File): String {
    val path = "photo_${System.currentTimeMillis()}.jpg"
    val pathToPicture = "${placePictureDirectory.path}/${path}"
    val file = File(placePictureDirectory, path)
    file.createNewFile()
    val stream = FileOutputStream(file)
    photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    imageView.setImageBitmap(photo)
    stream.flush()
    stream.close()
    return pathToPicture
}

fun Date.parseToString(): String = SimpleDateFormat.getDateInstance().format(this)

fun getCurrentDate(): String {
    return Date().parseToString()
}


