package com.hk.postphoto

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import java.util.ArrayList

object VideoGallery {
    @JvmStatic
    fun listOfVideos(context: Context): ArrayList<String> {
        val uri: Uri
        val cursor: Cursor?
        val column_index_data: Int
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID
        )
        val orderBy = MediaStore.Video.Media.DATE_TAKEN
        cursor = context.contentResolver.query(
            uri, projection, null,
            null, "$orderBy DESC"
        )
        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }
}