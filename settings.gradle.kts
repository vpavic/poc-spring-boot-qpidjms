rootProject.name = "poc-spring-boot-qpidjms"

pluginManagement {
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
	repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
	repositories {
		mavenCentral()
	}
}

includeBuild("build-logic")

include("sample-consumer")
include("sample-producer")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
