plugins {
	id("com.example.build.convention.java")
	id("java-library")
}

dependencies {
	api(libs.qpid.jms.client)
	api(libs.spring.boot.autoconfigure)
	api(libs.spring.boot.jms)

	compileOnly(libs.pooled.jms) {
		exclude(group = "org.apache.geronimo.specs", module = "geronimo-jms_2.0_spec")
	}
}
