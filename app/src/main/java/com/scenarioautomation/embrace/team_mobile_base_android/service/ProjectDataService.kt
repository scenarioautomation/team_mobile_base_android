package com.scenarioautomation.embrace.team_mobile_base_android.service

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.scenarioautomation.embrace.team_mobile_base_android.domain.Project
import com.scenarioautomation.embrace.team_mobile_base_android.domain.Projects
import com.scenarioautomation.embrace.team_mobile_base_android.features.project.list.ProjectItemDTO
import com.scenarioautomation.embrace.team_mobile_base_android.utils.readAll
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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

    private var completableProjects: CompletableDeferred<MutableList<Project>>? = null

    private var projectsFlow = MutableStateFlow<List<ProjectItemDTO>>(listOf())

    companion object {
        private const val PHOTOS_FOLDER = "PHOTOS"
        private const val PROJECTS_FILE = "projects.json"
        private const val IMAGE_NAME = "%s.img"
    }

    suspend fun listenProjects(): Flow<List<ProjectItemDTO>> {
        notifyProjects(getProjects())
        return projectsFlow
    }

    suspend fun saveNewProject(name: String, photo: Uri): Boolean {
        val projects = getProjects()
        val nextId = (projects.lastOrNull()?.id ?: 0) + 1

        val copyImageSuccess = withContext(Dispatchers.IO) {
            try {
                val photosFolder = File(appContext.filesDir, PHOTOS_FOLDER)
                photosFolder.mkdirs()
                val newPhotoFile = File(photosFolder, String.format(IMAGE_NAME, nextId))
                if (!newPhotoFile.createNewFile()) return@withContext false

                val photoInputStream =
                    appContext.contentResolver.openInputStream(photo) ?: return@withContext false

                val newPhotoOutputStream = FileOutputStream(newPhotoFile)
                val buffer = ByteArray(2048)
                var count = photoInputStream.read(buffer)
                while (count > 0) {
                    newPhotoOutputStream.write(buffer.copyOf(count))
                    newPhotoOutputStream.flush()
                    count = photoInputStream.read(buffer)
                }

                newPhotoOutputStream.close()
                photoInputStream.close()
                true
            } catch (e: Throwable) {
                e.printStackTrace()
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
        if (!saveProjectsSuccess) return false

        completableProjects?.await()?.let {
            it.clear()
            it.addAll(newProjects.projects)
        }

        notifyProjects(newProjects.projects)

        return true
    }

    private suspend fun getProjects(): List<Project> {
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
        completableProjects?.complete(projects.toMutableList())

        return projects
    }

    private suspend fun notifyProjects(projects: List<Project>) {
        projectsFlow.emit(projects.map {
            ProjectItemDTO(
                it.name,
                File(
                    File(appContext.filesDir, PHOTOS_FOLDER),
                    String.format(IMAGE_NAME, it.id)
                ).toUri()
            )
        })
    }
}