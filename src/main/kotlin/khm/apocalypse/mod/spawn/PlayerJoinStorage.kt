package khm.apocalypse.mod.spawn

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import java.nio.file.Files
import java.nio.file.Path

object PlayerJoinStorage {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun save(player: ServerPlayer) {
        val list = load(player.server).toMutableList()
        list.add(player.name.string)

        val path = getPath(player.server)
        Files.createDirectories(path.parent)
        Files.write(path, gson.toJson(list.toSet()).toByteArray())
    }

    fun isFirstJoin(player: ServerPlayer): Boolean {
        val list = load(player.server)
        return list.contains(player.name.string).not()
    }

    fun load(server: MinecraftServer): List<String> {
        val path = getPath(server)
        if (!Files.exists(path)) return emptyList()

        val jsonElement = gson.fromJson(Files.readString(path), JsonElement::class.java)

        return if (jsonElement.isJsonArray) {
            jsonElement.asJsonArray.mapNotNull { it.asString }
        } else {
            emptyList()
        }
    }

    private fun getPath(server: MinecraftServer): Path {
        return server.serverDirectory.toPath().resolve("config/khm_mod/player_join.json")
    }

}