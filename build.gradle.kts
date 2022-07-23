import io.gitlab.arturbosch.detekt.extensions.DetektExtension

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.kotlin.jvm)
  `kotlin-dsl`
  `java-gradle-plugin`
  `maven-publish`
  alias(libs.plugins.gradle.publish)
  alias(libs.plugins.misc.gradleVersions)
  alias(libs.plugins.misc.detekt)
}

group = "de.nilsdruyen"
version = "0.0.2"

repositories {
  mavenCentral()
}
dependencies {
  implementation(libs.hierynomusssh)
  add("detektPlugins", libs.misc.detektFormatting)
}
gradlePlugin {
  plugins.register("ftpUploadPlugin") {
    id = "de.nilsdruyen.gradle-ftp-upload-plugin"
    implementationClass = "de.nilsdruyen.gradle.ftp.UploadPlugin"
  }
}
java {
  withSourcesJar()
}
tasks.withType<GenerateModuleMetadata> {
  enabled = false
}
configure<DetektExtension> {
  toolVersion = rootProject.libs.versions.detekt.get()
  source = files("src/main/kotlin")
  parallel = true
  config = files("$rootDir/detekt-config.yml")
  buildUponDefaultConfig = true
  ignoreFailures = false
  reports {
    xml {
      enabled = true
      destination = file("$buildDir/reports/detekt/detekt.xml")
    }
    html.enabled = false
    txt.enabled = true
  }
}

// publish task: publishPlugins -Pgradle.publish.key=key -Pgradle.publish.secret=secret
pluginBundle {
  website = "https://github.com/nilsjr/gradle-ftp-upload-plugin"
  vcsUrl = "https://github.com/nilsjr/gradle-ftp-upload-plugin"
  description = "Gradle plugin for uploading files to ftp server"
  tags = listOf("upload", "ftp", "file", "files")
  (plugins) {
    "ftpUploadPlugin" {
      displayName = "FTP Upload Gradle Plugin"
    }
  }
  mavenCoordinates {
    groupId = "de.nilsdruyen"
    artifactId = "gradle-ftp-upload-plugin"
  }
}