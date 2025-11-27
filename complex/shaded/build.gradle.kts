plugins {
    id("java-library")
    id("io.freefair.lombok")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":complex"))
    implementation("org.apache.commons:commons-numbers-complex:1.2")
}