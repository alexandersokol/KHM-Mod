package khm.apocalypse.mod.spawn

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.ForgeRegistries
import java.util.*

@Mod.EventBusSubscriber(modid = "khm", bus = Mod.EventBusSubscriber.Bus.FORGE)
object ElevatorHandler {

    private const val ELEVATOR_BLOCK_ID = "khm:elevator"

    private val previousSneak = mutableMapOf<UUID, Boolean>()

    @SubscribeEvent
    @JvmStatic
    fun onPlayerJump(event: LivingEvent.LivingJumpEvent) {
        val player = event.entity
        if (player is ServerPlayer && !player.level().isClientSide) {

            val level = player.level()

            val pos = BlockPos.containing(player.x, player.y - 0.1, player.z)
            val blockId = ForgeRegistries.BLOCKS.getKey(level.getBlockState(pos).block)?.toString()
            if (blockId != ELEVATOR_BLOCK_ID || !player.isAlive || player.isRemoved) return

            for (dySearch in 1..256) {
                val target = pos.above(dySearch)
                val isElevator =
                    ForgeRegistries.BLOCKS.getKey(level.getBlockState(target).block)?.toString() == ELEVATOR_BLOCK_ID
                if (isElevator &&
                    level.getBlockState(target.above()).isAir &&
                    level.getBlockState(target.above(2)).isAir
                ) {
                    player.teleportTo(player.x, target.y + 1.0, player.z)
                    player.level().playSound(
                        null, // player (null = everyone hears it)
                        player.x, player.y, player.z,
                        net.minecraft.sounds.SoundEvents.ENDERMAN_TELEPORT,
                        net.minecraft.sounds.SoundSource.PLAYERS,
                        1.0f, 1.0f
                    )
                    (player.level() as ServerLevel).sendParticles(
                        net.minecraft.core.particles.ParticleTypes.PORTAL,
                        player.x, player.y + 0.5, player.z,
                        32, // count
                        0.5, 1.0, 0.5, // offsetX/Y/Z
                        0.1 // speed
                    )
                    break
                }
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onPlayerTick(event: TickEvent.PlayerTickEvent) {
        val player = event.player
        if (player.level().isClientSide || event.phase != TickEvent.Phase.END) return

        val level = player.level()
        val pos = BlockPos.containing(player.x, player.y - 0.1, player.z)
        val blockId = ForgeRegistries.BLOCKS.getKey(level.getBlockState(pos).block)?.toString()

        if (blockId != ELEVATOR_BLOCK_ID) return

        if (!player.isAlive || player.isRemoved) {
            previousSneak.remove(player.uuid)
            return
        }

        val wasSneaking = previousSneak.getOrDefault(player.uuid, false)
        val isSneaking = player.isShiftKeyDown
        val canTeleport = wasSneaking.not() && isSneaking
        previousSneak[player.uuid] = isSneaking

        if (canTeleport) {
            for (dySearch in -1 downTo -256) {
                val target = pos.above(dySearch)
                val isElevator =
                    ForgeRegistries.BLOCKS.getKey(level.getBlockState(target).block)?.toString() == ELEVATOR_BLOCK_ID
                if (isElevator &&
                    level.getBlockState(target.above()).isAir &&
                    level.getBlockState(target.above(2)).isAir
                ) {
                    player.teleportTo(player.x, target.y + 1.0, player.z)
                    player.level().playSound(
                        null, // player (null = everyone hears it)
                        player.x, player.y, player.z,
                        net.minecraft.sounds.SoundEvents.ENDERMAN_TELEPORT,
                        net.minecraft.sounds.SoundSource.PLAYERS,
                        1.0f, 1.0f
                    )
                    (player.level() as ServerLevel).sendParticles(
                        net.minecraft.core.particles.ParticleTypes.PORTAL,
                        player.x, player.y + 0.5, player.z,
                        32, // count
                        0.5, 1.0, 0.5, // offsetX/Y/Z
                        0.1 // speed
                    )
                    break
                }
            }
        }
    }
}