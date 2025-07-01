package khm.apocalypse.mod.spawn

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.core.BlockPos
import net.minecraft.server.MinecraftServer
import java.nio.file.Files
import java.nio.file.Path

object SpawnStorage {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun save(server: MinecraftServer, name: String, pos: BlockPos) {
        val map = load(server).toMutableMap()
        map[name] = pos
        val json = JsonObject()
        map.forEach { (key, value) ->
            val arr = JsonArray()
            arr.add(value.x)
            arr.add(value.y)
            arr.add(value.z)
            json.add(key, arr)
        }

        val path = getPath(server)
        Files.createDirectories(path.parent)
        Files.write(path, gson.toJson(json).toByteArray())
    }

    fun load(server: MinecraftServer): Map<String, BlockPos> {
        val path = getPath(server)
        if (!Files.exists(path)) return emptyMap()
        val json = gson.fromJson(Files.readString(path), JsonObject::class.java)
        return json.entrySet().associate { (key, value) ->
            val arr = value.asJsonArray
            key to BlockPos(arr[0].asInt, arr[1].asInt, arr[2].asInt)
        }
    }

    fun remove(server: MinecraftServer, name: String): Boolean {
        val map = load(server).toMutableMap()
        val removed = map.remove(name) != null
        if (removed) {
            val json = JsonObject()
            map.forEach { (key, value) ->
                val arr = JsonArray()
                arr.add(value.x)
                arr.add(value.y)
                arr.add(value.z)
                json.add(key, arr)
            }

            val path = getPath(server)
            Files.createDirectories(path.parent)
            Files.write(path, gson.toJson(json).toByteArray())
        }
        return removed
    }

    private fun getPath(server: MinecraftServer): Path {
        return server.serverDirectory.toPath().resolve("config/khm_mod/spawn_points.json")
    }
}
