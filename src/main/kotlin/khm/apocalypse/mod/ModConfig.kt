package khm.apocalypse.mod

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object ModConfig {

    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val configPath: Path = Paths.get("config/khm_mod", "config.json")

    var isBedTeleportEnabled: Boolean = true
        private set

    var isSpawnTeleportEnabled: Boolean = true
        private set

    var teleportDelaySeconds: Int = 10
        private set

    var syncKubeJsScripts: Boolean = true
        private set

    private data class ConfigData(
        val isBedTeleportEnabled: Boolean = true,
        val isSpawnTeleportEnabled: Boolean = true,
        val teleportDelaySeconds: Int = 10,
        val syncKubeJsScripts: Boolean = true
    )

    fun load() {
        try {
            if (!Files.exists(configPath)) {
                saveDefaults()
                return
            }

            val reader = Files.newBufferedReader(configPath)
            val config = gson.fromJson(reader, ConfigData::class.java)
            reader.close()

            isBedTeleportEnabled = config.isBedTeleportEnabled
            isSpawnTeleportEnabled = config.isSpawnTeleportEnabled
            teleportDelaySeconds = config.teleportDelaySeconds
            syncKubeJsScripts = config.syncKubeJsScripts

            println("[khm_apocalypse_mod] Config loaded successfully.")

        } catch (e: JsonSyntaxException) {
            println("[khm_apocalypse_mod] Invalid JSON config, using defaults.")
            saveDefaults()
        } catch (e: Exception) {
            println("[khm_apocalypse_mod] Failed to load config: ${e.message}")
        }
    }

    private fun saveDefaults() {
        try {
            val configDir = configPath.parent.toFile()
            if (!configDir.exists()) configDir.mkdirs()

            val writer = Files.newBufferedWriter(configPath)
            val defaultConfig = ConfigData()
            gson.toJson(defaultConfig, writer)
            writer.close()

            println("[khm_apocalypse_mod] Default config created at $configPath")
        } catch (e: Exception) {
            println("[khm_apocalypse_mod] Failed to save default config: ${e.message}")
        }
    }
}