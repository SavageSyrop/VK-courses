plugins {
    id("java")
}

group = "ru.vk"
version = "1.0-SNAPSHOT"


subprojects {
    apply(plugin = "java")
    repositories {
        mavenCentral()
    }
    dependencies {
        implementation("org.projectlombok:lombok:1.18.22")
        annotationProcessor("org.projectlombok:lombok:1.18.22")
        implementation("com.intellij:annotations:12.0")
        implementation("com.google.inject:guice:5.1.0")
        implementation("com.google.code.gson:gson:2.9.0")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
        testImplementation("org.mockito:mockito-junit-jupiter:4.8.0")
        testImplementation("org.mockito:mockito-inline:4.8.0")
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}