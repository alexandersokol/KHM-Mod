EntityEvents.death(event => {
  const entity = event.entity
  if (!entity || entity.type !== 'minecraft:cow') return

  const level = event.level
  const pos = entity.position

  const spawnItem = (id, count) => {
    try{
      const stack = Item.of(id, count)
      if (!stack || stack.empty) return
      const item = stack.createEntity(level)
      if (!item) return
      item.setPos(pos.x, pos.y, pos.z)
      level.spawn(item)
    } catch(err) {}
  }

  // 1–4 beef (cooked if burning)
  const beefType = entity.isOnFire() ? 'minecraft:cooked_beef' : 'minecraft:beef'
  spawnItem(beefType, 1 + Math.floor(Math.random() * 4))

  // 1–3 leather
  spawnItem('minecraft:leather', 1 + Math.floor(Math.random() * 3))

  // 1% cow spawn egg
  if (Math.random() < 0.01) {
    spawnItem('minecraft:cow_spawn_egg', 1)
  }
})
