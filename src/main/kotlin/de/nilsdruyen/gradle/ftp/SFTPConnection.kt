package de.nilsdruyen.gradle.ftp

import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.sftp.SFTPClient
import net.schmizz.sshj.transport.verification.PromiscuousVerifier

class SFTPConnection(
    host: String,
    port: Int,
    username: String,
    password: String,
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

    fun with(block: SFTPClient.() -> Unit) {
        client.use {
            it.block()
        }
    }

    fun disconnect() {
        if (ssh.isConnected) {
            ssh.disconnect()
        }
    }
}