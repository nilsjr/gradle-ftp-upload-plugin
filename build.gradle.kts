import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
    `maven-publish`
    alias(libs.plugins.gradle.publish)
    alias(libs.plugins.misc.gradleVersions)
    alias(libs.plugins.misc.detekt)
}

group = "de.nilsdruyen"
version = "0.4.2"

dependencies {
    implementation(libs.hierynomusssh)

    add("detektPlugins", libs.detekt.formatting)

    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)

    testImplementation(libs.assertk)
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<GenerateModuleMetadata> {
    enabled = false
}
tasks.withType<Test> {
    useJUnitPlatform()
}

// publish task: publishPlugins -Pgradle.publish.key=key -Pgradle.publish.secret=secret
gradlePlugin {
    plugins.register("ftpUploadPlugin") {
        id = "de.nilsdruyen.gradle-ftp-upload-plugin"
        implementationClass = "de.nilsdruyen.gradle.ftp.UploadPlugin"

        displayName = "FTP Upload Gradle Plugin"
        description = "Gradle plugin for uploading files to ftp server"

        website.set("https://github.com/nilsjr/gradle-ftp-upload-plugin")
        vcsUrl.set("https://github.com/nilsjr/gradle-ftp-upload-plugin")
        tags.set(listOf("upload", "ftp", "file", "files"))
    }
}

configure<DetektExtension> {
    source.from(layout.projectDirectory.files("src/main/kotlin"))
    config.from(layout.projectDirectory.file("detekt-config.yml"))
    parallel = true
    buildUponDefaultConfig = true
    ignoreFailures = false
}

tasks.withType<Detekt>().configureEach {
    reports {
        xml {
            required.set(true)
            outputLocation.set(layout.buildDirectory.file("reports/detekt/detekt.xml"))
        }
        html.required.set(false)
        txt.required.set(false)
    }
}

private val deps: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
tasks.register<Detekt>("ktlintCheck") {
    description = "Run detekt ktlint wrapper"
    parallel = true
    setSource(layout.projectDirectory)
    config.setFrom(layout.projectDirectory.file("detekt-formatting.yml"))
    buildUponDefaultConfig = true
    disableDefaultRuleSets = true
    autoCorrect = false
    reports {
        xml {
            required.set(true)
            outputLocation.set(layout.buildDirectory.file("reports/detekt/detektFormatting.xml"))
        }
        html.required.set(false)
        txt.required.set(false)
    }
    include(listOf("**/*.kt", "**/*.kts"))
    exclude("build/")
    dependencies {
        add("detektPlugins", deps.findLibrary("detekt.formatting").get())
    }
}