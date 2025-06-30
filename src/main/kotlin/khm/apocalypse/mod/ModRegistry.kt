package khm.apocalypse.mod

import khm.apocalypse.mod.paint.ModPaintingVariants
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.DeferredRegister

@Mod.EventBusSubscriber(modid = ForgeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object ModRegistry {

    val CREATIVE_MODE_TABS: DeferredRegister<CreativeModeTab> = DeferredRegister.create(
        Registries.CREATIVE_MODE_TAB,
        ForgeMod.MOD_ID
    )


    fun register(bus: IEventBus) {

        CREATIVE_MODE_TABS.register("items") {
            CreativeModeTab.builder()
                .title(Component.literal(ForgeMod.MOD_NAME))
                .icon { ModItems.ITEMS.entries.first().get().defaultInstance }
                .displayItems { arg, arg2 ->
                    ModItems.ITEMS.entries.sortedBy { it.id.path }
                        .forEach { arg2.accept { it.get() } }
                    ModItems.BLOCK_ITEMS.entries.sortedBy { it.id.path }
                        .forEach { arg2.accept { it.get() } }
                }
                .build()
        }

        ModBlocks.BLOCKS.register(bus)
        ModBlocks.OTHER_BLOCKS.register(bus)
        ModItems.BLOCK_ITEMS.register(bus)
        ModItems.ITEMS.register(bus)
        ModPaintingVariants.register(bus)
        CREATIVE_MODE_TABS.register(bus)
    }
}