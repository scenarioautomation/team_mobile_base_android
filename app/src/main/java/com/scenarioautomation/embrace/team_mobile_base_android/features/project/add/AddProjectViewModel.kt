package com.scenarioautomation.embrace.team_mobile_base_android.features.project.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scenarioautomation.embrace.team_mobile_base_android.service.ProjectDataService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProjectViewModel @Inject constructor(private val projectDataService: ProjectDataService) :
    ViewModel() {

    private val _state = MutableLiveData(AddProjectState.NONE)
    val state: LiveData<AddProjectState> = _state

    fun addProject(name: String?, photoUri: Uri?) {
        if ((name?.isBlank() != false) || (photoUri == null)) {
            _state.value = AddProjectState.ERROR
            return
        }
        _state.value = AddProjectState.SALVING
        viewModelScope.launch {
            _state.value =
                if (projectDataService.saveNewProject(name, photoUri) != null)
                    AddProjectState.SUCCESS else AddProjectState.ERROR
        }
    }
}