package dev.mbo.gradlestatusplugin.model

data class Dependency constructor(
    val group: String?,
    val name: String,
    val version: String?,
    val project: Boolean,
    val configurations: MutableSet<String> = mutableSetOf()
) {

    fun addConfiguration(name: String) {
        configurations.add(name)
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(group=$group, name='$name', version=$version, project=$project, configurations=$configurations)"
    }

}
