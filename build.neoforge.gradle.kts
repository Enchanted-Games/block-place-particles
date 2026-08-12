@file:Suppress("UnstableApiUsage")

plugins {
    id("net.neoforged.moddev")
    id("me.modmuss50.mod-publish-plugin")
    id("maven-publish")
}

stonecutter {
    val (version, loader) = current.project.split('-', limit = 2)
    properties.tags(version, loader)
}

val minecraft = stonecutter.current.version
val mcVersion = stonecutter.current.project.substringBeforeLast('-')


val rawModVersion = "${property("mod.version_major")}.${property("mod.version_minor")}.${property("mod.version_patch")}${property("mod.version_suffix")}"
val modVersion = "$rawModVersion+${property("deps.minecraft")}-neoforge"
version = modVersion
base.archivesName = property("mod.id") as String

repositories {
    mavenLocal()
    maven {
        name = "Terraformers (Mod Menu)"
        url = uri("https://maven.terraformersmc.com/releases/")
        content {
            includeGroupAndSubgroups("com.terraformersmc")
        }
    }
    maven {
        name = "Parchment Mappings"
        url = uri("https://maven.parchmentmc.org")
        content {
            includeGroupAndSubgroups("org.parchmentmc")
        }
    }
    maven {
        name = "Gegy (mojbackward)"
        url = uri("https://maven.gegy.dev/releases/")
        content {
            includeGroupAndSubgroups("dev.lambdaurora")
        }
    }
    maven {
        name = "Xander Maven (yacl)"
        url = uri("https://maven.isxander.dev/releases")
    }
}

dependencies {
    // yacl
    if(hasProperty("deps.yacl")) {
        // TODO: temp
        compileOnly("dev.isxander:yet-another-config-lib:${property("deps.yacl")}") {
            exclude("net.fabricmc.fabric-api")
        }
    } else {
        implementation("dev.isxander:yet-another-config-lib:3.8.2+1.21.11-neoforge")
    }
}

stonecutter {
}

neoForge {
    version = property("deps.neoforge") as String
    validateAccessTransformers = true

    if (hasProperty("deps.parchment")) parchment {
        val (mc, ver) = (property("deps.parchment") as String).split(':')
        mappingsVersion = ver
        minecraftVersion = mc
    }

    runs {
        register("client") {
            gameDirectory = rootProject.file("run/")
            client()
        }
        register("server") {
            gameDirectory = rootProject.file("run/")
            server()
        }
    }

    mods {
        register(property("mod.id") as String) {
            sourceSet(sourceSets["main"])
        }
    }
    sourceSets["main"].resources.srcDir("src/main/generated")
}

tasks.named<ProcessResources>("processResources") {
    fun prop(name: String) = project.property(name) as String

    val props = HashMap<String, String>().apply {
        this["version"] = modVersion
        this["version_major"] = prop("mod.version_major")
        this["version_minor"] = prop("mod.version_minor")
        this["version_patch"] = prop("mod.version_patch")
        this["minecraft"] = prop("dep_str.minecraft")
        this["id"] = prop("mod.id")
        this["group"] = prop("mod.group")
        this["description"] = prop("mod.description")
        this["name"] = prop("mod.name")
        this["website_url"] = prop("mod.website_url")
        this["source_url"] = prop("mod.source_url")
        this["issue_tracker"] = prop("mod.issue_tracker")
        this["icon"] = prop("mod.icon")
        this["license"] = prop("mod.license")
        this["neo_icon_property"] = prop("neo_icon_property")
        this["java_ver"] = java.targetCompatibility.majorVersion
    }

    filesMatching(listOf("pack_version.json", "fabric.mod.json", "META-INF/neoforge.mods.toml", "META-INF/mods.toml", "*.mixins.json")) {
        expand(props)
    }
}

tasks {
    processResources {
        exclude("**/fabric.mod.json", "**/*.accesswidener", "**/*.classtweaker")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">1.21.11")) {
        JavaVersion.VERSION_25
    } else if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) {
        JavaVersion.VERSION_21
    } else {
        JavaVersion.VERSION_17
    }
    sourceCompatibility = javaCompat
    targetCompatibility = javaCompat
}

val additionalVersionsStr = findProperty("publish.additionalVersions") as String?
val additionalVersions: List<String> = additionalVersionsStr
    ?.split(",")
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?: emptyList()

publishMods {
    file = tasks.jar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })

    // one of BETA, ALPHA, STABLE
    type = STABLE
    displayName = "[NF] v$rawModVersion for mc ${stonecutter.current.version}"
    version = modVersion
    changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    modLoaders.add("neoforge")

    dryRun = boolProperty("publish.dry_run")

    if (hasProperty("publish.modrinth")) {
        modrinth {
            projectId = property("publish.modrinth") as String
            accessToken = env.MODRINTH_API_KEY.orNull()
            minecraftVersions.add(property("deps.minecraft").toString())
            minecraftVersions.addAll(additionalVersions)
            environment = CLIENT_ONLY
        }
    }

    if (hasProperty("publish.curseforge")) {
        curseforge {
            projectId = property("publish.curseforge") as String
            accessToken = env.CURSEFORGE_API_KEY.orNull()
            minecraftVersions.add(stonecutter.current.version)
            minecraftVersions.addAll(additionalVersions)
            client = true
            server = false
        }
    }
}



fun bool(str: String) : Boolean {
    return str.lowercase().startsWith("t")
}

fun boolProperty(key: String) : Boolean {
    if(!hasProperty(key)){
        return false
    }
    return bool(property(key).toString())
}
