plugins {
    id("java-library")
    id("io.freefair.lombok")
}

dependencies {
    compileOnly(project(":core"))
    compileOnly("org.apache.commons:commons-numbers-complex:1.2")
}