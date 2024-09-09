package com.scenarioautomation.embrace.team_mobile_base_android.features.project.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddProjectViewModel : ViewModel() {

    private val _state = MutableLiveData(AddProjectState.NONE)
    val state: LiveData<AddProjectState> = _state

    fun addProject(name: String?, photoUri: Uri?) {
        if ((name?.isBlank() != false) || (photoUri == null))
            _state.value = AddProjectState.ERROR
        else
            _state.value = AddProjectState.SUCCESS
    }
}