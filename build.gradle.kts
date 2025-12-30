import java.util.Optional
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Predicate

// Baseline code. Minimal edits necessary.
plugins {
    `maven-publish`
    kotlin("jvm") version "2.1.21"
//    id("fabric-loom") // Leaving this here if you want to swap loom.
    id("dev.architectury.loom")
//    id("dev.kikugie.j52j") // Recommended by kiku if using swaps in json5.
    id("me.modmuss50.mod-publish-plugin")
}

// Leave this alone unless adding more dependencies.
repositories {
    // modmenu
    maven("https://maven.terraformersmc.com/") {
        name = "Terraformers"
    }

    // yacl
    maven("https://maven.isxander.dev/releases") {
        name = "Xander Maven"
    }
    // required by yacl
    maven("https://thedarkcolour.github.io/KotlinForForge/") {
        name = "Kotlin for Forge"
    }

    mavenCentral()
    exclusiveContent {
        forRepository { maven("https://www.cursemaven.com") { name = "CurseForge" } }
        filter { includeGroup("curse.maven") }
    }
    exclusiveContent {
        forRepository { maven("https://api.modrinth.com/maven") { name = "Modrinth" } }
        filter { includeGroup("maven.modrinth") }
    }
    maven("https://maven.neoforged.net/releases/")
    maven("https://maven.architectury.dev/")
    maven("https://modmaven.dev/")
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

fun listProperty(key: String) : ArrayList<String> {
    if(!hasProperty(key)){
        return arrayListOf()
    }
    val str = property(key).toString()
    if(str == "UNSET"){
        return arrayListOf()
    }
    return ArrayList(str.split(" "))
}

fun optionalStrProperty(key: String) : Optional<String> {
    if(!hasProperty(key)){
        return Optional.empty()
    }
    val str = property(key).toString()
    if(str =="UNSET"){
        return Optional.empty()
    }
    return Optional.of(str)
}

class VersionRange(val min: String, val max: String){
    fun asForgelike() : String{
        return "${if(min.isEmpty()) "(" else "["}${min},${max}${if(max.isEmpty()) ")" else "]"}"
    }
    fun asFabric() : String{
        if(min.isEmpty() && max.isEmpty()) {
            return "*"
        }
        var out = ""
        if(min.isNotEmpty()){
            out += ">=$min"
        }
        if(max.isNotEmpty()){
            if(out.isNotEmpty()){
                out += " "
            }
            out += "<=$max"
        }
        return out
    }
}
val wildcardVersion = VersionRange("", "");

/**
 * Creates a VersionRange from a listProperty
 */
fun versionProperty(key: String) : VersionRange {
    if(!hasProperty(key)){
        return VersionRange("","")
    }
    val list = listProperty(key)
    for (i in 0 until list.size) {
        if(list[i] == "UNSET"){
            list[i] = ""
        }
    }
    return if(list.isEmpty()){
        VersionRange("","")
    }
    else if(list.size == 1) {
        VersionRange(list[0],"")
    }
    else{
        VersionRange(list[0], list[1])
    }
}

/**
 * Creates a VersionRange unless the value is UNSET
 */
fun optionalVersionProperty(key: String) : Optional<VersionRange>{
    val str = optionalStrProperty(key)
    if(!hasProperty(key)){
        return Optional.empty()
    }
    if(!str.isPresent){
        return Optional.empty()
    }
    return Optional.of(versionProperty(key))
}

enum class EnvType {
    FABRIC,
    NEOFORGE
}

/**
 * Stores core dependency and environment information.
 */
class Env {
    val archivesBaseName = property("archives_base_name").toString()

    val mcVersionCompileTarget = versionProperty("deps.core.mc.version_range")
    val mcVersionCompatibleRange = versionProperty("deps.core.mc.compatible_range")
    val parchmentMcVersion = versionProperty("deps.parchment.mc_version")
    val parchmentMappingVersion = property("deps.parchment.version")

    val loader = property("loom.platform").toString()
    val loaderPublishingPrefix = if (loader == "fabric") "Fabric" else if (loader == "neoforge") "NF" else "Unknown";
    val isFabric = loader == "fabric"
    val isNeo = loader == "neoforge"
    val isCommon = project.parent!!.name == "common"
    val isApi = project.parent!!.name == "api"
    val type = if(isFabric) EnvType.FABRIC else EnvType.NEOFORGE

    val modmenuEnabled = optionalVersionProperty("deps.api.modmenu").isPresent;

    // if MC requires higher JVMs in future updates change this controller.
    val javaVer = 21

    val fabricLoaderVersion = versionProperty("deps.core.fabric.loader.version_range")
    val neoforgeVersion = versionProperty("deps.core.neoforge.version_range")
    // The modloader system is separate from the API in Neo
    val neoforgeLoaderVersion = versionProperty("deps.core.neoforge.loader.version_range")

    fun atLeast(version: String) = stonecutter.compare(mcVersionCompileTarget.min, version) >= 0
    fun atMost(version: String) = stonecutter.compare(mcVersionCompileTarget.min, version) <= 0
    fun isNot(version: String) = stonecutter.compare(mcVersionCompileTarget.min, version) != 0
    fun isExact(version: String) = stonecutter.compare(mcVersionCompileTarget.min, version) == 0

    private fun extractForgeVer(str: String) : String {
        val split = str.split("-")
        if(split.size == 1){
            return split[0]
        }
        if(split.size > 1){
            return split[1]
        }
        return ""
    }
}
val env = Env()

enum class DepType {
    API,
    // Optional API
    API_OPTIONAL{
        override fun isOptional(): Boolean {
            return true
        }
    },
    // Optional API, Compile only if disabled
    API_OPTIONAL_COMP_ONLY_IF_DISABLED{
        override fun isOptional(): Boolean {
            return true
        }
    },
    // Mod Implementation
    MOD_IMPL,
    // Implementation
    IMPL_YACL_HACK{
        override fun isOptional(): Boolean {
            return true
        }
    },
    // Forge Runtime Library
    FRL{
        override fun includeInDepsList(): Boolean {
            return false
        }
    },
    // Implementation and Included in output jar.
    INCLUDE{
        override fun includeInDepsList(): Boolean {
            return false
        }
    };
    open fun isOptional() : Boolean {
        return false
    }
    open fun includeInDepsList() : Boolean{
        return true
    }
}

class APIModInfo(val modid: String?, val curseSlug: String?, val rinthSlug: String?){
    constructor () : this(null,null,null)
    constructor (modid: String) : this(modid,modid,modid)
    constructor (modid: String, slug: String) : this(modid,slug,slug)
}

/**
 * APIs must have a maven source.
 * If the version range is not present then the API will not be used.
 * If modid is null then the API will not be declared as a dependency in uploads.
 * The enable condition determines whether the API will be used for this version.
 */
class APISource(val type: DepType, val modInfo: APIModInfo, val mavenLocation: String, val versionRange: Optional<VersionRange>, val dependencyVersion: Optional<VersionRange>, val enableCondition: Predicate<APISource>) {
    val enabled = this.enableCondition.test(this)

    constructor(type: DepType, modInfo: APIModInfo, mavenLocation: String, versionRange: Optional<VersionRange>, enableCondition: Predicate<APISource>) : this(type, modInfo, mavenLocation, versionRange, Optional.empty(), enableCondition)
}

/**
 * APIs with hardcoded support for convenience. These are optional.
 */
// add any hardcoded apis here. Hardcoded APIs should be used in most if not all your versions.
val apis = arrayListOf(
    APISource(
        DepType.API,
        APIModInfo(if(env.atMost("1.16.5")) "fabric" else "fabric-api","fabric-api"),
        "net.fabricmc.fabric-api:fabric-api",
        optionalVersionProperty("deps.api.fabric"),
        Optional.of(wildcardVersion),
        { src ->
            src.versionRange.isPresent && env.isFabric
        }
    ),

    APISource(
        DepType.API_OPTIONAL_COMP_ONLY_IF_DISABLED,
        APIModInfo("modmenu"),
        "com.terraformersmc:modmenu",
        optionalVersionProperty("deps.api.modmenu"),
        { src ->
            if(boolProperty("deps.api.modmenu.componly")) {
                false
            } else {
                src.versionRange.isPresent && env.isFabric
            }
        }
    ),

    APISource(
        DepType.IMPL_YACL_HACK,
        APIModInfo("yet_another_config_lib_v3", "yacl"),
        "dev.isxander:yet-another-config-lib",
        optionalVersionProperty("deps.api.yacl"),
        { src ->
            src.versionRange.isPresent
        }
    )
)

// Stores information about the mod itself.
class ModProperties {
    val id = property("mod.id").toString()
    val displayName = property("mod.display_name").toString()
    val version = property("version").toString()
    val description = optionalStrProperty("mod.description").orElse("")
    val authors = property("mod.authors").toString()
    val icon = property("mod.icon").toString()
    val issueTracker = optionalStrProperty("mod.issue_tracker").orElse("")
    val license = optionalStrProperty("mod.license").orElse("")
    val sourceUrl = optionalStrProperty("mod.source_url").orElse("")
    val generalWebsite = optionalStrProperty("mod.general_website").orElse(sourceUrl)
}

/**
 * Stores information specifically for fabric.
 * Fabric requires that the mod's client and common main() entry points be included in the fabric.mod.json file.
 */
class ModFabric {
    val commonEntry = "${group}.${env.archivesBaseName}.fabric.${property("mod.fabric.entry.common").toString()}"
    val clientEntry = "${group}.${env.archivesBaseName}.fabric.${property("mod.fabric.entry.client").toString()}"
}

/**
 * Provides access to the mixins for specific environments.
 * All environments are provided the vanilla mixin if it is enabled.
 */
class ModMixins {
    val enableFabricMixin = boolProperty("mixins.fabric.enable")
    val enableNeoforgeMixin = boolProperty("mixins.neoforge.enable")

    val clientMixin = "mixins.client.${mod.id}.json"
    val modCompatMixin = "mixins.mod_compat.${mod.id}.json"
    val fabricMixin = "mixins.fabric.${mod.id}.json"
    val neoForgeMixin = "mixins.neoforge.${mod.id}.json"
    val extraMixins = listProperty("mixins.extras")

    /**
     * Modify this method if you need better control over the mixin list.
     */
    fun getMixins(env: EnvType) : List<String> {
        val out = arrayListOf<String>()
        out.add(clientMixin)
        out.add(modCompatMixin)
        when (env) {
            EnvType.FABRIC -> if(enableFabricMixin) out.add(fabricMixin)
            EnvType.NEOFORGE -> if(enableNeoforgeMixin) out.add(neoForgeMixin)
        }
        return out
    }
}

/**
 * Controls publishing. For publishing to work dryRunMode must be false.
 * Modrinth and Curseforge project tokens are publicly accessible, so it is safe to include them in files.
 * Do not include your API keys in your project!
 *
 * The Modrinth API token should be stored in the MODRINTH_TOKEN environment variable.
 * The curseforge API token should be stored in the CURSEFORGE_TOKEN environment variable.
 */
class ModPublish {
    val mcTargets = arrayListOf<String>()
    val modrinthProjectToken = property("publish.token.modrinth").toString()
    val curseforgeProjectToken = property("publish.token.curseforge").toString()
    val mavenURL = optionalStrProperty("publish.maven.url")
    val dryRunMode = boolProperty("publish.dry_run")

    init {
        val tempmcTargets = listProperty("publish_acceptable_mc_versions")
        if(tempmcTargets.isEmpty()){
            mcTargets.add(env.mcVersionCompatibleRange.min)
        }
        else{
            mcTargets.addAll(tempmcTargets)
        }
    }
}
val modPublish = ModPublish()

/**
 * These dependencies will be added to the fabric.mods.json, and META-INF/neoforge.mods.toml file.
 */
class ModDependencies {
    val loadBefore = listProperty("deps.before")
    fun forEachAfter(cons: BiConsumer<String,VersionRange>){
        forEachRequired(cons, false)
        forEachOptional(cons, false)
    }

    fun forEachBefore(cons: Consumer<String>){
        loadBefore.forEach(cons)
    }

    fun forEachOptional(cons: BiConsumer<String,VersionRange>, forModMetadata: Boolean) {
        iterateDependencies(cons, forModMetadata)
    }

    fun forEachRequired(cons: BiConsumer<String,VersionRange>, forModMetadata: Boolean){
        cons.accept("minecraft",env.mcVersionCompatibleRange)
        if (env.isNeo){
            cons.accept("neoforge", env.neoforgeVersion)
        }
        if(env.isFabric) {
            cons.accept("fabric", env.fabricLoaderVersion)
        }
        iterateDependencies(cons, forModMetadata)
    }

    fun iterateDependencies(cons: BiConsumer<String,VersionRange>, forModMetadata: Boolean) {
        apis.forEach{src->
            if(src.enabled && !src.type.isOptional() && src.type.includeInDepsList()) {
                val range: Optional<VersionRange>;
                if(forModMetadata) {
                    if(src.dependencyVersion.isPresent) range = src.dependencyVersion else range = src.versionRange;
                } else {
                    range = src.versionRange
                }
                range.ifPresent { ver ->
                    src.modInfo.modid?.let {
                        cons.accept(it, ver)
                    }
                }
            }
        }
    }
}
val dependencies = ModDependencies()

/**
 * These values will change between versions and mod loaders. Handles generation of specific entries in mods.toml and neoforge.mods.toml
 */
class SpecialMultiversionedConstants {
    private val mandatoryIndicator = if(env.isNeo) "required" else "mandatory"
    val mixinField = if(env.atLeast("1.20.4") && env.isNeo) neoForgeMixinField() else if(env.isFabric) fabricMixinField() else ""

    val forgelikeLoaderVer = env.neoforgeLoaderVersion.asForgelike()
    val forgelikeAPIVer = env.neoforgeVersion.asForgelike()
    val dependenciesField = if(env.isFabric) fabricDependencyList() else forgelikeDependencyField()
    val excludes = excludes0()
    private fun excludes0() : List<String> {
        val out = arrayListOf<String>()
        if(!env.isFabric){
            out.add("fabric.mod.json")
        }
        if(!env.isNeo){
            out.add("META-INF/neoforge.mods.toml")
        }
        return out
    }
    private fun neoForgeMixinField () : String {
        var out = ""
        for (mixin in modMixins.getMixins(EnvType.NEOFORGE)) {
            out += "[[mixins]]\nconfig=\"${mixin}\"\n"
        }
        return out
    }
    private fun fabricMixinField () : String {
        val list = modMixins.getMixins(EnvType.FABRIC)
        if(list.isEmpty()){
            return ""
        }
        else{
            var out = "  \"mixins\" : [\n"
            for ((index, mixin) in list.withIndex()) {
                out += "    \"${mixin}\""
                if(index < list.size-1){
                    out+=","
                }
                out+="\n"
            }
            return "$out  ],"
        }
    }
    private fun fabricDependencyList() : String{
        var out = "  \"depends\":{"
        var useComma = false
        dependencies.forEachRequired({modid,ver->
            if(useComma){
                out+=","
            }
            out+="\n"
            out+="    \"${modid}\": \"${ver.asFabric()}\""
            useComma = true
        }, true)
        return "$out\n  }"

    }
    private fun forgelikeDependencyField() : String {
        var out = ""
        dependencies.forEachBefore{modid ->
            out += forgedep(modid,VersionRange("",""),"BEFORE",false)
        }
        dependencies.forEachOptional({modid,ver->
            out += forgedep(modid,ver,"AFTER",false)
        }, true)
        dependencies.forEachRequired({modid,ver->
            out += forgedep(modid,ver,"AFTER",true)
        }, true)
        return out
    }
    private fun forgedep(modid: String, versionRange: VersionRange, order: String, mandatory: Boolean) : String {
        return "[[dependencies.${mod.id}]]\n" +
                "modId=\"${modid}\"\n" +
                "${mandatoryIndicator}=${mandatory}\n" +
                "versionRange=\"${versionRange.asForgelike()}\"\n" +
                "ordering=\"${order}\"\n" +
                "side=\"BOTH\"\n"
    }
}
val mod = ModProperties()
val modFabric = ModFabric()
val modMixins = ModMixins()
val dynamics = SpecialMultiversionedConstants()

// Upload version format
version = "v${mod.version}-${env.loader}-mc${env.mcVersionCompileTarget.min}"
group = property("group").toString()

// Adds both optional and required dependencies to stonecutter version checking.
dependencies.forEachAfter{mid, ver ->
    stonecutter.dependency(mid,ver.min)
}
apis.forEach{ src ->
    src.modInfo.modid?.let {
        stonecutter.const(it,src.enabled)
        src.versionRange.ifPresent{ ver ->
            stonecutter.dependency(it,ver.min)
        }
    }
}

// Stonecutter variables here.
stonecutter.const("modmenu",env.modmenuEnabled)
stonecutter.const("fabric",env.isFabric)
stonecutter.const("neoforge",env.isNeo)

loom {
    silentMojangMappingsLicense()

    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        runDir = "../../run"
    }

    val widenerFile = file("src/main/resources/${mod.id}.accesswidener");
    if(widenerFile.exists()) {
        accessWidenerPath = widenerFile;
    }
}
base { archivesName.set(env.archivesBaseName) }

