LootJS.modifiers((event) => {
  // 🌍 Overworld chests (2% chance)
  const overworldChests = [
    'minecraft:chests/abandoned_mineshaft',
    'minecraft:chests/buried_treasure',
    'minecraft:chests/desert_pyramid',
    'minecraft:chests/jungle_temple',
    'minecraft:chests/shipwreck_supply',
    'minecraft:chests/shipwreck_treasure',
    'minecraft:chests/simple_dungeon',
    'minecraft:chests/stronghold_corridor',
    'minecraft:chests/stronghold_crossing',
    'minecraft:chests/stronghold_library',
    'minecraft:chests/village_armorer',
    'minecraft:chests/village_toolsmith',
    'minecraft:chests/village_weaponsmith',
    'minecraft:chests/woodland_mansion',
    'minecraft:chests/pillager_outpost'
  ]

  overworldChests.forEach(id => {
    event
      .addLootTableModifier(id)
      .randomChance(0.02)
      .addLoot('minecraft:elytra')
  })

  // 🟪 End dimension (15% chance)
  event
    .addLootTableModifier('minecraft:chests/end_city_treasure')
    .randomChance(0.15)
    .addLoot('minecraft:elytra')

  // 🔥 Nether chests (1% chance)
  const netherChests = [
    'minecraft:chests/bastion_bridge',
    'minecraft:chests/bastion_hoglin_stable',
    'minecraft:chests/bastion_other',
    'minecraft:chests/bastion_treasure',
    'minecraft:chests/nether_bridge',
    'minecraft:chests/ruined_portal'
  ]

  netherChests.forEach(id => {
    event
      .addLootTableModifier(id)
      .randomChance(0.01)
      .addLoot('minecraft:elytra')
  })
})