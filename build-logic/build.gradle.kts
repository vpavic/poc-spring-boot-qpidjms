plugins {
	`kotlin-dsl`
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"
	options.compilerArgs = listOf(
		"-parameters",
		"-Werror",
		"-Xlint:deprecation",
		"-Xlint:fallthrough",
		"-Xlint:rawtypes",
		"-Xlint:unchecked",
		"-Xlint:varargs"
	)
}

dependencies {
	implementation(platform("org.jetbrains.kotlin:kotlin-bom:${embeddedKotlinVersion}"))
	implementation("net.ltgt.errorprone:net.ltgt.errorprone.gradle.plugin:5.1.0")
	implementation("org.springframework.boot:spring-boot-gradle-plugin:${libs.versions.spring.boot.get()}")
}
