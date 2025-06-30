package khm.apocalypse.mod.data

import khm.apocalypse.mod.ModBlocks
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
        add(ModBlocks.MOON_QUARTZ_ORE.get(), Items.QUARTZ)
    }

    override fun getKnownBlocks(): Iterable<Block?> {
        return ModBlocks.BLOCKS.entries.map { it.get() }
    }

    private fun add(block: Block, item: Item) {
        add(
            block,
            createSilkTouchDispatchTable(
                block,
                applyExplosionDecay(
                    block,
                    LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f)))
                )
            )
        )
    }
}