package khm.apocalypse.mod.data

import khm.apocalypse.mod.ForgeMod
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(modid = ForgeMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object DataGenerator {

    @JvmStatic
    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent) {
        val generator = event.generator
        val packOutput = generator.packOutput
        val existingFileHelper = event.existingFileHelper
        val lookupProvider = event.lookupProvider

        val modBlockTagsProvider = generator.addProvider(
            event.includeServer(),
            ModBlockTagsProvider(packOutput, lookupProvider, existingFileHelper)
        )
        generator.addProvider(event.includeServer(), ModLootTableProvider(packOutput))
        generator.addProvider(
            event.includeServer(), ModItemTagsProvider(
                packOutput, lookupProvider,
                modBlockTagsProvider.contentsGetter(), existingFileHelper
            )
        )
        generator.addProvider(event.includeClient(), ModBlockStateProvider(packOutput, existingFileHelper))
        generator.addProvider(event.includeClient(), ModItemModelProvider(packOutput, existingFileHelper))
        ModLanguageProviders.getLanguageProviders(packOutput)
            .forEach { p -> generator.addProvider(event.includeClient(), p) }
    }

}