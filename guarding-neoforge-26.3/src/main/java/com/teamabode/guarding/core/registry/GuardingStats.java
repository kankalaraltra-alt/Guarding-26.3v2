package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GuardingStats {
    public static final DeferredRegister<Identifier> STATS = DeferredRegister.create(Registries.CUSTOM_STAT, Guarding.MOD_ID);

    public static final DeferredHolder<Identifier, Identifier> ATTACKS_BLOCKED_BY_SHIELD = register("attacks_blocked_by_shield");
    public static final DeferredHolder<Identifier, Identifier> ATTACKS_PARRIED_BY_SHIELD = register("attacks_parried_by_shield");

    private static DeferredHolder<Identifier, Identifier> register(String name) {
        return STATS.register(name, () -> Guarding.id(name));
    }

    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Stats.CUSTOM.get(ATTACKS_BLOCKED_BY_SHIELD.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(ATTACKS_PARRIED_BY_SHIELD.get(), StatFormatter.DEFAULT);
        });
    }
}
