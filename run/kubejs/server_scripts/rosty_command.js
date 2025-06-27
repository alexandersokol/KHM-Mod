ServerEvents.commandRegistry(event => {
  event.register(
    event.commands.literal('rosty')
      .requires(source => source.hasPermission(0)) // 0 = all players
      .executes(ctx => {
        let server = ctx.source.server
        server.runCommandSilent("mek radiation removeAll")
        return 1
      })
  )
})