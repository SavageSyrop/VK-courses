plugins {
    id("java")
}

group = "ru.vk.shevtsov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java")
    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.22")
        annotationProcessor("org.projectlombok:lombok:1.18.22")
        implementation("com.intellij:annotations:12.0")
    }
}


