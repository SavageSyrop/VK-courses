plugins {
    id("java")
}

group = "ru.vk"
version = "1.0-SNAPSHOT"


allprojects {
    apply(plugin = "java")
    repositories {
        mavenCentral()
    }
    dependencies {
        implementation("com.intellij:annotations:12.0")
        implementation("com.google.inject:guice:5.1.0")
        implementation("org.projectlombok:lombok:1.18.22")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    }
}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}