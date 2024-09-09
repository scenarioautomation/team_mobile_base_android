package com.scenarioautomation.embrace.team_mobile_base_android.features.project

import androidx.lifecycle.ViewModel
import com.scenarioautomation.embrace.team_mobile_base_android.domain.Project

class ProjectsViewModel : ViewModel() {
    suspend fun listProjects(): List<Project> {
        return listOf(Project("Cidade", ""), Project("Praia", ""))
    }
}