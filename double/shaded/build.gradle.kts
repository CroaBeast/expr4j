import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.named

dependencies {
    implementation(project(":core"))
    implementation(project(":double"))
}

tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    exclude("META-INF/**", "org/intellij/**", "org/jetbrains/**")
}