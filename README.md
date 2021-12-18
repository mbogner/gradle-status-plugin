Gradle Status Plugin
====================

This plugin is intended to gather metadata about gradle projects into a central place. By integrating this and using
a central uploadUrl (see below) you're able to build a company-wide knowledge base about used libraries in your
projects including information about module structure.

# Usage

## build.gradle.kts

```kotlin
plugins {
    id("dev.mbo.gradlestatusplugin") version "1.0.0"
}

tasks.withType<dev.mbo.gradlestatusplugin.GatherTask>() {
    uploadUrl = "http://localhost:8080/gradle-status-web/data"
}
```

The uploadUrl needs to be configured. It posts the metadata to that given url as application/json.

## settings.gradle.kts

This is only useful for local development if you're not able to get the plugin from the server. With the following in
place you can install the plugin to mavenLocal with `./gradlew publishToMavenLocal` and then use it from there instead
of downloading it.

```kotlin
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
```