package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.common.enchantment.TargetedShieldEnchantmentEffect;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class GuardingEnchantmentEffectComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> TYPES = DeferredRegister.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Guarding.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EnchantmentValueEffect>> SHIELD_KNOCKBACK =
            TYPES.register("shield_knockback", () -> DataComponentType.<EnchantmentValueEffect>builder()
                    .persistent(EnchantmentValueEffect.CODEC)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<TargetedShieldEnchantmentEffect<EnchantmentEntityEffect>>>> SHIELD_BLOCKED =
            TYPES.register("shield_blocked", () -> DataComponentType.<List<TargetedShieldEnchantmentEffect<EnchantmentEntityEffect>>>builder()
                    .persistent(TargetedShieldEnchantmentEffect.codec(EnchantmentEntityEffect.CODEC).listOf())
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<TargetedShieldEnchantmentEffect<EnchantmentEntityEffect>>>> SHIELD_PARRIED =
            TYPES.register("shield_parried", () -> DataComponentType.<List<TargetedShieldEnchantmentEffect<EnchantmentEntityEffect>>>builder()
                    .persistent(TargetedShieldEnchantmentEffect.parriedCodec(EnchantmentEntityEffect.CODEC).listOf())
                    .build());
}
