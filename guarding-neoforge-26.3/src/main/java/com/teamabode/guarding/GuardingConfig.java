package com.teamabode.guarding;

import net.neoforged.neoforge.common.ModConfigSpec;

public class GuardingConfig {
    public static final ModConfigSpec COMMON_SPEC;
    public static final ModConfigSpec STARTUP_SPEC;

    public static final ModConfigSpec.DoubleValue EXHAUSTION_COST;
    public static final ModConfigSpec.DoubleValue KNOCKBACK_STRENGTH;
    public static final ModConfigSpec.IntValue SHIELD_ENCHANTABILITY;
    public static final ModConfigSpec.IntValue NETHERITE_SHIELD_ENCHANTABILITY;

    static {
        ModConfigSpec.Builder common = new ModConfigSpec.Builder();
        common.push("parry");
        EXHAUSTION_COST = common.comment("Hunger exhaustion added to the player when they parry an attack.")
                .defineInRange("exhaustion_cost", 2.0d, 0.0d, 1000.0d);
        KNOCKBACK_STRENGTH = common.comment("Base knockback strength applied to attackers when parried.")
                .defineInRange("knockback_strength", 0.5d, 0.0d, 100.0d);
        common.pop();
        COMMON_SPEC = common.build();

        ModConfigSpec.Builder startup = new ModConfigSpec.Builder();
        startup.comment("Enchantability of shields. Requires a game restart.").push("enchantability");
        SHIELD_ENCHANTABILITY = startup.defineInRange("minecraft_shield", 9, 0, 255);
        NETHERITE_SHIELD_ENCHANTABILITY = startup.defineInRange("guarding_netherite_shield", 15, 0, 255);
        startup.pop();
        STARTUP_SPEC = startup.build();
    }

    public static float exhaustionCost() {
        try {
            return EXHAUSTION_COST.get().floatValue();
        } catch (IllegalStateException e) {
            return 2.0f;
        }
    }

    public static float knockbackStrength() {
        try {
            return KNOCKBACK_STRENGTH.get().floatValue();
        } catch (IllegalStateException e) {
            return 0.5f;
        }
    }

    public static int shieldEnchantability() {
        try {
            return SHIELD_ENCHANTABILITY.get();
        } catch (IllegalStateException e) {
            return 9;
        }
    }

    public static int netheriteShieldEnchantability() {
        try {
            return NETHERITE_SHIELD_ENCHANTABILITY.get();
        } catch (IllegalStateException e) {
            return 15;
        }
    }
}