dependencies {
    minecraft("com.mojang:minecraft:${env.mcVersionCompileTarget.min}")
    mappings(
        loom.layered {
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-${env.parchmentMcVersion.min}:${env.parchmentMappingVersion}@zip")
        }
    )

    if(env.isFabric) {
        modImplementation("net.fabricmc:fabric-loader:${env.fabricLoaderVersion.min}")
    }
    if(env.isNeo){
        "neoForge"("net.neoforged:neoforge:${env.neoforgeVersion.min}")
    }

    apis.forEach { src->
        if(src.type == DepType.API_OPTIONAL_COMP_ONLY_IF_DISABLED) {
            src.versionRange.ifPresent { ver ->
                if(src.enabled) {
                    modApi("${src.mavenLocation}:${ver.min}")
                } else {
                    modCompileOnly("${src.mavenLocation}:${ver.min}")
                }
            }
        }
        else if(src.enabled) {
            src.versionRange.ifPresent { ver ->
                if(src.type == DepType.API || src.type == DepType.API_OPTIONAL) {
                    modApi("${src.mavenLocation}:${ver.min}")
                }
                if(src.type == DepType.MOD_IMPL) {
                    modImplementation("${src.mavenLocation}:${ver.min}")
                }
                if(src.type == DepType.IMPL_YACL_HACK) {
                    if(env.isNeo) {
                        implementation("${src.mavenLocation}:${ver.min}") {
                            exclude(group = "net.neoforged.fancymodloader", module = "loader")
                        }
                    } else {
                        modImplementation("${src.mavenLocation}:${ver.min}")
                    }
                }
                if(src.type == DepType.INCLUDE) {
                    modImplementation("${src.mavenLocation}:${ver.min}")
                    include("${src.mavenLocation}:${ver.min}")
                }
            }
        }
    }

    vineflowerDecompilerClasspath("org.vineflower:vineflower:1.10.1")
}

