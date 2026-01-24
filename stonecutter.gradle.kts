plugins {
    id("dev.kikugie.stonecutter")
    id("co.uzzu.dotenv.gradle") version "4.0.0"
    id("net.fabricmc.fabric-loom-remap") version "1.14-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom") version "1.14-SNAPSHOT" apply false
    id("net.neoforged.moddev") version "2.0.123" apply false
    id("me.modmuss50.mod-publish-plugin") version "0.8.+" apply false
}

stonecutter active "1.21.11-fabric" /* You may have to edit this. Make sure it matches one of the versions present in settings.gradle.kts */

stonecutter parameters {
    constants.match(node.metadata.project.substringAfterLast('-'), "fabric", "neoforge")
    filters.include("**/*.fsh", "**/*.vsh")
}

stonecutter tasks {
    if(hasProperty("publish.modrinth")) {
        order("publishModrinth")
    }
    if(hasProperty("publish.curseforge")) {
        order("publishCurseforge")
    }
}

for (version in stonecutter.versions.map { it.version }.distinct()) tasks.register("publish$version") {
    group = "publishing"
    dependsOn(stonecutter.tasks.named("publishMods") { metadata.version == version })
}