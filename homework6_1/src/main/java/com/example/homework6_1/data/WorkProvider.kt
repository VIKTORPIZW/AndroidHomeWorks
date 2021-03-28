package com.example.homework6_1.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log

class WorkProvider : ContentProvider() {
    private var workDatabase: CarDatabase? = null
    companion object {
        private const val AUTHORITY = "com.example.homework6_1.data.WorkProvider"
        private const val WORK_PATH = "database"
        private const val CODE_WORK_ITEM = 1
        private val matcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(
                    AUTHORITY,
                    WORK_PATH,
                    CODE_WORK_ITEM)
        }
    }

    override fun onCreate(): Boolean {
        workDatabase = context?.run { CarDatabase.getDatabase(this.applicationContext) }
        return true
    }

    override fun query(
            uri: Uri,
            projection: Array<out String>?,
            selection: String?,
            selectionArgs: Array<out String>?,
            sortOrder: String?
    ): Cursor? =
            if (matcher.match(uri) == CODE_WORK_ITEM) {
            workDatabase?.getWorkDAO()?.getWorkProvider()
        } else {
            null
        }

    override fun getType(uri: Uri): String? {
        val string = ""
        Log.v("Content_Provider", "getType")
        return string
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.v("Content_Provider", "insert")
        return Uri.EMPTY
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val x = 0
        Log.v("Content_Provider", "delete")
        return x
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val x = 0
        Log.v("Content_Provider", "update")
        return x
    }
}