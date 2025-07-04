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
    val GALAXY = PAINTING_VARIANTS.register("galaxy") { PaintingVariant(64, 48) }
    val CREEPER = PAINTING_VARIANTS.register("creeper") { PaintingVariant(16, 32) }
    val ZOMBIE = PAINTING_VARIANTS.register("zombie") { PaintingVariant(32, 48) }
    val BEACH = PAINTING_VARIANTS.register("beach") { PaintingVariant(16, 16) }
    val HOUSE = PAINTING_VARIANTS.register("house") { PaintingVariant(32, 32) }
    val KHM_LOGO = PAINTING_VARIANTS.register("khm_logo") { PaintingVariant(32, 32) }
    val ROCKET = PAINTING_VARIANTS.register("rocket") { PaintingVariant(16, 16) }
    val PLAINS = PAINTING_VARIANTS.register("plains") { PaintingVariant(32, 16) }
    val CLOUDS = PAINTING_VARIANTS.register("clouds") { PaintingVariant(32, 16) }
    val PINK_CLOUDS = PAINTING_VARIANTS.register("pink_clouds") { PaintingVariant(32, 32) }
    val CAT = PAINTING_VARIANTS.register("cat") { PaintingVariant(16, 16) }
    val MOUSE = PAINTING_VARIANTS.register("mouse") { PaintingVariant(32, 16) }
    val ARTEM_1 = PAINTING_VARIANTS.register("artem_1") { PaintingVariant(48, 16) }
    val ARTEM_2 = PAINTING_VARIANTS.register("artem_2") { PaintingVariant(32, 32) }
    val DIMA_1 = PAINTING_VARIANTS.register("dima_1") { PaintingVariant(32, 32) }
    val DIMA_2 = PAINTING_VARIANTS.register("dima_2") { PaintingVariant(32, 32) }
    val DIMA_3 = PAINTING_VARIANTS.register("dima_3") { PaintingVariant(16, 16) }
    val DIMA_4 = PAINTING_VARIANTS.register("dima_4") { PaintingVariant(64, 48) }
    val DIMA_5 = PAINTING_VARIANTS.register("dima_5") { PaintingVariant(64, 48) }

    fun register(bus: IEventBus) {
        PAINTING_VARIANTS.register(bus)
    }
}