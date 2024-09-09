package com.scenarioautomation.embrace.team_mobile_base_android.features.project.list

import androidx.lifecycle.ViewModel
import com.scenarioautomation.embrace.team_mobile_base_android.domain.Project
import com.scenarioautomation.embrace.team_mobile_base_android.service.ProjectDataService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(private val projectDataService: ProjectDataService) :
    ViewModel() {
    suspend fun listProjects(): List<Project> {
        return projectDataService.loadProjects()
    }
}