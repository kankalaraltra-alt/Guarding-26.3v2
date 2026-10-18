package com.teamabode.guarding.core.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Refunds the banner when a decorated shield is upgraded in the smithing table.
 */
@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin {

    @Inject(method = "onTake", at = @At("HEAD"), require = 0)
    private void guarding$onTake(Player player, ItemStack stack, CallbackInfo ci) {
        if (!(player instanceof ServerPlayer) || !(player.level() instanceof ServerLevel server)) {
            return;
        }
        // Slot 1 of the smithing menu is the base item.
        ItemStack baseStack = ((AbstractContainerMenu) (Object) this).getSlot(SmithingMenu.BASE_SLOT).getItem();

        DyeColor color = baseStack.get(DataComponents.BASE_COLOR);
        if (color == null) {
            return;
        }
        ItemStack bannerStack = new ItemStack(guarding$banner(color));

        if (baseStack.has(DataComponents.BANNER_PATTERNS)) {
            bannerStack.set(DataComponents.BANNER_PATTERNS, baseStack.get(DataComponents.BANNER_PATTERNS));
        }
        if (!player.getInventory().add(bannerStack)) {
            player.spawnAtLocation(server, bannerStack);
        }
    }

    @Unique
    private static Item guarding$banner(DyeColor color) {
        return BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath("minecraft", color.getSerializedName() + "_banner"));
    }
}
