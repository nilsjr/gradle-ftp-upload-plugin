plugins {
  kotlin("jvm") version "1.6.0"
  `kotlin-dsl`
  `java-gradle-plugin`
  `maven-publish`
  id("com.gradle.plugin-publish") version "0.17.0"
  id("com.github.ben-manes.versions") version "0.39.0"
  id("io.gitlab.arturbosch.detekt") version Versions.detekt
}

group = "de.nilsdruyen"
version = "0.0.1"

repositories {
  mavenCentral()
}
dependencies {
  implementation(Deps.sshj)
}
gradlePlugin {
  plugins.register("ftp-uploader") {
    id = "de.nilsdruyen.gradle-ftp-uploader"
    implementationClass = "de.nilsdruyen.gradle.ftpuploader.FTPUploadPlugin"
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
//pluginBundle {
//  website = "https://github.com/nanogiants/android-versioning/"
//  vcsUrl = "https://github.com/nanogiants/android-versioning"
//  description = "Gradle plugin to automatically generate Android versionName and versionCode using Git."
//  tags = listOf("versioning", "android", "artifact", "version")
//  (plugins) {
//    "versioningPlugin" {
//      displayName = "Android Versioning Gradle Plugin"
//    }
//  }
//  mavenCoordinates {
//    groupId = "de.nanogiants"
//    artifactId = "android-versioning"
//  }
//}