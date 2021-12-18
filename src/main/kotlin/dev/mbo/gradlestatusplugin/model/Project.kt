package dev.mbo.gradlestatusplugin.model

import org.apache.commons.lang3.StringUtils
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.internal.artifacts.result.DefaultResolvedComponentResult
import org.gradle.api.internal.artifacts.result.DefaultResolvedDependencyResult
import org.gradle.api.internal.artifacts.result.DefaultUnresolvedComponentResult
import org.gradle.api.internal.artifacts.result.DefaultUnresolvedDependencyResult
import org.slf4j.Logger

data class Project constructor(
    val group: String?,
    val name: String,
    val version: String?,
    val dependencies: MutableMap<String, Dependency> = mutableMapOf(),
    val subprojects: MutableSet<Project> = mutableSetOf(),
) {

    companion object {
        fun createFrom(project: org.gradle.api.Project, logger: Logger, projectVersion: String? = null): Project {
            val result = Project(
                project.group.toString(),
                project.name,
                projectVersion ?: project.version.toString(),
            )

            project.configurations.forEach { config ->
                if (config.isCanBeResolved) {
                    val deps = config.incoming.resolutionResult.allDependencies
                    deps.forEach {
                        val id: String = when (it) {
                            is DefaultResolvedComponentResult -> it.id.displayName
                            is DefaultUnresolvedComponentResult -> it.requested.displayName

                            is DefaultResolvedDependencyResult -> it.selected.id.displayName
                            is DefaultUnresolvedDependencyResult -> it.requested.displayName

                            else -> ""
                        }
                        if (id.isNotBlank()) {
                            val split = id.split(":")
                            if (split.size == 3) {
                                val entry = result.dependencies.getOrElse(id) {
                                    Dependency(split[0], split[1], split[2], false)
                                }
                                entry.addConfiguration(config.name)
                                result.dependencies[id] = entry
                            }
                        }
                    }
                }

                config.allDependencies.filterIsInstance<ProjectDependency>().forEach { dependency ->
                    val version = projectVersion ?: project.version.toString()
                    val id = ("${dependency.group}:${dependency.name}:${version}")
                    val entry = result.dependencies.getOrElse(id) {
                        Dependency(dependency.group, dependency.name, version, true)
                    }
                    entry.addConfiguration(config.name)
                    result.dependencies[id] = entry
                }
            }

            // also run for subprojects
            project.subprojects.forEach {
                result.subprojects.add(createFrom(it, logger, result.version))
            }
            return result
        }
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(group=$group, name='$name', version=$version, " +
                "dependencies:\n - ${StringUtils.join(dependencies.values, ",\n - ")}, \n => subprojects=$subprojects)"
    }


}
