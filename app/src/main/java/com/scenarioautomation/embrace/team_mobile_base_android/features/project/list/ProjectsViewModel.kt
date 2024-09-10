package com.scenarioautomation.embrace.team_mobile_base_android.features.project.list

import androidx.lifecycle.ViewModel
import com.scenarioautomation.embrace.team_mobile_base_android.service.ProjectDataService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(private val projectDataService: ProjectDataService) :
    ViewModel() {
    suspend fun listenProjects(): Flow<List<ProjectItemDTO>> {
        return projectDataService.listenProjects()
    }
}