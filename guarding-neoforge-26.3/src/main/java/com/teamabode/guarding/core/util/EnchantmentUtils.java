package com.teamabode.guarding.core.util;

import com.teamabode.guarding.common.enchantment.EnchantmentConsumer;
import com.teamabode.guarding.common.enchantment.TargetedShieldEnchantmentEffect;
import com.teamabode.guarding.core.registry.GuardingEnchantmentEffectComponentTypes;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import org.apache.commons.lang3.mutable.MutableFloat;

public class EnchantmentUtils {

    public static float modifyParryKnockback(Player player, float baseStrength) {
        MutableFloat strength = new MutableFloat(baseStrength);
        runIterationOnItem(player.getUseItem(), (holder, level) -> {
            Enchantment enchantment = holder.value();
            enchantment.modifyUnfilteredValue(GuardingEnchantmentEffectComponentTypes.SHIELD_KNOCKBACK.get(), player.getRandom(), level, strength);
        });
        return strength.floatValue();
    }

    public static void runBlockedEffects(ServerLevel server, Player player, DamageSource source, boolean performedParry) {
        ItemStack stack = player.getUseItem();
        runIterationOnItem(stack, (holder, level) -> {
            Enchantment enchantment = holder.value();
            EnchantedItemInUse enchantedItem = new EnchantedItemInUse(stack, getSlotFromHand(player.getUsedItemHand()), player);

            for (var blockedCondition : enchantment.getEffects(GuardingEnchantmentEffectComponentTypes.SHIELD_BLOCKED.get())) {
                if (performedParry && blockedCondition.cancelOnParry()) {
                    continue;
                }
                tryEffect(blockedCondition, server, level, enchantedItem, player, source);
            }
        });
    }

    public static void runParriedEffects(ServerLevel server, Player player, DamageSource source) {
        ItemStack stack = player.getUseItem();
        runIterationOnItem(stack, (holder, level) -> {
            Enchantment enchantment = holder.value();
            EnchantedItemInUse enchantedItem = new EnchantedItemInUse(stack, getSlotFromHand(player.getUsedItemHand()), player);

            for (var targetedCondition : enchantment.getEffects(GuardingEnchantmentEffectComponentTypes.SHIELD_PARRIED.get())) {
                tryEffect(targetedCondition, server, level, enchantedItem, player, source);
            }
        });
    }

    public static EquipmentSlot getSlotFromHand(InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return EquipmentSlot.MAINHAND;
        }
        return EquipmentSlot.OFFHAND;
    }

    public static void tryEffect(TargetedShieldEnchantmentEffect<EnchantmentEntityEffect> condition, ServerLevel server, int level, EnchantedItemInUse enchantedItem, Player player, DamageSource source) {
        if (condition.matches(Enchantment.damageContext(server, level, player, source))) {
            Entity target = switch (condition.affected()) {
                case VICTIM -> player;
                case ATTACKER -> source.getEntity();
                case DAMAGING_ENTITY -> source.getDirectEntity();
            };
            if (target != null) {
                condition.effect().apply(server, level, enchantedItem, target, target.position());
            }
        }
    }

    public static void runIterationOnItem(ItemStack stack, EnchantmentConsumer consumer) {
        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            consumer.accept(entry.getKey(), entry.getIntValue());
        }
    }
}
