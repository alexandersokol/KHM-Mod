package khm.apocalypse.mod.paint

import khm.apocalypse.mod.ForgeMod
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagEntry
import net.minecraft.world.entity.decoration.PaintingVariant
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.ForgeRegistries
import java.util.concurrent.CompletableFuture

class ModPaintingVariantTagsProvider(
    output: PackOutput,
    completableFuture: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper
) : TagsProvider<PaintingVariant>(
    output,
    Registries.PAINTING_VARIANT,
    completableFuture,
    ForgeMod.MOD_ID,
    existingFileHelper
) {

    override fun addTags(arg: HolderLookup.Provider) {
        ModPaintingVariants.PAINTING_VARIANTS.entries
            .map { it.get() }
            .forEach {
                tag(ModPaintingVariantTags.KHM_PAINTINGS)
                    .add(TagEntry.element(ForgeRegistries.PAINTING_VARIANTS.getKey(it)))
            }
    }
}