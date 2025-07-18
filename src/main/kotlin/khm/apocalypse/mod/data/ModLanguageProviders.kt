package khm.apocalypse.mod.data

import khm.apocalypse.mod.ForgeMod
import khm.apocalypse.mod.ModBlocks
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.data.LanguageProvider
import net.minecraftforge.registries.RegistryObject

object ModLanguageProviders {

    fun getLanguageProviders(packOutput: PackOutput): List<LanguageProvider?> {
        return SupportedLocales.entries.map { getLanguageProvider(packOutput, it) }
    }

    fun getLanguageProvider(packOutput: PackOutput, locale: SupportedLocales): LanguageProvider {
        val oreNames: Map<RegistryObject<Block>, List<String>> = buildMap {
            for (entry in ModBlocks.BLOCKS.entries) {
                val pathParts = entry.id.path.split("_")
                put(entry, pathParts)
            }
        }

        val genLocale: (LanguageProvider) -> Unit = when (locale) {
            SupportedLocales.UK_UA -> { lang ->
                for ((blockRegObj, parts) in oreNames) {
                    val planet = when (parts[0]) {
                        "moon" -> "Місячна"
                        "mars" -> "Марсова"
                        "mercury" -> "Меркурієва"
                        "venus" -> "Венеріанська"
                        "glacio" -> "Льодовикова"
                        else -> parts[0]
                    }


                    val path = blockRegObj.id.path
                    val oreName = when {
                        "quartz" in path -> "кварцева руда"
                        "coal" in path -> "вугільна руда"
                        "lapis" in path -> "лазуритова руда"
                        "copper" in path -> "мідна руда"
                        "deepslate_gold" in path -> "глибосланцева золота руда"
                        "gold" in path -> "золота руда"
                        "basalt_diamond" in path -> "базальтова діамантова руда"
                        "blackstone_diamond" in path -> "темнокамінна діамантова руда"
                        "deepslate_diamond" in path -> "глибосланцева діамантова руда"
                        "diamond" in path -> "діамантова руда"
                        "ancient" in path -> "стародавня руда"
                        "deepslate_redstone" in path -> "глибосланцева редстоун руда"
                        "redstone" in path -> "редстоун руда"
                        else -> null
                    }

                    oreName?.let {
                        lang.add(blockRegObj.get(), "$planet $oreName")
                    }
                }
            }
            // add other locales if needed
            else -> { lang ->
                for ((block, parts) in oreNames) {
                    val name = parts.joinToString(" ") { it.replaceFirstChar { ch -> ch.titlecase() } }
                    lang.add(block.get(), name)
                }
            }
        }

        return object : LanguageProvider(packOutput, ForgeMod.MOD_ID, locale.localeCode) {
            override fun addTranslations() {
                // mod name is english only.
                add("itemGroup.khm", ForgeMod.MOD_NAME)
                add("item.khm.khm_painting", "KHM Картина")
                add("block.khm.elevator", "Елеватор")
                add("item.khm.elevator", "Елеватор")
                add("item.khm.we_tool", "WorldEdit Tool")
                add("item.khm.flan_tool", "Flan Tool")

                genLocale.invoke(this)
            }
        }
    }

    enum class SupportedLocales(val localeCode: String) {
        EN_US("en_us"),
        UK_UA("uk_ua")
    }
}