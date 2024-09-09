package com.scenarioautomation.embrace.team_mobile_base_android.features.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.scenarioautomation.embrace.team_mobile_base_android.R
import com.scenarioautomation.embrace.team_mobile_base_android.domain.Project

class ProjectsAdapter(private val projects: List<Project>) :
    RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>() {

    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        return ProjectViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.project_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.tvName.text = project.name
    }
}