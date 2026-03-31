plugins {
	id("com.example.build.convention.application")
}

dependencies {
	implementation(project(":spring-boot-qpidjms"))
	implementation(libs.spring.boot.starter.jms)
}

testing {
	suites {
		dependencies {
			implementation(libs.spring.boot.starter.jms.test)
		}
	}
}
