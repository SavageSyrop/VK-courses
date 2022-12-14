plugins {
    id("java")
}

group = "ru.vk"
version = "1.0-SNAPSHOT"



dependencies {
    implementation(project(":verticles"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}