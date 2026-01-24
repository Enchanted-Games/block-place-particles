@file:Suppress("UnstableApiUsage")

plugins {
    id("net.fabricmc.fabric-loom-remap")
    id("me.modmuss50.mod-publish-plugin")
    id("maven-publish")
}

val minecraft = stonecutter.current.version
val mcVersion = stonecutter.current.project.substringBeforeLast('-')
val accessWidenerFilepath = "src/main/resources/${property("mod.id")}.classtweaker"

version = "${property("mod.version")}+${property("deps.minecraft")}-fabric"
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
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")
    mappings(loom.layered {
        officialMojangMappings()
        if (hasProperty("deps.parchment"))
            parchment("org.parchmentmc.data:parchment-${property("deps.parchment")}@zip")
        if (hasProperty("deps.mojbackward"))
            mappings("dev.lambdaurora:yalmm-mojbackward:${property("deps.minecraft")}+build.${property("deps.mojbackward")}")
    })
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric-loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric-api")}")

    // Mod Menu
    if (hasProperty("deps.modmenu")) {
        modApi("com.terraformersmc:modmenu:${property("deps.modmenu")}")
    } else {
        modCompileOnly("com.terraformersmc:modmenu:15.0.0-beta.3")
    }

    // yacl
    modImplementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}")
}

stonecutter {
}

loom {
    if(project.file(accessWidenerFilepath).exists()) {
        accessWidenerPath = project.file(accessWidenerFilepath)
    }

    runConfigs.all {
        runDir = "../../run"
        isIdeConfigGenerated = true
    }
}

configurations.all {
    resolutionStrategy {
        force("net.fabricmc:fabric-loader:${property("deps.fabric-loader")}")
    }
}

tasks.named<ProcessResources>("processResources") {
    fun prop(name: String) = project.property(name) as String

    val props = HashMap<String, String>().apply {
        this["version"] = prop("mod.version") + "+" + prop("deps.minecraft")
        this["minecraft"] = prop("dep_str.minecraft")
        this["id"] = prop("mod.id")
        this["group"] = prop("mod.group")
        this["description"] = prop("mod.description")
        this["name"] = prop("mod.name")
        this["source_url"] = prop("mod.source_url")
        this["issue_tracker"] = prop("mod.issue_tracker")
        this["icon"] = prop("mod.icon")
        this["license"] = prop("mod.license")
        this["fabric_loader_dep_str"] = prop("dep_str.fabric-loader")
        this["fabric_api_dep_str"] = prop("dep_str.fabric-api")
        this["java_ver"] = java.targetCompatibility.majorVersion
    }

    if(project.file(accessWidenerFilepath).exists()) {
        props["accesswidener_field"] = "\"accessWidener\": \"${prop("mod.id")}.classtweaker\","
    } else {
        props["accesswidener_field"] = ""
    }

    filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "META-INF/mods.toml", "*.mixins.json")) {
        expand(props)
    }
}

tasks.named("processResources") {
    dependsOn(":${stonecutter.current.project}:stonecutterGenerate")
}

tasks {
    processResources {
        exclude("**/neoforge.mods.toml", "**/mods.toml", "**/accesstransformer.cfg", "neoforge.mods.toml", "mods.toml", "accesstransformer.cfg")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(remapJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) {
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
    file = tasks.remapJar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.remapSourcesJar.map { it.archiveFile.get() })

    // one of BETA, ALPHA, STABLE
    type = STABLE
    displayName = "[Fabric] v${property("mod.version")} for mc ${stonecutter.current.version}"
    version = "${property("mod.version")}+${property("deps.minecraft")}-fabric"
    changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    modLoaders.add("fabric")

    dryRun = boolProperty("publish.dry_run")

    if(hasProperty("publish.modrinth")) {
        modrinth {
            projectId = property("publish.modrinth") as String
            accessToken = env.MODRINTH_API_KEY.orNull()
            minecraftVersions.add(property("deps.minecraft").toString())
            minecraftVersions.addAll(additionalVersions)
            requires("fabric-api")
            optional("modmenu")
        }
    }

    if(hasProperty("publish.curseforge")) {
        curseforge {
            projectId = property("publish.curseforge") as String
            accessToken = env.CURSEFORGE_API_KEY.orNull()
            minecraftVersions.add(stonecutter.current.version)
            minecraftVersions.addAll(additionalVersions)
            requires("fabric-api")
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
