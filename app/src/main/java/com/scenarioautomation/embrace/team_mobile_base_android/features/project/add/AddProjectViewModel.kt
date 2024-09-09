package com.scenarioautomation.embrace.team_mobile_base_android.features.project.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scenarioautomation.embrace.team_mobile_base_android.service.ProjectDataService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
        viewModelScope.launch(Dispatchers.IO) {
            projectDataService.saveNewProject(name, photoUri)
            _state.postValue(AddProjectState.SUCCESS)
        }
    }
}