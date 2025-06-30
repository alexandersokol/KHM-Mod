package khm.apocalypse.mod.data

import khm.apocalypse.mod.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import java.util.function.Consumer

class ModRecipeProvider(output: PackOutput) : RecipeProvider(output) {

    override fun buildRecipes(consumer: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.KHM_PAINTING.get(), 1)
            .define('#', ItemTags.PLANKS)
            .define('S', Items.STICK)
            .define('G', Items.PAPER)
            .pattern("S#S")
            .pattern("#G#")
            .pattern("S#S")
            .unlockedBy("has_paper", has(Items.PAPER))
            .save(consumer)
    }
}