import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

//file:noinspection GroovyUnusedAssignment
plugins {
    id("org.ajoberstar.grgit") version "5.2.0" apply false
    id("fabric-loom") version "1.3-SNAPSHOT"
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    //kotlin("jvm") version "1.9.0"
}

version = project.properties["mod_version"]!!
group = project.properties["maven_group"]!!

base {
    archivesName.set(project.properties["archives_base_name"]!!.toString())
}

repositories {
    maven { url = uri("https://maven.ladysnake.org/releases") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.wispforest.io") }
    maven { url = uri("https://repo.morazzer.dev/releases") }
}

val shadowConfiguration: Configuration by configurations.creating {
    this.isCanBeResolved = true
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")
    mappings("net.fabricmc:yarn:${project.properties["yarn_mappings"]}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_version"]}")
    //include(modImplementation("net.fabricmc:fabric-language-kotlin:${project.properties["fabric_kotlin_version"]}")!!)
    include(modImplementation("com.github.0x3C50:Renderer:master-SNAPSHOT")!!)

    implementation("com.google.code.gson:gson:2.10.1")!!
    shadowConfiguration(implementation("org.reflections:reflections:0.10.2") {
        exclude(module = "slf4j-api")
    })
    shadowConfiguration(implementation("org.eclipse.jgit:org.eclipse.jgit:3.5.0.201409260305-r")!!)

    include(modImplementation("dev.morazzer:moulconfig:1.0.0-fix+2")!!)

    annotationProcessor("org.projectlombok:lombok:1.18.26")
    compileOnly("org.projectlombok:lombok:1.18.26")
}

tasks.withType<ProcessResources> {
    filesMatching("fabric.mod.json") {
        expand(Pair("version", project.version))
    }
}

tasks.withType<JavaCompile>().configureEach {
    this.options.release.set(17)
}

tasks.getByName<ShadowJar>("shadowJar") {
    archiveClassifier.set("dev")
    configurations = listOf(shadowConfiguration)
}

tasks.getByName<RemapJarTask>("remapJar") {
    this.dependsOn(tasks.getByName<ShadowJar>("shadowJar"))
    println(tasks.getByName<ShadowJar>("shadowJar").archiveFile)
    inputFile = tasks.getByName<ShadowJar>("shadowJar").archiveFile
    archiveClassifier = "remapped"
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Jar> {
    from("LICENSE") {
        rename {
            "${it}_${project.base.archivesName.getOrElse("cookies")}"
        }
    }
}

publishing {
    publications {
    }

    repositories {
    }
}

loom {
    accessWidenerPath.set(file("src/main/resources/cookiesmod.accesswidener"))
}

apply(from = "gradle/generate-changelog.gradle")
apply(from = "gradle/create-tag-name.gradle")
apply(from = "gradle/hash-libraries.gradle")