import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application

    kotlin("jvm")
    kotlin("plugin.serialization")

    id("com.github.jakemarsden.git-hooks")
    id("com.github.johnrengelman.shadow")
    id("io.gitlab.arturbosch.detekt")
}

group = "template"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()

    maven {
        name = "Sonatype Snapshots (Legacy)"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }

    maven {
        name = "Sonatype Snapshots"
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    detektPlugins(libs.detekt)

    implementation(libs.kord.extensions)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kx.ser)

    // Logging dependencies
    implementation(libs.groovy)
    implementation(libs.jansi)
    implementation(libs.logback)
    implementation(libs.logging)
}

application {
    // This is deprecated, but the Shadow plugin requires it
    mainClassName = "template.AppKt"
}

gitHooks {
    setHooks(
        mapOf("pre-commit" to "detekt")
    )
}

tasks.withType<KotlinCompile> {
    // Current LTS version of Java
    kotlinOptions.jvmTarget = "11"

    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "template.AppKt"
        )
    }
}

java {
    // Current LTS version of Java
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

detekt {
    buildUponDefaultConfig = true
    config = rootProject.files("detekt.yml")
}
