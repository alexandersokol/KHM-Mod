package khm.apocalypse.mod.data

import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets

class ModLootTableProvider(arg: PackOutput) :

    LootTableProvider(
        arg, emptySet(), listOf(
            SubProviderEntry(
                { ModBlockLootSubProvider() },
                LootContextParamSets.BLOCK
            )
        )
    )