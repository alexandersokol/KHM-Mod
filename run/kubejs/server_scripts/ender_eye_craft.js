ServerEvents.recipes(event => {
  // Remove the vanilla Eye of Ender recipe
  event.remove({ output: 'minecraft:ender_eye' })

  // Add a new shapeless recipe
  event.shapeless('minecraft:ender_eye', [
    'minecraft:ender_pearl',
    'minecraft:blaze_powder'
  ])
})