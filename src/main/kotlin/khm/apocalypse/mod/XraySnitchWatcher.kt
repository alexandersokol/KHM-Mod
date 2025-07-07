package khm.apocalypse.mod

import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Mod.EventBusSubscriber(modid = ForgeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object XraySnitchWatcher {

    private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
    private val CHECK_INTERVAL_TICKS = TimeUnit.MINUTES.toSeconds(3).toInt() * 20
    private var tickCounter = 0

    @SubscribeEvent
    @JvmStatic
    fun onServerTick(event: TickEvent.ServerTickEvent) {
        if (event.phase != TickEvent.Phase.END) return
        tickCounter++
        if (tickCounter >= CHECK_INTERVAL_TICKS) {
            tickCounter = 0
            runScan(event.server)
        }
    }

    private fun runScan(server: MinecraftServer) {
        val snitchDir = Path.of("config/xray_snitch")
        val kickedDir = snitchDir.resolve("kicked")

        if (!Files.exists(snitchDir)) return
        try {
            Files.createDirectories(kickedDir)
        } catch (e: IOException) {
            ForgeMod.LOGGER.error("Could not create kicked folder for XraySnitch: ${e.message}")
            return
        }

        Files.list(snitchDir).use { paths ->
            paths.filter { it.toString().endsWith(".txt") && !it.toString().contains("/kicked/") }
                .forEach { file ->
                    val filename = file.fileName.toString().removeSuffix(".txt")
                    val uuid = try {
                        UUID.fromString(filename)
                    } catch (e: IllegalArgumentException) {
                        ForgeMod.LOGGER.warn("Skipping non-UUID file: $filename")
                        return@forEach
                    }

                    server.playerList.getPlayer(uuid)?.let {
                        kickPlayerAndArchive(file, it, kickedDir)
                    }
                }
        }
    }

    private fun kickPlayerAndArchive(file: Path, player: ServerPlayer, kickedDir: Path) {
        val timestamp = DATE_FORMAT.format(Date())
        val archiveName = "${timestamp}_${player.uuid}.txt"
        val archivePath = kickedDir.resolve(archiveName)
        try {
            Files.move(file, archivePath, StandardCopyOption.REPLACE_EXISTING)
            ForgeMod.LOGGER.warn("Xray detected: ${player.name.string}. Kicked and archived to $archivePath")

            player.connection.disconnect(
                Component.literal(
                    "\u274C Looks like you're using cheats.\n\n\u26D4 You have been kicked from the server!"
                )
            )
        } catch (e: IOException) {
            ForgeMod.LOGGER.error("Failed to archive Xray Snitch report for ${player.name.string}: ${e.message}")
        }
    }

}