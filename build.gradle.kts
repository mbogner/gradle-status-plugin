plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.18.0"
    `maven-publish`
}

repositories {
    mavenCentral()
    google()
}

group = "dev.mbo"
version = "1.0.0"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly(gradleApi())
    implementation("org.apache.commons:commons-lang3:3.12.0")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")

    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation(kotlin("gradle-plugin"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<Wrapper> {
    // https://gradle.org/releases/
    gradleVersion = "7.3.2"
    distributionType = Wrapper.DistributionType.ALL
}

gradlePlugin {
    plugins {
        create("gradleStatusPlugin") {
            id = "dev.mbo.gradlestatusplugin"
            implementationClass = "dev.mbo.gradlestatusplugin.GradleStatusPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/mbogner/gradle-status-plugin"
    vcsUrl = "https://github.com/mbogner/gradle-status-plugin.git"
    description = "Plugin to gather metadata about your project and send it to a customizable server."

    (plugins) {
        "gradleStatusPlugin" {
            // id is captured from java-gradle-plugin configuration
            displayName = "Gradle Status Plugin"
            tags = listOf("metadata", "libraries")
            version = project.version.toString()
        }
    }
}