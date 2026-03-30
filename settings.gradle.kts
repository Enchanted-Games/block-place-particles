pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
    }
    plugins {
        kotlin("jvm") version "2.1.21"
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.8"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

stonecutter {
    create(rootProject) {
        fun ver(version: String, vararg loaders: String) = loaders
            .forEach {
                version("$version-${it.replace("_remap", "")}", version).buildscript = "build.$it.gradle.kts"
            }

        // use fabric_remap as the loader for obfuscated minecraft versions (1.21.11 or below)

        ver("26.1", "fabric", "neoforge")

        vcsVersion = "26.1-fabric"
    }
}

rootProject.name = "Particle Interactions"

