package khm.apocalypse.mod.client

import khm.apocalypse.mod.ForgeMod
import khm.apocalypse.mod.ModRegistry
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.RegistryObject
import java.util.function.Consumer


@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ForgeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
class ClientModBusEvents {

    @SubscribeEvent
    fun onBuildCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        if (event.tabKey === CreativeModeTabs.NATURAL_BLOCKS) {
            ModRegistry.ITEMS.getEntries()
                .forEach(Consumer { itemRegistryObject: RegistryObject<Item> -> event.accept(itemRegistryObject.get()) })
        }
    }
}