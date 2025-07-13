package khm.apocalypse.mod

import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.GameRules
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = ForgeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object DayNightCycleController {

    @JvmStatic
    @SubscribeEvent
    fun onPlayerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
        if (ModConfig.isDayLightCycleControlEnabled.not()) return

        val server = event.entity.server ?: return
        if (server.playerList.playerCount > 0) {
            setDaylightCycle(server, true)
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onPlayerLeave(event: PlayerEvent.PlayerLoggedOutEvent) {
        if (ModConfig.isDayLightCycleControlEnabled.not()) return

        val server = event.entity.server ?: return
        val actualPlayersCount = server.playerList.players.filterNot { it.uuid == event.entity.uuid }.size

        if (actualPlayersCount == 0) {
            setDaylightCycle(server, false)
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onServerStart(event: ServerStartingEvent) {
        if (ModConfig.isDayLightCycleControlEnabled.not()) return

        val server = event.server
        if (server.playerList.playerCount == 0) {
            setDaylightCycle(server, false)
        }
    }

    private fun setDaylightCycle(server: MinecraftServer, enabled: Boolean) {
        val overworld: ServerLevel = server.overworld()
        val current = overworld.gameRules.getBoolean(GameRules.RULE_DAYLIGHT)
        if (current != enabled) {
            ForgeMod.LOGGER.warn("Setting doDaylightCycle to $enabled")
            overworld.gameRules.getRule(GameRules.RULE_DAYLIGHT).set(enabled, server)
        }
    }
}