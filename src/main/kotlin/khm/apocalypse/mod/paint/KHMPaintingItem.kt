package khm.apocalypse.mod.paint

import khm.apocalypse.mod.ModRegistry
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.decoration.Painting
import net.minecraft.world.entity.decoration.PaintingVariant
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.HangingEntityItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import java.util.*
import java.util.function.Consumer
import java.util.function.Function

class KHMPaintingItem(settings: Properties) : HangingEntityItem(EntityType.PAINTING, settings) {

    private val defaultVariant = ModPaintingVariants.PLANT
    private val variants = ModPaintingVariantTags.KHM_PAINTINGS

    private fun variantArea(variant: Holder<PaintingVariant>): Int {
        return variant.value().width * variant.value().height
    }

    fun create(level: Level, pos: BlockPos, direction: Direction): Optional<Painting> {
        val painting: Painting = object :
            Painting(level, pos, direction, BuiltInRegistries.PAINTING_VARIANT.wrapAsHolder(defaultVariant.get())) {
            override fun spawnAtLocation(item: ItemLike): ItemEntity? {
                return super.spawnAtLocation(ModRegistry.KHM_PAINTING.get())
            }

            override fun getPickResult(): ItemStack {
                return ItemStack(ModRegistry.KHM_PAINTING.get())
            }
        }
        val list: MutableList<Holder<PaintingVariant>> = ArrayList<Holder<PaintingVariant>>()
        BuiltInRegistries.PAINTING_VARIANT.getTagOrEmpty(variants)
            .forEach(Consumer { e: Holder<PaintingVariant> -> list.add(e) })
        if (!list.isEmpty()) {
            list.removeIf { holder: Holder<PaintingVariant> ->
                painting.variant = holder
                !painting.survives()
            }
            if (!list.isEmpty()) {
                val max = list.stream()
                    .mapToInt { variant: Holder<PaintingVariant> -> variantArea(variant) }.max()
                    .orElse(0)
                list.removeIf { holder: Holder<PaintingVariant> -> variantArea(holder) < max }
                return Util.getRandomSafe(list, level.getRandom())
                    .map(Function { holder: Holder<PaintingVariant> ->
                        painting.variant = holder
                        painting
                    })
            }
        }
        return Optional.empty<Painting>()
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val pos: BlockPos = context.clickedPos
        val direction: Direction = context.clickedFace
        val pos2 = pos.relative(direction)
        val player: Player? = context.player
        val stack: ItemStack = context.itemInHand
        val level: Level = context.level
        if (player != null && !mayPlace(player, direction, stack, pos2)) {
            return InteractionResult.FAIL
        }

        val optional = create(level, pos2, direction)
        if (optional.isEmpty) {
            return InteractionResult.CONSUME
        }
        val painting = optional.get()

        val tag = stack.tag
        if (tag != null) {
            EntityType.updateCustomEntityTag(level, player, painting, tag)
        }
        if (painting.survives()) {
            if (!level.isClientSide) {
                painting.playPlacementSound()
                level.gameEvent(player, GameEvent.ENTITY_PLACE, painting.blockPosition())
                level.addFreshEntity(painting)
            }
            stack.shrink(1)
            return InteractionResult.sidedSuccess(level.isClientSide)
        }
        return InteractionResult.CONSUME
    }
}