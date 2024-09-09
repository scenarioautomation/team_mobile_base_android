package com.scenarioautomation.embrace.team_mobile_base_android.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File

fun Uri.getFileFromURI(context: Context): File? {
    var res: String? = null
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(this, proj, null, null, null)
    if (cursor?.moveToFirst() == true) {
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        res = cursor.getString(columnIndex)
    }
    cursor?.close()
    res?.let {
        return File(it)
    }
    return null
}
