import net.ltgt.gradle.errorprone.errorprone

plugins {
	id("java")
	id("net.ltgt.errorprone")
}

group = "com.example"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"
	options.compilerArgs.addAll(listOf(
		"-parameters",
		"-Werror",
		"-Xlint:deprecation",
		"-Xlint:fallthrough",
		"-Xlint:rawtypes",
		"-Xlint:unchecked",
		"-Xlint:varargs"
	))
	options.errorprone {
		option("NullAway:AnnotatedPackages", "com.example")
	}
}

val libs = versionCatalogs.named("libs")

dependencies {
	implementation(platform(libs.findLibrary("spring.boot.dependencies").get()))

	implementation(libs.findLibrary("jspecify").get())
	implementation(libs.findLibrary("log4j-api").get())

	annotationProcessor(platform(libs.findLibrary("spring.boot.dependencies").get()))

	errorprone(libs.findLibrary("errorprone").get())
	errorprone(libs.findLibrary("nullaway").get())
}

testing {
	@Suppress("UnstableApiUsage")
	suites {
		withType<JvmTestSuite> {
			useJUnitJupiter()
			dependencies {
				implementation(libs.findLibrary("assertj-core").get())
				implementation(libs.findLibrary("mockito-core").get())
			}
		}
	}
}
