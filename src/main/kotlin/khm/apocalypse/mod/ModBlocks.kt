package khm.apocalypse.mod

import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DropExperienceBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ModBlocks {

    val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, ForgeMod.MOD_ID)

    val MOON_QUARTZ_ORE = register("moon_quartz_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_QUARTZ_ORE), UniformInt.of(2, 5))
    }
    val MOON_COAL_ORE = register("moon_coal_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.COAL_ORE), UniformInt.of(2, 5))
    }

    private fun register(name: String, supplier: () -> Block): RegistryObject<Block> {
        val block = BLOCKS.register(name, Supplier { supplier.invoke() })
        ModItems.BLOCK_ITEMS.register(name, Supplier { BlockItem(block.get(), Item.Properties()) })
        return block
    }
}