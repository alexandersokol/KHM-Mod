package khm.apocalypse.mod.data

import khm.apocalypse.mod.ModBlocks
import khm.apocalypse.mod.ModItems
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

class ModBlockLootSubProvider : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags()) {

    override fun generate() {
        add(ModBlocks.ELEVATOR.get(), ModItems.ELEVATOR.get(), 1, 1)

        add(ModBlocks.MOON_QUARTZ_ORE.get(), Items.QUARTZ, 2, 4)
        add(ModBlocks.MOON_COAL_ORE.get(), Items.COAL, 3, 6)
        add(ModBlocks.MOON_LAPIS_ORE.get(), Items.LAPIS_LAZULI, 1, 2)
        add(ModBlocks.MOON_COPPER_ORE.get(), Items.RAW_COPPER, 1, 2)

        add(ModBlocks.MARS_COAL_ORE.get(), Items.COAL, 4, 7)
        add(ModBlocks.MARS_GOLD_ORE.get(), Items.RAW_GOLD, 1, 2)
        add(ModBlocks.MARS_REDSTONE_ORE.get(), Items.REDSTONE, 4, 8)
        add(ModBlocks.MARS_ANCIENT_DEBRIS.get(), Items.ANCIENT_DEBRIS, 1, 1)

        add(ModBlocks.MERCURY_REDSTONE_ORE.get(), Items.REDSTONE, 4, 8)
        add(ModBlocks.MERCURY_LAPIS_ORE.get(), Items.LAPIS_LAZULI, 1, 4)
        add(ModBlocks.MERCURY_COPPER_ORE.get(), Items.RAW_COPPER, 2, 4)
        add(ModBlocks.MERCURY_DIAMOND_ORE.get(), Items.DIAMOND, 1, 2)
        add(ModBlocks.MERCURY_BASALT_DIAMOND_ORE.get(), Items.DIAMOND, 1, 3)
        add(ModBlocks.MERCURY_BLACKSTONE_DIAMOND_ORE.get(), Items.DIAMOND, 2, 4)

        add(ModBlocks.VENUS_REDSTONE_ORE.get(), Items.REDSTONE, 2, 5)
        add(ModBlocks.VENUS_QUARTZ_ORE.get(), Items.QUARTZ, 2, 10)

        add(ModBlocks.GLACIO_GOLD_ORE.get(), Items.RAW_GOLD, 1, 4)
        add(ModBlocks.GLACIO_DEEPSLATE_GOLD_ORE.get(), Items.RAW_GOLD, 2, 4)
        add(ModBlocks.GLACIO_REDSTONE_ORE.get(), Items.REDSTONE, 3, 5)
        add(ModBlocks.GLACIO_DEEPSLATE_REDSTONE_ORE.get(), Items.REDSTONE, 3, 7)
        add(ModBlocks.GLACIO_DIAMOND_ORE.get(), Items.DIAMOND, 1, 3)
        add(ModBlocks.GLACIO_DEEPSLATE_DIAMOND_ORE.get(), Items.DIAMOND, 1, 5)
        add(ModBlocks.GLACIO_QUARTZ_ORE.get(), Items.QUARTZ, 2, 5)
        add(ModBlocks.GLACIO_DEEPSLATE_ANCIENT_DEBRIS.get(), Items.ANCIENT_DEBRIS, 1, 1)
    }

    override fun getKnownBlocks(): Iterable<Block?> {
        val blocks = ModBlocks.OTHER_BLOCKS.entries + ModBlocks.BLOCKS.entries
        return blocks.map { it.get() }
    }

    private fun add(block: Block, item: Item, min: Int, max: Int) {
        add(
            block,
            createSilkTouchDispatchTable(
                block,
                applyExplosionDecay(
                    block,
                    LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(min.toFloat(), max.toFloat())))
                )
            )
        )
    }
}