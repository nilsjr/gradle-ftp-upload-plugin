import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.gitlab.arturbosch.detekt.Detekt

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
version = "0.0.3"

repositories {
  mavenCentral()
}
dependencies {
  implementation(libs.hierynomusssh)
  add("detektPlugins", libs.misc.detektFormatting)
}
java {
  withSourcesJar()
}
tasks.withType<GenerateModuleMetadata> {
  enabled = false
}

gradlePlugin {
  plugins.register("ftpUploadPlugin") {
    id = "de.nilsdruyen.gradle-ftp-upload-plugin"
    implementationClass = "de.nilsdruyen.gradle.ftp.UploadPlugin"

    displayName = "FTP Upload Gradle Plugin"
    description = "Gradle plugin for uploading files to ftp server"
  }
}

// publish task: publishPlugins -Pgradle.publish.key=key -Pgradle.publish.secret=secret
pluginBundle {
  website = "https://github.com/nilsjr/gradle-ftp-upload-plugin"
  vcsUrl = "https://github.com/nilsjr/gradle-ftp-upload-plugin"
  tags = listOf("upload", "ftp", "file", "files")
}

configure<DetektExtension> {
  toolVersion = rootProject.libs.versions.detekt.get()
  source = files("src/main/kotlin")
  parallel = true
  config = files("$rootDir/detekt-config.yml")
  buildUponDefaultConfig = true
  ignoreFailures = false
}

tasks.withType<Detekt> {
  reports {
    xml {
      required.set(true)
      outputLocation.set(file("$buildDir/reports/detekt/detekt.xml"))
    }
    html.required.set(false)
    txt.required.set(true)
  }
}