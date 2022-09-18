import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
}

group = "io.github.dejankos"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

gradlePlugin {
    plugins {
        create("rockyBuildCache") {
            id = "io.github.dejankos.gradle-rocky-build-cache"
            implementationClass = "io.github.dejankos.RockyBuildCachePlugin"
            displayName = "Rocky Build Cache"
            description = "Rocky Build Cache"
        }
    }
}