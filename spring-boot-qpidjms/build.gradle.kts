plugins {
	id("com.example.build.convention.java")
	id("java-library")
}

dependencies {
	api(libs.qpid.jms.client)
	api(libs.spring.boot.autoconfigure)
	api(libs.spring.boot.jms)
}
