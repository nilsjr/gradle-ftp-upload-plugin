package de.nilsdruyen.gradle.ftp.tasks

import net.schmizz.sshj.sftp.RemoteResourceInfo
import net.schmizz.sshj.sftp.SFTPClient

@Throws(IllegalStateException::class)
fun cleanTargetDirectory(client: SFTPClient, targetDir: String) {
    val items = client.ls(targetDir)
    clearDirectory(client, items)

    if (client.ls(targetDir).size > 0) {
        println("Target directory is not empty")
    } else {
        client.rmdir(targetDir)
        client.mkdir(targetDir)

        println("Target directory cleared & recreated")
    }
}

fun clearDirectory(client: SFTPClient, items: List<RemoteResourceInfo>) {
    items.forEach {
        if (it.isDirectory) {
            clearDirectory(client, client.ls(it.path))
            client.rmdir(it.path)
        } else {
            val filePath = it.path
            println("Remove $filePath")
            client.rm(filePath)
        }
    }
}