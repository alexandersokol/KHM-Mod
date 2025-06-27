const TICK_PER_SECOND = 20
const SLEEP_PERCENT_FALLBACK = 0.3
const DELAY_SLEEP_ANNOUNCE_SECONDS = 3
const DELAY_NIGHT_SKIP_TIMER_START_SECONDS = 10
const DELAY_NIGHT_SKIP_SECONDS = 30

let sleepState = {
  sleepers: new Set(), // Names of sleeping players
  triggered: false,
  preCheckTimer: 0,
  countdownTimer: 0,
  countdownActive: false,
  sleepPercentRequired: SLEEP_PERCENT_FALLBACK,
  lastSleepersCount: 0
}
let seconds = 0

// Try to get playersSleepingPercentage from gamerule
ServerEvents.loaded(event => {
  try {
    let val = event.server.overworld().getGameRules().getInt('playersSleepingPercentage')
    if (!isNaN(val)) {
      sleepState.sleepPercentRequired = val / 100
      console.info(`[auto_sleep] Sleep percentage loaded from gamerule: ${val}%`)
    }
  } catch (e) {
    console.info(`[auto_sleep] Failed to read gamerule. Fallback to 30%`)
  }
})

// Reset all timers and states
const resetSleepSystem = () => {
  sleepState.sleepers.clear()
  sleepState.triggered = false
  sleepState.preCheckTimer = 0
  sleepState.countdownTimer = 0
  sleepState.countdownActive = false
}

// Server tick: track leave + logic
ServerEvents.tick(event => {
  const server = event.server
  const level = server.overworld()
  const players = level.getPlayers()
  const totalCount = players.length

  // Remove players who left the game
  for (const uuidStr of Array.from(sleepState.sleepers)) {
    if (!players.some(p => (p.uuid + '') === uuidStr)) {
      sleepState.sleepers.delete(uuidStr)
    }
  }

  // Track current sleep states
  for (const p of players) {
    if (p.isSleeping()) {
      sleepState.sleepers.add(p.uuid + '')
    } else {
      sleepState.sleepers.delete(p.uuid + '')
    }
  }

  // Reset if nobody sleeping
  if (sleepState.sleepers.size === 0) {
    resetSleepSystem()
    return
  }

  const sleepersCount = sleepState.sleepers.size
  const requiredSleepers = Math.ceil(totalCount * sleepState.sleepPercentRequired)

  if (sleepState.lastSleepersCount != sleepersCount) {
    sleepState.lastSleepersCount = sleepersCount
    console.info(`[auto_sleep] Sleeperst count changed - ${sleepersCount}/${requiredSleepers}`)
  }

  // Reset if enough are sleeping
  if (sleepersCount >= requiredSleepers) {
    resetSleepSystem()
    return
  }

  // If no countdown active and logic hasn't started yet, trigger it
  if (!sleepState.triggered && sleepersCount > 0) {
    sleepState.triggered = true
    sleepState.preCheckTimer = DELAY_SLEEP_ANNOUNCE_SECONDS * TICK_PER_SECOND
    console.info(`[auto_sleep] First sleeper detected – sleep announce in ${DELAY_SLEEP_ANNOUNCE_SECONDS}seconds`)
  }

  // 3s delay before GO TO BED
  if (sleepState.preCheckTimer > 0) {
    sleepState.preCheckTimer--
    if (sleepState.preCheckTimer === 0) {
      console.info(`[auto_sleep] GO TO SLEEP announce!`)
      for (const p of players) {
        if (!sleepState.sleepers.has(p.uuid + '')) {
          p.server.runCommandSilent(`title ${p.name.string} title {"text":"СПАТИ!","color":"red","bold":true}`)
          p.server.runCommandSilent(`title ${p.name.string} subtitle {"text":"Ану бігом у ліжко!","color":"gray"}`)
          p.playSound('minecraft:block.bell.use', 1.0, 1.0)
        }
      }
      console.info(`[auto_sleep] Night skip timer starts in ${DELAY_NIGHT_SKIP_TIMER_START_SECONDS}seconds`)
      sleepState.countdownTimer = DELAY_NIGHT_SKIP_TIMER_START_SECONDS * TICK_PER_SECOND
    }
    return
  }

  // After 10s start countdown
  if (!sleepState.countdownActive && sleepState.countdownTimer > 0) {
    sleepState.countdownTimer--
    if (sleepState.countdownTimer === 0) {
      console.info('[auto_sleep] Starting 10s countdown timer!')
      sleepState.countdownActive = true
      sleepState.countdownTimer = DELAY_NIGHT_SKIP_SECONDS * TICK_PER_SECOND
      console.info(`[auto_sleep] Night skip in ${DELAY_NIGHT_SKIP_SECONDS}seconds`)
      server.getPlayerList().broadcastSystemMessage(`§6Не всі хоуміки сплять! Через ${DELAY_NIGHT_SKIP_SECONDS}сек настане день!`, false)
    }
    return
  }

  // Countdown in progress
  if (sleepState.countdownActive) {
    sleepState.countdownTimer--

    // Chat every 5s
    if (sleepState.countdownTimer % (5 * TICK_PER_SECOND) === 0) {
      seconds = sleepState.countdownTimer / TICK_PER_SECOND
      console.info(`[auto_sleep] Day change announce in ${seconds}seconds`)
      server.getPlayerList().broadcastSystemMessage(`§eДень настане через ${seconds}сек`, false)
    }

    if (sleepState.countdownTimer <= 0) {
      level.setDayTime(1000)
      server.runCommandSilent("weather clear")
      console.info('[auto_sleep] Skipping night')
      server.getPlayerList().broadcastSystemMessage(`§2Настав новий день! Доброго ранку!`, false)
      resetSleepSystem()
    }
  }
})
