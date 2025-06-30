package khm.apocalypse.mod

import khm.apocalypse.mod.paint.KHMPaintingItem
import khm.apocalypse.mod.paint.ModPaintingVariants
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DropExperienceBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

@Mod.EventBusSubscriber(modid = ForgeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object ModRegistry {

    val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, ForgeMod.MOD_ID)
    val ITEMS: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, ForgeMod.MOD_ID)
    val CREATIVE_MODE_TABS: DeferredRegister<CreativeModeTab> = DeferredRegister.create(
        Registries.CREATIVE_MODE_TAB,
        ForgeMod.MOD_ID
    )

    val MOON_QUARTZ_ORE = register("moon_quartz_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_ORE), UniformInt.of(2, 5))
    }

    private fun register(name: String, supplier: () -> Block): RegistryObject<Block> {
        val block = BLOCKS.register(name, Supplier { supplier.invoke() })
        ITEMS.register(name, Supplier { BlockItem(block.get(), Item.Properties()) })
        return block
    }

// public static final RegistryEntry<Item> SPACE_PAINTING = BASIC_ITEMS.register("space_painting", () -> new SpacePaintingItem(new Item.Properties(), ModPaintingVariants.EARTH, ModPaintingVariantTags.SPACE_PAINTINGS));

    val KHM_PAINTING = ITEMS.register("khm_painting") { KHMPaintingItem(Item.Properties()) }

    fun register(bus: IEventBus) {

        CREATIVE_MODE_TABS.register("items") {
            CreativeModeTab.builder()
                .title(Component.literal(ForgeMod.MOD_NAME))
                .icon { ITEMS.entries.first().get().defaultInstance }
                .displayItems { arg, arg2 ->
                    ITEMS.entries.sortedBy { it.id.path }
                        .forEach { arg2.accept { it.get() } }
                }
                .build()
        }

        BLOCKS.register(bus)
        ITEMS.register(bus)
        ModPaintingVariants.register(bus)
        CREATIVE_MODE_TABS.register(bus)
    }
}