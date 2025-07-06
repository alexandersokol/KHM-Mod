package khm.apocalypse.mod.spawn

import khm.apocalypse.mod.ForgeMod
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = ForgeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object PlayerSpawnHandler {

    private const val TAG_FIRST_JOINED = "khm_has_joined_before"

    @SubscribeEvent
    @JvmStatic
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        if (!event.entity.level().isClientSide) {
            val player = event.entity as ServerPlayer
            if (player.respawnPosition == null) {
                teleportToRandomSpawn(player)
            }
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity as? ServerPlayer ?: return
        if (player.level().isClientSide) return

        val data: CompoundTag = player.persistentData
        if (PlayerJoinStorage.isFirstJoin(player) || !data.getBoolean(TAG_FIRST_JOINED)) {
            teleportToRandomSpawn(player)
            data.putBoolean(TAG_FIRST_JOINED, true)
            PlayerJoinStorage.save(player)
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
        val overworld = player.server.getLevel(Level.OVERWORLD)
        if (overworld != null) {
            val spawns = SpawnStorage.load(player.server)
            val pos = if (spawns.isNotEmpty()) {
                spawns.values.random()
            } else {
                BlockPos(0, 120, 0) // Fallback
            }

            player.teleportTo(
                overworld,
                pos.x + 0.5,
                pos.y.toDouble(),
                pos.z + 0.5,
                player.yRot,
                player.xRot
            )
            player.sendSystemMessage(
                Component.literal("Тебе телепортовано на спавн.").withStyle { it.withColor(0x0ba300) })
        } else {
            player.sendSystemMessage(
                Component.literal("⚠ Неможливо телепортувати на спавн: Overworld не знайдено.")
                    .withStyle { it.withColor(0xd40f22) }
            )
        }
    }
}
