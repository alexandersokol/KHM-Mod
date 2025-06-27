let shutdownTimer = null

ServerEvents.commandRegistry(event => {
  event.register(
    event.commands.literal('shutdown')
      .requires(source => source.hasPermission(2)) // Only OPs (level 2+) can use
      .executes(ctx => {
        const server = ctx.source.server

        if (shutdownTimer !== null) {
          ctx.source.sendSystemMessage("§cA shutdown is already in progress.")
          return 0
        }

        const seconds = 180
        const shutdownTime = Date.now() + seconds * 1000

        ctx.source.sendSystemMessage("§eShutdown scheduled in 3 minutes.")
        server.getPlayerList().broadcastSystemMessage("§6Рестарт сервера через 3 хвилини! (Приблизно на 1-2хв)", false)

        const tick = () => {
          const remaining = Math.floor((shutdownTime - Date.now()) / 1000)

          if (remaining <= 0) {
            server.getPlayerList().broadcastSystemMessage("§cСервер пішов в рестарт! (Приблизно на 1-2хв)", false)
            // server.getCommands().performCommand(server.createCommandSourceStack(), "stop")
            server.runCommandSilent("stop")
            shutdownTimer = null
            return
          }

          if (
            (remaining % 60 === 0 && remaining > 60) ||
            remaining === 60 ||
            remaining === 30 ||
            remaining === 15 ||
            remaining === 10 ||
            remaining === 5
          ) {
            const msg = remaining >= 60
              ? `§eРестарт сервера через ${Math.floor(remaining / 60)} хвилини!`
              : `§eРестарт сервера через ${remaining} секунд!`
            server.getPlayerList().broadcastSystemMessage(msg, false)
          }

          shutdownTimer = server.scheduleInTicks(20, tick)
        }

        shutdownTimer = server.scheduleInTicks(20, tick)
        return 1
      })
  )
})
