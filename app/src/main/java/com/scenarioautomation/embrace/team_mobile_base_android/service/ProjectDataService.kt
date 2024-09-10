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
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectDataService @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val jsonService: JsonService
) {

    private var completableProjects: CompletableDeferred<List<Project>>? = null

    companion object {
        private const val PHOTOS_FOLDER = "PHOTOS"
        private const val PROJECTS_FILE = "projects.json"
    }

    suspend fun getProjects(): List<Project> {
        completableProjects?.let {
            return it.await()
        }
        completableProjects = CompletableDeferred()
        val json = withContext(Dispatchers.IO) {
            try {
                val projectsFile = FileInputStream(File((appContext.filesDir), PROJECTS_FILE))
                return@withContext String(projectsFile.readAll(), Charsets.UTF_8)
            } catch (e: Throwable) {
                return@withContext ""
            }
        }

        val projects =
            (jsonService.parseFromJson<Projects>(json)?.projects ?: listOf()).sortedBy { it.id }
        completableProjects?.complete(projects)
        return projects
    }

    suspend fun saveNewProject(name: String, photo: Uri): Boolean {
        val projects = getProjects()
        val nextId = (projects.lastOrNull()?.id ?: 0) + 1

        val originFile = photo.getFileFromURI(appContext) ?: return false
        val copyImageSuccess = withContext(Dispatchers.IO) {
            try {
                originFile.copyTo(
                    File(File(appContext.filesDir, PHOTOS_FOLDER), "$nextId.img"),
                    true
                )
                true
            } catch (_: Throwable) {
                false
            }
        }
        if (!copyImageSuccess) return false

        val newProjects = Projects(projects + listOf(Project(nextId, name)))
        val json = jsonService.parseToJson(newProjects) ?: return false
        val saveProjectsSuccess = withContext(Dispatchers.IO) {
            try {
                val projectsFile = FileOutputStream(File((appContext.filesDir), PROJECTS_FILE))
                projectsFile.write(json.toByteArray(Charsets.UTF_8))
                true
            } catch (_: Throwable) {
                false
            }
        }
        return saveProjectsSuccess
    }

}