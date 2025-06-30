package khm.apocalypse.mod

import khm.apocalypse.mod.paint.KHMPaintingItem
import net.minecraft.world.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object ModItems {

    val BLOCK_ITEMS: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, ForgeMod.MOD_ID)
    val ITEMS: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, ForgeMod.MOD_ID)

    val KHM_PAINTING = ITEMS.register("khm_painting") { KHMPaintingItem(Item.Properties()) }

}