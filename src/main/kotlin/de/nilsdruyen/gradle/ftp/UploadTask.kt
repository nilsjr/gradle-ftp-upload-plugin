package de.nilsdruyen.gradle.ftp

import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.sftp.SFTPClient
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import net.schmizz.sshj.xfer.FileSystemFile
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class UploadTask : DefaultTask() {

    @get:Input
    abstract val host: Property<String>

    @get:Input
    abstract val port: Property<Int>

    @get:Input
    abstract val username: Property<String>

    @get:Input
    abstract val password: Property<String>

    @get:Input
    abstract val sourceDir: Property<String>

    @get:Input
    abstract val targetDir: Property<String>

    @TaskAction
    fun run() {
        println("\uD83D\uDE80 Welcome bro, you're using upload plugin v0.4.1")
        val root = File(sourceDir.get())
        if (root.exists() && root.isDirectory) {
            val uploader = FTPUploader(host.get(), port.get(), username.get(), password.get(), targetDir.get())
            try {
                root.listFiles()?.toList()?.let {
                    uploader.rootUpload(it)
                }
            } finally {
                uploader.disconnect()
            }
        } else {
            println("\uD83D\uDD25 Source directory not exists!")
            println("sourceDir was (${sourceDir.get()})")
        }
    }

    class FTPUploader(
        host: String,
        port: Int,
        username: String,
        password: String,
        private val targetDir: String
    ) {

        private val ssh = SSHClient()
        private val client: SFTPClient

        init {
            ssh.loadKnownHosts()
            ssh.addHostKeyVerifier(PromiscuousVerifier())
            ssh.connect(host, port)
            ssh.authPassword(username, password)
            client = ssh.newSFTPClient()
        }

        @Throws(IllegalStateException::class)
        fun rootUpload(files: List<File>) {
            client.use {
                upload(files, targetDir, it)
            }
        }

        @Throws(IllegalStateException::class)
        fun upload(files: List<File>, dir: String, client: SFTPClient) {
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

        fun disconnect() {
            if (ssh.isConnected) {
                ssh.disconnect()
            }
        }
    }
}