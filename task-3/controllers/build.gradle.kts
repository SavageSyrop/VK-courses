group = "ru.vk"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":models"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}