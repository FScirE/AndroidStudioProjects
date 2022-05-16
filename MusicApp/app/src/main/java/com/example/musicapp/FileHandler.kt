package com.example.musicapp

import android.R.attr.data
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.File
import java.io.InputStream
import java.util.*


abstract class FileHandler {
    companion object {
        var currentUri = Uri.EMPTY

        fun getFileName(context: Context): String {
            var fileName = ""
            val cr = context.contentResolver
            val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
            val metaCursor: Cursor = cr.query(currentUri, projection, null, null, null)!!
            metaCursor.use { metaCursor ->
                if (metaCursor.moveToFirst()) {
                    fileName = metaCursor.getString(0)
                }
            }
            return fileName
        }
    }
}