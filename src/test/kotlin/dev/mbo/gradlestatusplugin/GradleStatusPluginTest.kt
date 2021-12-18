package dev.mbo.gradlestatusplugin

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GradleStatusPluginTest {

    @Test
    fun test() {
        val pluginProject: Project = ProjectBuilder.builder().build()
        pluginProject.pluginManager.apply("dev.mbo.gradlestatusplugin")
        val task = pluginProject.tasks.findByName("gather")
        assertNotNull(task)
        assertTrue(task is GatherTask)
        assertEquals("status", task!!.group)
    }

}