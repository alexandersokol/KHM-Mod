package khm.apocalypse.mod.paint

import khm.apocalypse.mod.ForgeMod
import net.minecraft.world.entity.decoration.PaintingVariant
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object ModPaintingVariants {

    val PAINTING_VARIANTS = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, ForgeMod.MOD_ID)

    val PLANT = PAINTING_VARIANTS.register("plant") { PaintingVariant(32, 16) }

    fun register(bus: IEventBus) {
        PAINTING_VARIANTS.register(bus)
    }
}