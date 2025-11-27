import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.named

dependencies {
    implementation(project(":core"))
    implementation(project(":complex"))
    implementation("org.apache.commons:commons-numbers-complex:1.2")
}

tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    exclude(
        "META-INF/**", "org/intellij/**", "org/jetbrains/**"
    )
}