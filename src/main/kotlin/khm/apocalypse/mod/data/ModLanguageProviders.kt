package khm.apocalypse.mod.data

import khm.apocalypse.mod.ForgeMod
import khm.apocalypse.mod.ModRegistry
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.data.LanguageProvider
import net.minecraftforge.registries.RegistryObject

object ModLanguageProviders {

    private const val MOON: String = "moon"
    private const val MARS: String = "mars"
    private const val MERCURY: String = "mercury"
    private const val VENUS: String = "venus"
    private const val GLACIO: String = "glacio"

    fun getLanguageProviders(packOutput: PackOutput): List<LanguageProvider?> {
        return SupportedLocales.entries.map { getLanguageProvider(packOutput, it) }
    }

    fun getLanguageProvider(packOutput: PackOutput, locale: SupportedLocales): LanguageProvider {
        val oreNames: Map<RegistryObject<Block>, List<String>> = buildMap {
            for (entry in ModRegistry.BLOCKS.entries) {
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


                    val oreName = when (blockRegObj.id.path) {
                        ModRegistry.MOON_QUARTZ_ORE.id.path -> "Олов'яна"
                        else -> null
                    }

                    oreName?.let {
                        lang.add(blockRegObj.get(), "$planet $oreName руда")
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
                add("itemGroup.khm_apocalypse_mod", ForgeMod.MOD_NAME)
                genLocale.invoke(this)
            }
        }
    }

    enum class SupportedLocales(val localeCode: String) {
        EN_US("en_us"),
        UK_UA("uk_ua")
    }
}