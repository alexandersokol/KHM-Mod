ServerEvents.commandRegistry(event => {
  event.register(
    event.commands.literal('die')
      .requires(source => source.hasPermission(0)) // 0 = all players
      .executes(ctx => {
        let player = ctx.source.playerOrException
        player.kill()
        return 1
      })
  )
})