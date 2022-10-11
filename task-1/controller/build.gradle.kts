plugins {
    id("java")
}

group = "ru.vk.shevtsov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.9.0")
    implementation(project(":models"))
}
