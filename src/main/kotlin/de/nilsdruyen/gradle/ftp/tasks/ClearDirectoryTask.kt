package de.nilsdruyen.gradle.ftp.tasks

import de.nilsdruyen.gradle.ftp.SFTPConnection
import de.nilsdruyen.gradle.ftp.UploadExtension
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class ClearDirectoryTask : DefaultTask() {

    @get:Input
    abstract val taskExtension: Property<UploadExtension>

    @TaskAction
    fun run() {
        println("\uD83D\uDE80 Welcome bro, you're using upload plugin v0.5.0")
        val ext = taskExtension.get()
        val root = File(ext.sourceDir)
        if (root.exists() && root.isDirectory) {
            val connection = SFTPConnection(ext.host, ext.port, ext.username, ext.password)
            try {
                connection.with {
                    cleanTargetDirectory(this, ext.targetDir)
                }
            } finally {
                connection.disconnect()
            }
        } else {
            println("\uD83D\uDD25 Source directory not exists!")
            println("sourceDir was (${ext.sourceDir})")
        }
    }
}