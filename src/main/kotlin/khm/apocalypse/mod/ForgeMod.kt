package khm.apocalypse.mod

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Suppress("DEPRECATION", "removal")
@Mod(ForgeMod.MOD_ID)
class ForgeMod {

    init {
        val modEventBus = FMLJavaModLoadingContext.get().modEventBus

        ModConfig.load()

        MinecraftForge.EVENT_BUS.register(ModRegistry::class.java)
        ModRegistry.register(modEventBus)
        MinecraftForge.EVENT_BUS.addListener(CommandRegistry::onRegisterCommands)
    }

    companion object {
        const val MOD_ID = "khm"
        const val MOD_NAME = "KHM Mod"
        val LOGGER: Logger = LogManager.getLogger(MOD_ID)
    }
}