package de.nilsdruyen.gradle.ftp.tasks

import de.nilsdruyen.gradle.ftp.SFTPConnection
import de.nilsdruyen.gradle.ftp.UploadExtension
import net.schmizz.sshj.sftp.SFTPClient
import net.schmizz.sshj.xfer.FileSystemFile
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class UploadTask : DefaultTask() {

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
                    root.listFiles()?.toList()?.let {
                        upload(it, ext.targetDir, ext.clearDirectoryBeforeUpload)
                    }
                }
            } finally {
                connection.disconnect()
            }
        } else {
            println("\uD83D\uDD25 Source directory not exists!")
            println("sourceDir was (${ext.sourceDir})")
        }
    }

    @Throws(IllegalStateException::class)
    fun SFTPClient.upload(files: List<File>, targetDirectory: String, clearDirectoryBeforeUpload: Boolean) {
        if (clearDirectoryBeforeUpload) cleanTargetDirectory(this, targetDirectory)
        upload(files, targetDirectory, this)
    }

    @Throws(IllegalStateException::class)
    private fun upload(files: List<File>, dir: String, client: SFTPClient) {
        files.forEach { file ->
            if (file.isFile) {
                println("Upload ${file.path} - ${file.name}")
                println("to $dir - ${file.name}")
                client.put(FileSystemFile(file), dir)
            } else if (file.isDirectory) {
                println("Create dir ${file.name}")
                client.mkdirs("$dir/${file.name}")
                file.listFiles()?.toList()?.let {
                    upload(it, "$dir/${file.name}", client)
                }
            }
        }
    }
}