package khm.apocalypse.mod.data

import khm.apocalypse.mod.ForgeMod
import khm.apocalypse.mod.ModRegistry
import khm.apocalypse.mod.oresInGround
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.Tags
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModItemTagsProvider(
    arg: PackOutput,
    completableFuture: CompletableFuture<HolderLookup.Provider>,
    completableFuture2: CompletableFuture<TagLookup<Block>>,
    existingFileHelper: ExistingFileHelper
) : ItemTagsProvider(arg, completableFuture, completableFuture2, ForgeMod.MOD_ID, existingFileHelper) {

    override fun addTags(arg: HolderLookup.Provider) {
        ModRegistry.ITEMS.entries.forEach {
            if (it.id.path != ModRegistry.KHM_PAINTING.id.path) {
                // forge:ores
                this.tag(Tags.Items.ORES).add(it.get())

                this.tag(Tags.Items.ORE_RATES_SINGULAR).add(it.get())
                // forge:ores_in_ground/...
                this.tag(ItemTags.create(oresInGround(it.id))).add(it.get())
            }
        }
    }

}