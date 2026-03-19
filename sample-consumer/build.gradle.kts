plugins {
	id("com.example.build.convention.application")
}

dependencies {
	implementation(libs.qpid.jms.client)
	implementation(libs.spring.boot.starter.jms)
}

testing {
	suites {
		dependencies {
			implementation(libs.spring.boot.starter.jms.test)
		}
	}
}
