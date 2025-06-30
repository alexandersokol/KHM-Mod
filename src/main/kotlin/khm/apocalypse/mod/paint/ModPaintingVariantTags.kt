package khm.apocalypse.mod.paint

import khm.apocalypse.mod.ForgeMod
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.decoration.PaintingVariant

@Suppress("removal", "DEPRECATION")
object ModPaintingVariantTags {

    val KHM_PAINTINGS: TagKey<PaintingVariant?> = tag("khm_paintings")

    private fun tag(name: String): TagKey<PaintingVariant?> {
        return TagKey.create(Registries.PAINTING_VARIANT, ResourceLocation(ForgeMod.MOD_ID, name))
    }
}