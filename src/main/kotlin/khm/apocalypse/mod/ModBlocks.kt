package khm.apocalypse.mod

import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DropExperienceBlock
import net.minecraft.world.level.block.RedStoneOreBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ModBlocks {

    val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, ForgeMod.MOD_ID)
    val OTHER_BLOCKS: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, ForgeMod.MOD_ID)

    val ELEVATOR =
        OTHER_BLOCKS.register("elevator", Supplier { Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)) })

    val MOON_QUARTZ_ORE = register("moon_quartz_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_QUARTZ_ORE), UniformInt.of(2, 5))
    }
    val MOON_COAL_ORE = register("moon_coal_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.COAL_ORE), UniformInt.of(2, 5))
    }
    val MOON_LAPIS_ORE = register("moon_lapis_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE), UniformInt.of(4, 8))
    }
    val MOON_COPPER_ORE = register("moon_copper_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_ORE), UniformInt.of(1, 2))
    }
    val MARS_COAL_ORE = register("mars_coal_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.COAL_ORE), UniformInt.of(3, 6))
    }
    val MARS_GOLD_ORE = register("mars_gold_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE), UniformInt.of(1, 2))
    }
    val MARS_REDSTONE_ORE = register("mars_redstone_ore") {
        RedStoneOreBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_ORE))
    }
    val MARS_ANCIENT_DEBRIS = register("mars_ancient_debris") {
        Block(BlockBehaviour.Properties.copy(Blocks.ANCIENT_DEBRIS))
    }
    val MERCURY_REDSTONE_ORE = register("mercury_redstone_ore") {
        RedStoneOreBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_ORE))
    }
    val MERCURY_LAPIS_ORE = register("mercury_lapis_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE), UniformInt.of(4, 8))
    }
    val MERCURY_COPPER_ORE = register("mercury_copper_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_ORE), UniformInt.of(2, 4))
    }
    val MERCURY_DIAMOND_ORE = register("mercury_diamond_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE), UniformInt.of(4, 10))
    }
    val MERCURY_BASALT_DIAMOND_ORE = register("mercury_basalt_diamond_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE), UniformInt.of(4, 10))
    }
    val MERCURY_BLACKSTONE_DIAMOND_ORE = register("mercury_blackstone_diamond_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE), UniformInt.of(4, 10))
    }
    val VENUS_REDSTONE_ORE = register("venus_redstone_ore") {
        RedStoneOreBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_ORE))
    }
    val VENUS_QUARTZ_ORE = register("venus_quartz_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_QUARTZ_ORE), UniformInt.of(3, 7))
    }
    val GLACIO_GOLD_ORE = register("glacio_gold_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE), UniformInt.of(3, 6))
    }
    val GLACIO_DEEPSLATE_GOLD_ORE = register("glacio_deepslate_gold_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_GOLD_ORE), UniformInt.of(3, 8))
    }
    val GLACIO_REDSTONE_ORE = register("glacio_redstone_ore") {
        RedStoneOreBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_ORE))
    }
    val GLACIO_DEEPSLATE_REDSTONE_ORE = register("glacio_deepslate_redstone_ore") {
        RedStoneOreBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_REDSTONE_ORE))
    }
    val GLACIO_DIAMOND_ORE = register("glacio_diamond_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE), UniformInt.of(2, 6))
    }
    val GLACIO_DEEPSLATE_DIAMOND_ORE = register("glacio_deepslate_diamond_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE), UniformInt.of(2, 6))
    }
    val GLACIO_QUARTZ_ORE = register("glacio_quartz_ore") {
        DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_QUARTZ_ORE), UniformInt.of(2, 4))
    }
    val GLACIO_DEEPSLATE_ANCIENT_DEBRIS = register("glacio_deepslate_ancient_debris") {
        Block(BlockBehaviour.Properties.copy(Blocks.ANCIENT_DEBRIS))
    }

    private fun register(name: String, supplier: () -> Block): RegistryObject<Block> {
        val block = BLOCKS.register(name, Supplier { supplier.invoke() })
        ModItems.BLOCK_ITEMS.register(name, Supplier { BlockItem(block.get(), Item.Properties()) })
        return block
    }
}