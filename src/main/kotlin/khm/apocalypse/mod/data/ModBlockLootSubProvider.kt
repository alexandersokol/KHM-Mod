package khm.apocalypse.mod.data

import khm.apocalypse.mod.ModRegistry
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block

class ModBlockLootSubProvider : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags()) {

    override fun generate() {
        add(ModRegistry.MOON_QUARTZ_ORE.get(), Items.QUARTZ)
    }

    override fun getKnownBlocks(): Iterable<Block?> {
        return ModRegistry.BLOCKS.entries.map { it.get() }
    }

    private fun add(block: Block, item: Item) {
        add(block, createOreDrop(block, item))
    }
}