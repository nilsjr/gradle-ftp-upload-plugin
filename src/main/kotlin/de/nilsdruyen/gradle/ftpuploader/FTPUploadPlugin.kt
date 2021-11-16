package de.nilsdruyen.gradle.ftpuploader

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class FTPUploadPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    val extension = target.extensions.create<FTPUploadExtension>("ftpUpload")
    target.tasks.register<FTPUploadTask>("uploadFiles") {
      group = "deployment"
      description = "Upload files to ftp server"

      host.set(extension.host)
      port.set(extension.port)
      username.set(extension.username)
      password.set(extension.password)
      sourceDir.set(extension.sourceDir)
      targetDir.set(extension.targetDir)
    }
  }
}