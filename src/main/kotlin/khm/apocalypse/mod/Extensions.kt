@file:Suppress("DEPRECATION", "removal")

package khm.apocalypse.mod

import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraftforge.registries.ForgeRegistries
import java.io.File


@Throws(AssertionError::class)
fun oresInGround(k: ResourceLocation): ResourceLocation {
    return if (k.path.startsWith("moon_")) {
        ResourceLocation("forge", "ores_in_ground/moon_stone")
    } else if (k.path.startsWith("mars_")) {
        ResourceLocation("forge", "ores_in_ground/mars_stone")
    } else if (k.path.startsWith("venus_")) {
        ResourceLocation("forge", "ores_in_ground/venus_stone")
    } else if (k.path.startsWith("mercury_")) {
        ResourceLocation("forge", "ores_in_ground/mercury_stone")
    } else if (k.path.startsWith("glacio_")) {
        ResourceLocation("forge", "ores_in_ground/glacio_stone")
    } else {
        throw AssertionError(String.format("Can't make ores_in_ground/ item tag for %s", k))
    }
}

fun dumpModItems(source: CommandSourceStack, modid: String) {
    val filteredItems = ForgeRegistries.ITEMS.entries
        .mapNotNull { ForgeRegistries.ITEMS.getKey(it.value) }
        .filter { it.namespace == modid }

    if (filteredItems.isEmpty()) {
        source.sendSystemMessage(Component.literal("❌ No items found for modid '$modid'"))
        return
    }

    // Log to console
    ForgeMod.LOGGER.info("========== Items for mod: $modid ==========")
    filteredItems.sortedBy { it.path }.forEach {
        ForgeMod.LOGGER.info(" - ${it.namespace}:${it.path}")
    }

    try {
        val server: MinecraftServer = source.server
        val file = File(server.serverDirectory, "logs/moditems_$modid.txt")
        file.parentFile.mkdirs()
        file.printWriter().use { writer ->
            writer.println("========== Items for mod: $modid ==========")
            filteredItems.sortedBy { it.path }.forEach {
                writer.println("${it.namespace}:${it.path}")
            }
        }
        source.sendSystemMessage(
            Component.literal(
                "✅ Found ${filteredItems.size} items. Saved to ${
                    file.relativeTo(
                        server.serverDirectory
                    )
                }"
            )
        )
    } catch (e: Exception) {
        source.sendSystemMessage(Component.literal("⚠ Error writing moditems_$modid.txt: ${e.message}"))
        ForgeMod.LOGGER.error("Failed to write item list for modid '$modid'", e)
    }
}