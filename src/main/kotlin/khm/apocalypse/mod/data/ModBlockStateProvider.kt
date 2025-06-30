package khm.apocalypse.mod.data

import khm.apocalypse.mod.ForgeMod
import khm.apocalypse.mod.ModBlocks
import net.minecraft.data.PackOutput
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.common.data.ExistingFileHelper

class ModBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, ForgeMod.MOD_ID, exFileHelper) {

    override fun registerStatesAndModels() {
        ModBlocks.BLOCKS.getEntries().forEach { v -> simpleBlockWithItem(v.get(), cubeAll(v.get())) }
    }
}