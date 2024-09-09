package com.scenarioautomation.embrace.team_mobile_base_android.service

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.scenarioautomation.embrace.team_mobile_base_android.domain.Project
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectDataService @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    companion object {
        private const val PHOTOS_FOLDER = "PHOTOS"
    }

    fun saveNewProject(name: String, photo: Uri): Project? {
        val originFile = getFileFromURI(photo) ?: return null
        originFile.copyTo(File(File(appContext.filesDir, PHOTOS_FOLDER), "1.img"), true)
        return Project(name, "1.img")
    }

    private fun getFileFromURI(contentUri: Uri): File? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = appContext.contentResolver.query(contentUri, proj, null, null, null)
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

}