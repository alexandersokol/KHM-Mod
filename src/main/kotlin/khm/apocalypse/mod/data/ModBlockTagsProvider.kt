package khm.apocalypse.mod.data

import khm.apocalypse.mod.ForgeMod
import khm.apocalypse.mod.ModRegistry
import khm.apocalypse.mod.oresInGround
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraftforge.common.Tags
import net.minecraftforge.common.data.BlockTagsProvider
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

@Suppress("DEPRECATION", "removal")
class ModBlockTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : BlockTagsProvider(output, lookupProvider, ForgeMod.MOD_ID, existingFileHelper) {

    override fun addTags(arg: HolderLookup.Provider) {
        ModRegistry.BLOCKS.entries.forEach {
            // forge:ores
            this.tag(Tags.Blocks.ORES).add(it.get())
            this.tag(Tags.Blocks.ORE_RATES_SINGULAR).add(it.get())
            // forge:ores_in_ground/...
            this.tag(BlockTags.create(oresInGround(it.id))).add(it.get())

            // minecraft:mineable/pickaxe
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(it.get())

            // minecraft:needs_stone_tool
            this.tag(BlockTags.NEEDS_STONE_TOOL).add(it.get())

            // minecraft:snaps_goat_horn
            this.tag(BlockTags.SNAPS_GOAT_HORN).add(it.get())

            // forge:mineable/paxel
            this.tag(BlockTags.create(ResourceLocation("forge", "mineable/paxel"))).add(it.get())

            // minecraft:overworld_carver_replaceables
            this.tag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(it.get())

            // mekanism:atomic_disassembler_ore
            this.tag(BlockTags.create(ResourceLocation("mekanism", "atomic_disassembler_ore"))).add(it.get())
        }
    }
}