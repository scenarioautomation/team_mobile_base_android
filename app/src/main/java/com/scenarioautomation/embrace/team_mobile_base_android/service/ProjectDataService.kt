package com.scenarioautomation.embrace.team_mobile_base_android.service

import android.content.Context
import android.net.Uri
import com.scenarioautomation.embrace.team_mobile_base_android.domain.Project
import com.scenarioautomation.embrace.team_mobile_base_android.domain.Projects
import com.scenarioautomation.embrace.team_mobile_base_android.utils.getFileFromURI
import com.scenarioautomation.embrace.team_mobile_base_android.utils.readAll
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectDataService @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val jsonService: JsonService
) {

    private var completableProjects: CompletableDeferred<MutableList<Project>>? = null


    companion object {
        private const val PHOTOS_FOLDER = "PHOTOS"
        private const val PROJECTS_FILE = "projects.json"
    }

    suspend fun loadProjects(): MutableList<Project> {
        completableProjects?.let {
            return it.await()
        }
        completableProjects = CompletableDeferred()
        return withContext(Dispatchers.IO) {
            try {
                val projectsFile = FileInputStream(File((appContext.filesDir), PROJECTS_FILE))
                val projectsJson = String(projectsFile.readAll(), Charsets.UTF_8)
                val projects =
                    jsonService.parseFromJson<Projects>(projectsJson)?.projects ?: mutableListOf()
                completableProjects?.complete(projects)
                return@withContext projects
            } catch (e: Throwable) {
                return@withContext mutableListOf()
            }
        }
    }

    suspend fun saveNewProject(name: String, photo: Uri): Project? {
        return withContext(Dispatchers.IO) {
            val originFile = photo.getFileFromURI(appContext) ?: return@withContext null
            originFile.copyTo(File(File(appContext.filesDir, PHOTOS_FOLDER), "1.img"), true)
            Project(1, name, "1.img")
        }
    }

}