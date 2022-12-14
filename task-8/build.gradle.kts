plugins {
    id("java")
    id("nu.studer.jooq") version "8.0" apply false
}

group = "ru.vk"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":dao"))
    implementation(project(":entities"))
}

allprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin("nu.studer.jooq")
        plugin("java")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }

    dependencies {
        implementation("com.zaxxer:HikariCP:5.0.1")

        implementation("org.flywaydb:flyway-core:9.6.0")
        implementation("org.postgresql:postgresql:42.5.0")
        implementation("org.jetbrains:annotations:23.0.0")

        implementation("org.jooq:jooq:3.17.4")
        implementation("org.jooq:jooq-codegen:3.17.4")
        implementation("org.jooq:jooq-meta:3.17.4")

        implementation("org.projectlombok:lombok:1.18.22")

        implementation("org.eclipse.jetty:jetty-server:9.4.33.v20201020")
        implementation("org.eclipse.jetty:jetty-servlet:9.4.33.v20201020")

        implementation("org.jboss.resteasy:resteasy-guice:4.7.7.Final")
        implementation("org.jboss.resteasy:resteasy-jackson-provider:3.6.3.Final")
        implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
        implementation("javax.ws.rs:javax.ws.rs-api:2.1.1")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    }
}