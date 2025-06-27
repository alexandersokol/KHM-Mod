const BLOCKED_MOBS = [
  'alexsmobs:cockroach',
  'alexsmobs:centipede_tail',
  'alexsmobs:seagull',
  'alexsmobs:terrapin',
  'aquaculture:minnow',
  'aquaculture:bluegill',
  'aquaculture:tambaqui',
  'aquaculture:atlantic_herring',
  'aquaculture:muskellunge',
  'aquaculture:gar',
  'luminousworld:trout_fish',
  'untitledduckmod:goose',
  'mekanismadditions:baby_creeper',
  'mekanismadditions:baby_enderman',
  'mekanismadditions:baby_skeleton',
  'mekanismadditions:baby_stray',
  'mekanismadditions:baby_wither_skeleton',
  'samurai_dynasty:akaname'
]

EntityEvents.spawned(event => {
  const type = event.entity?.type

  if (!type) return

  let id = null

  if (typeof type === 'string') {
    id = type
  } else if (typeof type.getId === 'function') {
    id = type.getId().toString()
  } else {
    try{
      id = event.entity.type.toString()
    }catch(err){}
  }

  if (BLOCKED_MOBS.includes(id)) {
    event.cancel()
  }
})
