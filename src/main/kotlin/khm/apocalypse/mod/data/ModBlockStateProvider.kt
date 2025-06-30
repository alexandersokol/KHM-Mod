package khm.apocalypse.mod.data

import khm.apocalypse.mod.ForgeMod
import khm.apocalypse.mod.ModBlocks
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.client.model.generators.ModelFile
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.ForgeRegistries

class ModBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, ForgeMod.MOD_ID, exFileHelper) {

    override fun registerStatesAndModels() {
        val blocks = ModBlocks.BLOCKS.entries + ModBlocks.OTHER_BLOCKS.entries
        blocks.forEach { v ->
            when (v.id.path) {
                ModBlocks.MARS_ANCIENT_DEBRIS.id.path,
                ModBlocks.ELEVATOR.id.path,
                ModBlocks.MERCURY_BASALT_DIAMOND_ORE.id.path,
                ModBlocks.MERCURY_BLACKSTONE_DIAMOND_ORE.id.path,
                ModBlocks.GLACIO_DEEPSLATE_ANCIENT_DEBRIS.id.path -> {
                    basicCubeColumn(v.get())
                }

                else -> simpleBlockWithItem(v.get(), cubeAll(v.get()))
            }
        }
    }

    fun basicCubeColumn(block: Block) {
        basicBlock(
            block,
            models().cubeColumn(
                name(block),
                modLoc("block/" + name(block)),
                modLoc("block/" + name(block) + "_top")
            )
        )
    }

    fun basicBlock(block: Block, model: ModelFile?) {
        simpleBlockItem(block, models().getBuilder(name(block)))
        simpleBlock(block, model)
    }

    private fun name(block: Block): String? {
        return this.key(block)?.path
    }

    private fun key(block: Block): ResourceLocation? {
        return ForgeRegistries.BLOCKS.getKey(block)
    }
}