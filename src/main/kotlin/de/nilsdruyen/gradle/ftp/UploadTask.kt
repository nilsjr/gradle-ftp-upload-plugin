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
    val root = File(sourceDir.get())
    if (root.exists()) {
      val uploader = FTPUploader(host.get(), port.get(), username.get(), password.get(), targetDir.get())
      try {
        root.listFiles()?.let {
          uploader.upload(it.toList())
        }
      } finally {
        uploader.disconnect()
      }
    } else {
      println("Source directory not exists!")
    }
  }

  class FTPUploader(host: String, port: Int, username: String, password: String, val targetDir: String) {

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
    fun upload(files: List<File>) {
      client.use {
        files.forEach { file ->
          println("Upload ${file.name}")
          it.put(FileSystemFile(file), targetDir)
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