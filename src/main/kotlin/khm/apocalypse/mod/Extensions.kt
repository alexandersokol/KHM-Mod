@file:Suppress("DEPRECATION", "removal")

package khm.apocalypse.mod

import net.minecraft.resources.ResourceLocation


@Throws(AssertionError::class)
fun oresInGround(k: ResourceLocation): ResourceLocation {
    return if (k.path.startsWith("moon_")) {
        ResourceLocation("forge", "ores_in_ground/moon_stone")
    } else if (k.path.startsWith("mars_")) {
        ResourceLocation("forge", "ores_in_ground/mars_stone")
    } else if (k.path.startsWith("venus_")) {
        ResourceLocation("forge", "ores_in_ground/venus_stone")
    } else if (k.path.startsWith("mercury_")) {
        ResourceLocation("forge", "ores_in_ground/mercury_stone")
    } else if (k.path.startsWith("glacio_")) {
        ResourceLocation("forge", "ores_in_ground/glacio_stone")
    } else {
        throw AssertionError(String.format("Can't make ores_in_ground/ item tag for %s", k))
    }
}