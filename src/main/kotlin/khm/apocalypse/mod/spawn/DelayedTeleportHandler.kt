package khm.apocalypse.mod.spawn

import khm.apocalypse.mod.ModConfig
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.util.*

object DelayedTeleportHandler {
    private val pendingTeleports = mutableMapOf<UUID, PendingTeleport>()
    private val lastPositions = mutableMapOf<UUID, Vec3>()

    enum class DestinationType {
        SPAWN, BED
    }

    data class PendingTeleport(val player: ServerPlayer, val ticksLeft: Int, val type: DestinationType)

    fun requestTeleportToSpawn(player: ServerPlayer) {
        if (!ModConfig.isSpawnTeleportEnabled) {
            player.sendSystemMessage(
                Component.literal("❌ Телепорт на спавн вимкнено в конфігурації.").withStyle { it.withColor(0xd40f22) })
            return
        }

        if (ModConfig.doHostileMobCheckBeforeTeleport && areThereHostileMobsAround(player)) {
            player.sendSystemMessage(
                Component.literal("⚠ Телепорт скасовано: поблизу є ворожі істоти!")
                    .withStyle { it.withColor(0xf29c1e) })
            return
        }

        val delayTicks = ModConfig.teleportDelaySeconds * 20
        pendingTeleports[player.uuid] = PendingTeleport(player, delayTicks, DestinationType.SPAWN)
        lastPositions[player.uuid] = player.position()
        player.sendSystemMessage(
            Component.literal("Телепорт на спавн за ${ModConfig.teleportDelaySeconds} сек. Не рухайся!")
                .withStyle { it.withColor(0x0a9c00) })
    }

    fun requestTeleportToBed(player: ServerPlayer) {
        if (!ModConfig.isBedTeleportEnabled) {
            player.sendSystemMessage(
                Component.literal("❌ Телепорт додому вимкнено в конфігурації.").withStyle { it.withColor(0xd40f22) })
            return
        }

        if (ModConfig.doHostileMobCheckBeforeTeleport && areThereHostileMobsAround(player)) {
            player.sendSystemMessage(
                Component.literal("⚠ Телепорт скасовано: поблизу є ворожі істоти!")
                    .withStyle { it.withColor(0xf29c1e) })
            return
        }

        val bedPos = player.respawnPosition
        if (bedPos == null) {
            player.sendSystemMessage(
                Component.literal("⚠ Точка відродження (ліжко) не встановлено.").withStyle { it.withColor(0xd40f22) })
            return
        }
        val delayTicks = ModConfig.teleportDelaySeconds * 20
        pendingTeleports[player.uuid] = PendingTeleport(player, delayTicks, DestinationType.BED)
        lastPositions[player.uuid] = player.position()
        player.sendSystemMessage(
            Component.literal("Телепорт додому через ${ModConfig.teleportDelaySeconds} сек. Не рухайся!")
                .withStyle { it.withColor(0x0a9c00) })
    }

    fun tick() {
        val toTeleport = mutableListOf<PendingTeleport>()
        val iterator = pendingTeleports.iterator()

        while (iterator.hasNext()) {
            val (uuid, pending) = iterator.next()
            val (player, ticksLeft, type) = pending

            if (!player.isAlive || !player.server.playerList.players.contains(player)) {
                iterator.remove()
                lastPositions.remove(uuid)
                continue
            }

            val lastPos = lastPositions[uuid]
            if (lastPos != null && player.position().distanceToSqr(lastPos) > 0.01) {
                player.sendSystemMessage(
                    Component.literal("Телепорт скасовано бо ти не стояв на місці!")
                        .withStyle { it.withColor(0xFFFF55) })
                iterator.remove()
                lastPositions.remove(uuid)
                continue
            }

            if (ticksLeft <= 0) {
                toTeleport.add(pending)
                iterator.remove()
                lastPositions.remove(uuid)
            } else {
                if (ticksLeft % 20 == 0) {
                    val secondsLeft = ticksLeft / 20
                    player.sendSystemMessage(
                        Component.literal("Телепорт через $secondsLeft сек").withStyle { it.withColor(0x8ac486) })
                }
                pendingTeleports[uuid] = pending.copy(ticksLeft = ticksLeft - 1)
            }
        }

        for (pending in toTeleport) {
            when (pending.type) {
                DestinationType.SPAWN -> {
                    PlayerSpawnHandler.teleportToRandomSpawn(pending.player)
                }

                DestinationType.BED -> {
                    val bedPos: BlockPos? = pending.player.respawnPosition
                    val bedWorld = pending.player.server.getLevel(pending.player.respawnDimension)
                    if (bedPos != null && bedWorld != null) {
                        pending.player.teleportTo(
                            bedWorld,
                            bedPos.x + 0.5,
                            bedPos.y + 0.1,
                            bedPos.z + 0.5,
                            pending.player.yRot,
                            pending.player.xRot
                        )
                        pending.player.sendSystemMessage(
                            Component.literal("Тебе телепортовано додому.").withStyle { it.withColor(0x0ba300) })
                    } else {
                        pending.player.sendSystemMessage(
                            Component.literal("⚠ Точка відродження (ліжко) не встановлено.")
                                .withStyle { it.withColor(0xd40f22) })
                    }
                }
            }
        }
    }

    fun isTeleporting(player: ServerPlayer): Boolean {
        return pendingTeleports.containsKey(player.uuid)
    }

    fun areThereHostileMobsAround(player: ServerPlayer): Boolean {
        return player.level().getEntitiesOfClass(
            LivingEntity::class.java,
            AABB.ofSize(player.position(), 16.0, 6.0, 16.0)
        ).any {
            it.isAlive && it.type.category == MobCategory.MONSTER && it != player
        }
    }
}
