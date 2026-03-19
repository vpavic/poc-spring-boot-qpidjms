plugins {
	id("com.example.build.convention.java")
	id("org.springframework.boot")
}

val libs = versionCatalogs.named("libs")

dependencies {
	annotationProcessor(libs.findLibrary("spring.boot.configuration.processor").get())
}