java {
    withSourcesJar()
    val java = JavaVersion.VERSION_21
    targetCompatibility = java
    sourceCompatibility = java
}

/**
 * Replaces the normal copy task and post-processes the files.
 * Effectively renames datapack directories due to depluralization past 1.20.4.
 */

abstract class ProcessResourcesExtension : ProcessResources() {
    @get:Input
    val autoPluralize = arrayListOf(
        "/data/minecraft/tags/block",
        "/data/minecraft/tags/item",
        "/data/eg_particle_interactions/loot_table",
        "/data/eg_particle_interactions/recipe",
        "/data/eg_particle_interactions/tags/item",
    )
    override fun copy() {
        super.copy()
        val root = destinationDir.absolutePath
        autoPluralize.forEach { path ->
            val file = File(root.plus(path))
            if(file.exists()){
                file.copyRecursively(File(file.absolutePath.plus("s")),true)
                file.deleteRecursively()
            }
        }
    }
}

if(env.atMost("1.20.6")){
    tasks.replace("processResources",ProcessResourcesExtension::class)
}

tasks.processResources {
    val map = mapOf<String,String>(
        "modid" to mod.id,
        "id" to mod.id,
        "name" to mod.displayName,
        "display_name" to mod.displayName,
        "version" to mod.version,
        "description" to mod.description,
        "authors" to mod.authors,
        "github_url" to mod.sourceUrl,
        "source_url" to mod.sourceUrl,
        "website" to mod.generalWebsite,
        "icon" to mod.icon,
        "fabric_common_entry" to modFabric.commonEntry,
        "fabric_client_entry" to modFabric.clientEntry,
        "mc_min" to env.mcVersionCompileTarget.min,
        "mc_max" to env.mcVersionCompileTarget.max,
        "issue_tracker" to mod.issueTracker,
        "java_ver" to env.javaVer.toString(),
        "forgelike_loader_ver" to dynamics.forgelikeLoaderVer,
        "forgelike_api_ver" to dynamics.forgelikeAPIVer,
        "loader_id" to env.loader,
        "license" to mod.license,
        "mixin_field" to dynamics.mixinField,
        "dependencies_field" to dynamics.dependenciesField
    )
    map.forEach{ (key, value) ->
        inputs.property(key,value)
    }
    dynamics.excludes.forEach{file->
        exclude(file)
    }
    filesMatching("pack.mcmeta") { expand(map) }
    filesMatching("fabric.mod.json") { expand(map) }
    filesMatching("META-INF/neoforge.mods.toml") { expand(map) }
    modMixins.getMixins(env.type).forEach { str->
        filesMatching(str) { expand(map) }
    }
}

