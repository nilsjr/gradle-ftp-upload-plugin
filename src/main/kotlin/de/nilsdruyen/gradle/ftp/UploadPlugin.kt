package de.nilsdruyen.gradle.ftp

import org.gradle.api.Plugin
import org.gradle.api.Project
class UploadPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    val extension = target.extensions.create("ftpUploadExtension", UploadExtension::class.java)
    target.tasks.register("uploadFilesToFtp", UploadTask::class.java) { task ->
      with(task) {
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
}