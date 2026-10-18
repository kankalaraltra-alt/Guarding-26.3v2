package com.teamabode.guarding.core.event;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.core.registry.GuardingAttachments;
import com.teamabode.guarding.core.util.ShieldUtils;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

import java.util.Optional;
import java.util.UUID;

@EventBusSubscriber(modid = Guarding.MOD_ID)
public class GuardingEvents {
    private static final Identifier PARRY_ADVANCEMENT = Guarding.id("story/parry_this_you_casual");
    private static final String PARRY_CRITERION = "killed_by_parried_arrow";

    /** Replaces sketch's ShieldEvents.BLOCKED. */
    @SubscribeEvent
    public static void onShieldBlock(LivingShieldBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(player.level() instanceof ServerLevel server)) return;
        if (!event.getBlocked()) return;

        ShieldUtils.onBlocked(server, player, event.getDamageSource());
    }

    /** Replaces the custom criterion trigger: the advancement uses minecraft:impossible and is awarded here. */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        Entity directEntity = event.getSource().getDirectEntity();

        if (directEntity instanceof Projectile projectile && projectile.level() instanceof ServerLevel server) {
            Optional<UUID> parrierId = projectile.getExistingData(GuardingAttachments.PARRIER.get());

            if (parrierId.isPresent() && server.getEntityInAnyDimension(parrierId.get()) instanceof ServerPlayer player) {
                AdvancementHolder advancement = server.getServer().getAdvancements().get(PARRY_ADVANCEMENT);
                if (advancement != null) {
                    player.getAdvancements().award(advancement, PARRY_CRITERION);
                }
            }
        }
    }
}
