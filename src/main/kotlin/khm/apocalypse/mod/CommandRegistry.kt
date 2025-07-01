package khm.apocalypse.mod

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import khm.apocalypse.mod.spawn.DelayedTeleportHandler
import khm.apocalypse.mod.spawn.PlayerSpawnHandler
import khm.apocalypse.mod.spawn.SpawnStorage
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = ForgeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object CommandRegistry {

    @SubscribeEvent
    fun onRegisterCommands(event: RegisterCommandsEvent) {
        event.dispatcher.register(
            Commands.literal("spawn")
                .executes { ctx ->
                    val player = ctx.source.playerOrException
                    if (!player.hasPermissions(2)) {
                        // not OP → request delayed teleport
                        if (DelayedTeleportHandler.isTeleporting(player)) {
                            player.sendSystemMessage(Component.literal("Ти вже очікуєш телепорт..."))
                        } else {
                            DelayedTeleportHandler.requestTeleportToSpawn(player)
                        }
                        Command.SINGLE_SUCCESS
                    } else {
                        // OP → teleport instantly
                        PlayerSpawnHandler.teleportToRandomSpawn(player)
                        Command.SINGLE_SUCCESS
                    }
                }
                .then(
                    Commands.literal("add")
                        .requires { it.hasPermission(2) }
                        .then(
                            Commands.argument("name", StringArgumentType.word())
                                .executes { ctx ->
                                    val name = StringArgumentType.getString(ctx, "name")
                                    val player = ctx.source.playerOrException
                                    val pos = player.blockPosition()
                                    SpawnStorage.save(ctx.source.server, name, pos)
                                    ctx.source.sendSuccess(
                                        { Component.literal("Spawn point '$name' saved at $pos") },
                                        false
                                    )
                                    1
                                }
                        )
                )
                .then(
                    Commands.literal("remove")
                        .requires { it.hasPermission(2) }
                        .then(
                            Commands.argument("name", StringArgumentType.word())
                                .executes { ctx ->
                                    val name = StringArgumentType.getString(ctx, "name")
                                    val removed = SpawnStorage.remove(ctx.source.server, name)
                                    if (removed) {
                                        ctx.source.sendSuccess(
                                            { Component.literal("❌ Spawn point '$name' removed.") },
                                            false
                                        )
                                    } else {
                                        ctx.source.sendFailure(Component.literal("⚠ Spawn point '$name' does not exist."))
                                    }
                                    1
                                }
                        )
                )
                .then(
                    Commands.literal("list")
                        .requires { it.hasPermission(2) }
                        .executes { ctx ->
                            val spawns = SpawnStorage.load(ctx.source.server)
                            if (spawns.isEmpty()) {
                                ctx.source.sendSuccess({ Component.literal("📭 No spawn points saved.") }, false)
                            } else {
                                val lines = spawns.entries.joinToString("\n") { (name, pos) ->
                                    "• $name → ${pos.x} ${pos.y} ${pos.z}"
                                }
                                ctx.source.sendSuccess({ Component.literal("📌 Spawn Points:\n$lines") }, false)
                            }
                            1
                        }
                )
        )

        event.dispatcher.register(
            Commands.literal("home")
                .requires { true } // Available to all players
                .executes { context ->
                    val player = context.source.playerOrException
                    val bedPos = player.respawnPosition

                    if (bedPos == null) {
                        player.sendSystemMessage(Component.literal("§c⚠ Точку відродження (ліжко) не встановлено!"))
                        return@executes 0
                    }

                    DelayedTeleportHandler.requestTeleportToBed(player)
                    1
                }
        )
    }

}