package khm.apocalypse.mod.spawn

import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import java.util.*

object DelayedTeleportHandler {
    private val pendingTeleports = mutableMapOf<UUID, Pair<ServerPlayer, Int>>() // (Player, ticks left)
    private val lastPositions = mutableMapOf<UUID, Vec3>()


    fun requestTeleport(player: ServerPlayer) {
        pendingTeleports[player.uuid] = Pair(player, 200)
        lastPositions[player.uuid] = player.position()
        player.sendSystemMessage(Component.literal("Телепорт на спавн за 10сек. Не рухайся!"))
    }

    fun tick() {
        val toTeleport = mutableListOf<ServerPlayer>()
        val iterator = pendingTeleports.iterator()

        while (iterator.hasNext()) {
            val (uuid, pair) = iterator.next()
            val (player, ticksLeft) = pair

            val lastPos = lastPositions[uuid]
            if (lastPos != null && player.position().distanceToSqr(lastPos) > 0.01) {
                player.sendSystemMessage(Component.literal("Телепорт скасовано бо ти не стояв на місці!"))
                iterator.remove()
                lastPositions.remove(uuid)
                continue
            }

            if (ticksLeft <= 0) {
                toTeleport.add(player)
                iterator.remove()
                lastPositions.remove(uuid)
            } else {
                if (ticksLeft % 20 == 0) {
                    val secondsLeft = ticksLeft / 20
                    player.sendSystemMessage(Component.literal("Телепортуємось через $secondsLeft секунд"))
                }
                pendingTeleports[uuid] = player to (ticksLeft - 1)
            }
        }

        for (player in toTeleport) {
            PlayerSpawnHandler.teleportToRandomSpawn(player)
        }
    }


    fun isTeleporting(player: ServerPlayer): Boolean {
        return pendingTeleports.containsKey(player.uuid)
    }
}
