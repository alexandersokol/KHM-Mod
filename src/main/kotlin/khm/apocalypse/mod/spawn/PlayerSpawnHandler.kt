package khm.apocalypse.mod.spawn

import khm.apocalypse.mod.ForgeMod
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = ForgeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object PlayerSpawnHandler {

    @SubscribeEvent
    @JvmStatic
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        if (!event.entity.level().isClientSide) {
            teleportToRandomSpawn(event.entity as ServerPlayer)
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        if (!event.entity.level().isClientSide) {
            teleportToRandomSpawn(event.entity as ServerPlayer)
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun onServerTick(event: TickEvent.ServerTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            DelayedTeleportHandler.tick()
        }
    }

    fun teleportToRandomSpawn(player: ServerPlayer) {
        val spawns = SpawnStorage.load(player.server)
        if (spawns.isNotEmpty()) {
            val pos = spawns.values.random()
            player.teleportTo(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5)
        } else {
            // Fallback
            player.teleportTo(0.5, 120.0, 0.5)
        }
    }
}
