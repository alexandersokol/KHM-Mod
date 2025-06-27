LootJS.modifiers((event) => {
  // Loot tables for Moon structures
  const adAstraMoonLoot = [
    "ad_astra:chests/dungeon/moon/dungeon_chest",
    "ad_astra:chests/dungeon/moon/large_dungeon_chest",
    "ad_astra:chests/temple/mars/temple",
    "ad_astra:chests/village/moon/blacksmith",
    "ad_astra:chests/village/moon/house",
  ];

  adAstraMoonLoot.forEach((id) => {
    event
      .addLootTableModifier(id)
      .randomChance(0.56)
      .addLoot(
        Item.of("minecraft:ghast_tear", Math.floor(Math.random() * 5) + 1)
      );
  });

  const adAstraMoonDungeonLoot = [
    "ad_astra:chests/dungeon/moon/dungeon_chest",
    "ad_astra:chests/dungeon/moon/large_dungeon_chest",
    "ad_astra:chests/temple/mars/temple",
  ];

  adAstraMoonDungeonLoot.forEach((id) => {
    event
      .addLootTableModifier(id)
      .randomChance(0.15)
      .addLoot(
        Item.of("mekanism:ingot_steel", Math.floor(Math.random() * 5) + 1)
      );

    event
      .addLootTableModifier(id)
      .randomChance(0.07)
      .addLoot(
        Item.of("mekanism:alloy_infused", Math.floor(Math.random() * 3) + 1)
      );

    event
      .addLootTableModifier(id)
      .randomChance(0.12)
      .addLoot(
        Item.of(
          "mekanism:basic_control_circuit",
          Math.floor(Math.random() * 3) + 1
        )
      );

    event
      .addLootTableModifier(id)
      .randomChance(0.05)
      .addLoot(
        Item.of("mekanism:alloy_reinforced", Math.floor(Math.random() * 2) + 1)
      );

    event
      .addLootTableModifier(id)
      .randomChance(0.01)
      .addLoot("mekanism:alloy_atomic");

    event
      .addLootTableModifier(id)
      .randomChance(0.15)
      .addLoot(
        Item.of("ae2:logic_processor", Math.floor(Math.random() * 4) + 1)
      );

    event
      .addLootTableModifier(id)
      .randomChance(0.1)
      .addLoot(
        Item.of("ae2:calculation_processor", Math.floor(Math.random() * 3) + 1)
      );

    event
      .addLootTableModifier(id)
      .randomChance(0.05)
      .addLoot("ae2:engeneering_processor");
  });

  const adAstraMarsDungeonLoot = [
    "ad_astra:chests/temple/mars/temple",
  ];

  adAstraMarsDungeonLoot.forEach((id) => {
    event
      .addLootTableModifier(id)
      .randomChance(0.03)
      .addLoot(Item.of("mekanism:robit", Math.floor(Math.random() * 5) + 1));

    event.addLootTableModifier(id).randomChance(0.08).addLoot("ae2:controller");

    event
      .addLootTableModifier(id)
      .randomChance(0.08)
      .addLoot("ae2:crafting_accelerator");
  });
});
