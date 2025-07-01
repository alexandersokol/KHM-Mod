package khm.apocalypse.mod.kubejs

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import khm.apocalypse.mod.ForgeMod
import khm.apocalypse.mod.ModConfig
import org.apache.logging.log4j.Level
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object KubeJSRemoteSyncer {

    fun sync() {
        if (ModConfig.syncKubeJsScripts) {
            try {
                run()
            } catch (ex: Exception) {
                ForgeMod.LOGGER.log(Level.DEBUG, "Failed to sync KubeJS scripts", ex)
            }
        }
    }

    private fun run() {
        val host = "wa3-perf.mineconnect.xyz"
        val port = 2221
        val username = "a2ory65v8b.5402a7c9"
        val password = "kLZA7mCkkHx.29sV"
        val remoteRoot = "kubejs"
        val localRoot = Paths.get("kubejs")

        val jsch = JSch()
        val session: Session = jsch.getSession(username, host, port)
        session.setPassword(password)
        session.setConfig("StrictHostKeyChecking", "no")
        session.connect()

        val channel = session.openChannel("sftp") as ChannelSftp
        channel.connect()

        try {
            // Start recursive sync
            Files.createDirectories(localRoot)
            val remoteFiles = mutableSetOf<String>()
            syncDirectory(channel, remoteRoot, localRoot, remoteFiles)
            // Cleanup: delete local files that don't exist remotely
            cleanupLocal(localRoot, remoteFiles)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


        channel.disconnect()
        session.disconnect()
    }

    private fun syncDirectory(
        channel: ChannelSftp,
        remotePath: String,
        localPath: Path,
        remoteFiles: MutableSet<String>
    ) {
        channel.cd(remotePath)
        Files.createDirectories(localPath)

        val entries = channel.ls(".")
        for (entry in entries) {
            val lsEntry = entry as ChannelSftp.LsEntry
            val name = lsEntry.filename
            if (name == "." || name == "..") continue

            val remoteFilePath = "$name"
            val localFilePath = localPath.resolve(name)

            if (lsEntry.attrs.isDir) {
                syncDirectory(channel, remoteFilePath, localFilePath, remoteFiles)
                channel.cd("..")  // Go back after recursion
            } else {
                remoteFiles.add(localFilePath.toAbsolutePath().normalize().toString())
                val localFile = localFilePath.toFile()
                if (localFile.exists() && localFile.length() == lsEntry.attrs.size) {
                    continue
                }

                FileOutputStream(localFile).use { output ->
                    channel.get(name, output)
                }
            }
        }
    }

    private fun cleanupLocal(localPath: Path, remoteFiles: Set<String>) {
        Files.walk(localPath)
            .filter { Files.isRegularFile(it) }
            .forEach { path ->
                if (path.toAbsolutePath().normalize().toString() !in remoteFiles) {
                    Files.delete(path)
                }
            }
    }
}