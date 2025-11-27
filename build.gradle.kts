plugins {
    id("java-library")
    id("io.freefair.lombok") version "8.10"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "me.croabeast"
version = "1.0"

allprojects {
    group = "me.croabeast"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        mavenLocal()

        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.freefair.lombok")

    java {
        withSourcesJar()
        withJavadocJar()
    }

    tasks.withType<Javadoc>().configureEach {
        isFailOnError = false

        (options as StandardJavadocDocletOptions).apply {
            addStringOption("Xdoclint:none", "-quiet")
            encoding = "UTF-8"
            charSet = "UTF-8"
            docEncoding = "UTF-8"

            if (JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_1_9))
                addBooleanOption("html5", true)
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:26.0.2")
        annotationProcessor("org.jetbrains:annotations:26.0.2")

        compileOnly("org.projectlombok:lombok:1.18.38")
        annotationProcessor("org.projectlombok:lombok:1.18.38")
    }
}

tasks.build {
    dependsOn("shadowJar")
}
