BlockEvents.rightClicked(event => {
  const { player, block, level } = event
  if (!level || level.isClientSide()) return

  // const pillarId = 'kubejs:elevator_pillar'
  let pillarId = 'ad_astra:glowing_dash_pillar'
  let pillarId2 = 'ad_astra:steel_factory_block'

  if (block.id != pillarId && block.id != pillarId2) return

  // Cancel block placement if holding a placeable block
  const heldItem = player.mainHandItem
  if (heldItem && heldItem.isBlock()) {
    event.cancel() // prevents placing the block
  }

  const pos = block.pos
  const x = pos.x
  const y = pos.y
  const z = pos.z

  const maxY = level.getMaxBuildHeight()
  const minY = level.getMinBuildHeight()

  const searchUp = !player.isShiftKeyDown()
  const step = searchUp ? 1 : -1

  for (let offset = step; (searchUp ? y + offset < maxY : y + offset > minY); offset += step) {

    let currentY = y + offset

    let basePos = BlockPos(x, currentY, z)
    let blockAt = level.getBlockState(basePos)

    // If we found another pillar block, try to teleport
    let blockId = blockAt.getBlock().getId()
    if (blockId == pillarId || blockId == pillarId2) {
      let abovePos = BlockPos(x, currentY + 1, z)
      let aboveBlock = level.getBlockState(abovePos)

      let twoBlocksAbovePos = BlockPos(x, currentY + 2, z)
      let twoAboveBlock = level.getBlockState(twoBlocksAbovePos)

      // Ensure player fits — needs two air blocks
      if (aboveBlock.isAir() && twoAboveBlock.isAir()) {
        // Spawn particles before and after
        spawnParticles(level, player.x, player.y, player.z)
        player.setPosition(x + 0.5, currentY + 1, z + 0.5)
        spawnParticles(level, x + 0.5, currentY + 1, z + 0.5)

        level.playSound(null, x, y, z, 'minecraft:entity.enderman.teleport', 'players', 1.0, 1.0)
        return
      }
    }
  }

  player.displayClientMessage(Text.red("Не вдалось знайти відповідний елеватор."), true)
})

function spawnParticles(level, x, y, z) {
  const command = `particle minecraft:portal ${x} ${y + 1} ${z} 0.5 0.5 0.5 0.01 30 force @a`
  level.server.runCommandSilent(command)
}