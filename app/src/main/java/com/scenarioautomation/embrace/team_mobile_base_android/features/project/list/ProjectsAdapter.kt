package com.scenarioautomation.embrace.team_mobile_base_android.features.project.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scenarioautomation.embrace.team_mobile_base_android.R

class ProjectsAdapter : RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>() {
    private val projects = mutableListOf<ProjectItemDTO>()

    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhoto)
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
        Glide.with(holder.itemView.context)
            .load(project.image)
            .into(holder.ivPhoto)
    }

    fun updateProjects(projects: List<ProjectItemDTO>) {
        val currentSize = this.projects.size
        val newSize = projects.size
        val dif = newSize - currentSize

        this.projects.clear()
        this.projects.addAll(projects)

        if (dif > 0) {
            notifyItemRangeChanged(0, currentSize)
            notifyItemRangeInserted(currentSize, dif)
        } else if (dif < 0) {
            notifyItemRangeChanged(0, newSize)
            notifyItemRangeRemoved(newSize, dif * -1)
        } else
            notifyItemRangeChanged(0, newSize)
    }
}