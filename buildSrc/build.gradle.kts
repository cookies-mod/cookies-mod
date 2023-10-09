plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}
repositories {
    mavenCentral()
    maven { url = uri("https://maven.ladysnake.org/releases") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://repo.morazzer.dev/releases") }
}

allprojects {
    dependencies {
        implementation("com.github.javaparser:javaparser-symbol-solver-core:3.25.5")
        implementation("com.google.code.gson:gson:2.10.1")
        implementation("org.apache.commons:commons-lang3:3.13.0")
    }
}

gradlePlugin {
    plugins {
        create("generateAreas") {
            id = "cookies.generateAreas"
            implementationClass = "dev.morazzer.cookies.plugins.areaenum.AreaEnumPlugin"
        }
        create("generateModuleLoader") {
            id = "cookies.moduleLoader"
            implementationClass = "dev.morazzer.cookies.plugins.modules.GenerateModuleLoader"
        }
    }
}

