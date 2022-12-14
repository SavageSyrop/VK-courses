plugins {
    id("java")
}

group = "ru.vk"
version = "1.0-SNAPSHOT"


allprojects {

    apply {
        plugin("java")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.22")
        implementation("io.vertx:vertx-hazelcast:4.3.5")
        implementation("io.vertx:vertx-core:4.3.5")
        implementation("io.vertx:vertx-core:4.3.5")
        implementation("io.vertx:vertx-web:4.3.5")
        implementation("io.vertx:vertx-web-client:4.3.5")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}

