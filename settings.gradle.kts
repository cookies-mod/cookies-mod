pluginManagement {
	repositories {
		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
		mavenCentral()
		gradlePluginPortal()
	}
}

buildscript {
	repositories {
		mavenCentral()
	}
	dependencies {
		"classpath"("org.apache.httpcomponents.client5:httpclient5:5.2.1")
	}
}

rootProject.name = "cookies-mod"