package khm.apocalypse.mod.paint

import khm.apocalypse.mod.ForgeMod
import net.minecraft.world.entity.decoration.PaintingVariant
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object ModPaintingVariants {

    val PAINTING_VARIANTS = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, ForgeMod.MOD_ID)

    val KHMELNITSKYI = PAINTING_VARIANTS.register("khmelnitskyi") { PaintingVariant(32, 32) }
    val ALEX = PAINTING_VARIANTS.register("alex") { PaintingVariant(16, 32) }
    val KHM_FAMQ = PAINTING_VARIANTS.register("khm_famq") { PaintingVariant(32, 16) }
    val DIMA_1 = PAINTING_VARIANTS.register("dima_1") { PaintingVariant(32, 32) }
    val DIMA_2 = PAINTING_VARIANTS.register("dima_2") { PaintingVariant(32, 32) }
    val DIMA_3 = PAINTING_VARIANTS.register("dima_3") { PaintingVariant(16, 16) }
    val DIMA_4 = PAINTING_VARIANTS.register("dima_4") { PaintingVariant(64, 48) }

    fun register(bus: IEventBus) {
        PAINTING_VARIANTS.register(bus)
    }
}