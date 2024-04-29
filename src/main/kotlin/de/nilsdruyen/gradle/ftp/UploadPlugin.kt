package de.nilsdruyen.gradle.ftp

import de.nilsdruyen.gradle.ftp.tasks.ClearDirectoryTask
import de.nilsdruyen.gradle.ftp.tasks.UploadTask
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("UNUSED")
class UploadPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target.extensions.create(Constants.UPLOAD_EXTENSION, UploadExtension::class.java)

        with(target) {
            tasks.register(Constants.UPLOAD_TASK, UploadTask::class.java) { task ->
                with(task) {
                    group = "deployment"
                    description = "Upload files to ftp server"

                    taskExtension.set(extension)
                }
            }
            tasks.register(Constants.CLEAR_TASK, ClearDirectoryTask::class.java) { task ->
                with(task) {
                    group = "deployment"
                    description = "Clear target directory on ftp server"

                    taskExtension.set(extension)
                }
            }
        }
    }
}