package dev.mbo.gradlestatusplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleStatusPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val task = project.tasks.create("gather", GatherTask::class.java)
        task.group = "status"
    }

}