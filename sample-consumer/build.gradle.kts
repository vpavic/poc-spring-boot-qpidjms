plugins {
	id("com.example.build.convention.application")
}

dependencies {
	implementation(project(":spring-boot-qpidjms"))
	implementation(libs.spring.boot.starter.jms)

	developmentOnly(libs.spring.boot.docker.compose)
}

testing {
	suites {
		dependencies {
			implementation(libs.spring.boot.starter.jms.test)
		}
	}
}
