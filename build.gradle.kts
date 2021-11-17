plugins {
  kotlin("jvm") version "1.5.31"
  `kotlin-dsl`
  `java-gradle-plugin`
  `maven-publish`
  id("com.gradle.plugin-publish") version "0.17.0"
  id("com.github.ben-manes.versions") version "0.39.0"
  id("io.gitlab.arturbosch.detekt") version Versions.detekt
}

group = "de.nilsdruyen"
version = "0.0.2"

repositories {
  mavenCentral()
}
dependencies {
  implementation(Deps.sshj)
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
configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
  toolVersion = Versions.detekt
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
dependencies {
  "detektPlugins"(Plugins.detektFormatting)
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