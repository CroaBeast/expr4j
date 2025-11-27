plugins {
    id("java-library")
    id("io.freefair.lombok")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":double"))
}