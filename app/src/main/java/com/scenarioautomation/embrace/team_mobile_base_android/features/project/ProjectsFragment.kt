package com.scenarioautomation.embrace.team_mobile_base_android.features.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.scenarioautomation.embrace.team_mobile_base_android.R
import kotlinx.coroutines.launch

class ProjectsFragment : Fragment() {

    companion object {
        fun newInstance() = ProjectsFragment()
    }

    private val viewModel: ProjectsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_projects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvProjects = view.findViewById<RecyclerView>(R.id.rvProjects)
        viewLifecycleOwner.lifecycleScope.launch {
            rvProjects.adapter = ProjectsAdapter(viewModel.listProjects())
        }
    }
}