//TODO: Enable auto-publishing.
publishMods {
    file = tasks.remapJar.get().archiveFile
    additionalFiles.from(tasks.remapSourcesJar.get().archiveFile)
    displayName = "[${env.loaderPublishingPrefix}] v${mod.version} for mc ${env.mcVersionCompileTarget.min}"
    version = mod.version
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE
    modLoaders.add(env.loader)

    dryRun = modPublish.dryRunMode

    modrinth {
        projectId = modPublish.modrinthProjectToken
        // Get one here: https://modrinth.com/settings/pats, enable read, write, and create Versions ONLY!
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.addAll(modPublish.mcTargets)
        apis.forEach{ src ->
            if(src.enabled) src.versionRange.ifPresent{ ver ->
                if(src.type.isOptional()){
                    src.modInfo.rinthSlug?.let {
                        optional {
                            slug = it
                            version = ver.min

                        }
                    }
                }
                else{
                    src.modInfo.rinthSlug?.let {
                        requires {
                            slug = it
                            version = ver.min
                        }
                    }
                }
            }
        }
    }

    curseforge {
        projectId = modPublish.curseforgeProjectToken
        // Get one here: https://legacy.curseforge.com/account/api-tokens
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.addAll(modPublish.mcTargets)
        apis.forEach{ src ->
            if(src.enabled) src.versionRange.ifPresent{ ver ->
                if(src.type.isOptional()){
                    src.modInfo.curseSlug?.let {
                        optional {
                            slug = it
                            version = ver.min

                        }
                    }
                }
                else{
                    src.modInfo.curseSlug?.let {
                        requires {
                            slug = it
                            version = ver.min
                        }
                    }
                }
            }
        }
    }
}
// TODO Disable if not uploading to a maven
/*publishing {
    repositories {
        // TODO this is an example of how I recommend you do this.
        if(modPublish.mavenURL.isPresent) {
            maven {
                url = uri(modPublish.mavenURL.get())
                credentials {
                    username = System.getenv("MVN_NAME")
                    password = System.getenv("MVN_KEY")
                }
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava"){
            groupId = project.group.toString()
            artifactId = env.archivesBaseName
            version = project.version.toString()
            from(components["java"])
        }
    }
}*/