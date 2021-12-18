package dev.mbo.gradlestatusplugin

import com.fasterxml.jackson.databind.ObjectMapper
import dev.mbo.gradlestatusplugin.model.Project
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

open class GatherTask : DefaultTask() {

    private val jsonMapper = ObjectMapper()
    private val uploader = Uploader()

    @get:Input
    @get:Option(option = "upload-url", description = "The url to send the result to.")
    var uploadUrl: String? = null

    @TaskAction
    fun run() {
        logger.info("gather task starting")
        val url = nullsafeUploadUrl()
        val project = Project.createFrom(project, logger)
        logger.info("meta: {}", project)
        val json = jsonMapper.writeValueAsString(project)
        uploader.upload(url, json, logger)
        logger.info("gather task done")
    }

    private fun nullsafeUploadUrl(): String {
        if (null == uploadUrl) {
            throw GradleException("uploadUrl not configured")
        }
        return uploadUrl!!
    }

}