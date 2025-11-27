plugins {
    id("java-library")
    id("io.freefair.lombok")
}

dependencies {
    compileOnly(project(":core"))
    compileOnly("ch.obermuhlner:big-math:2.3.2")
}