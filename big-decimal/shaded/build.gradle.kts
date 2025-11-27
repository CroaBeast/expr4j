plugins {
    id("java-library")
    id("io.freefair.lombok")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":big-decimal"))
    implementation("ch.obermuhlner:big-math:2.3.2")
}