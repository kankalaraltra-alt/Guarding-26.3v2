package com.teamabode.guarding;

import com.mojang.logging.LogUtils;
import com.teamabode.guarding.core.registry.GuardingAttachments;
import com.teamabode.guarding.core.registry.GuardingEnchantmentEffectComponentTypes;
import com.teamabode.guarding.core.registry.GuardingItems;
import com.teamabode.guarding.core.registry.GuardingParticles;
import com.teamabode.guarding.core.registry.GuardingSounds;
import com.teamabode.guarding.core.registry.GuardingStats;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(Guarding.MOD_ID)
public class Guarding {
    public static final String MOD_ID = "guarding";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Guarding(IEventBus modBus, ModContainer container) {
        // Configs must be registered before anything reads them.
        container.registerConfig(ModConfig.Type.STARTUP, GuardingConfig.COMMON_SPEC, "guarding-common.toml");
        container.registerConfig(ModConfig.Type.STARTUP, GuardingConfig.STARTUP_SPEC, "guarding-startup.toml");

        GuardingItems.ITEMS.register(modBus);
        GuardingSounds.SOUNDS.register(modBus);
        GuardingParticles.PARTICLES.register(modBus);
        GuardingEnchantmentEffectComponentTypes.TYPES.register(modBus);
        GuardingStats.STATS.register(modBus);
        GuardingAttachments.ATTACHMENTS.register(modBus);

        modBus.addListener(GuardingItems::onModifyDefaultComponents);
        modBus.addListener(GuardingItems::onBuildCreativeTab);
        modBus.addListener(GuardingStats::onCommonSetup);
    }

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
