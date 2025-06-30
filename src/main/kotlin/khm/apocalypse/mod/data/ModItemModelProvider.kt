package khm.apocalypse.mod.data

import khm.apocalypse.mod.ForgeMod
import khm.apocalypse.mod.ModItems
import net.minecraft.data.PackOutput
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.data.ExistingFileHelper

class ModItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, ForgeMod.MOD_ID, existingFileHelper) {

    override fun registerModels() {
        // All items have the same model as blocks

        basicItem(ModItems.KHM_PAINTING.get())
    }
}