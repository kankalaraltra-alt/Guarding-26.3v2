package com.teamabode.guarding.core.util;

import com.teamabode.guarding.GuardingConfig;
import com.teamabode.guarding.core.registry.GuardingAttachments;
import com.teamabode.guarding.core.registry.GuardingParticles;
import com.teamabode.guarding.core.registry.GuardingSounds;
import com.teamabode.guarding.core.registry.GuardingStats;
import com.teamabode.guarding.core.tag.GuardingDamageTypeTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class ShieldUtils {

    /** Called (server side) whenever a player's shield successfully blocks an attack. */
    public static void onBlocked(ServerLevel server, Player user, DamageSource source) {
        boolean performedParry = canPerformParry(user, source);

        if (performedParry) {
            parry(server, user, source);
        }
        user.awardStat(GuardingStats.ATTACKS_BLOCKED_BY_SHIELD.get(), 1);
        EnchantmentUtils.runBlockedEffects(server, user, source, performedParry);
    }

    public static void parry(ServerLevel server, Player player, DamageSource source) {
        if (source.getDirectEntity() instanceof Projectile projectile) {
            deflectProjectile(player, projectile);
            spawnParticle(server, projectile.getX(), projectile.getY(), projectile.getZ());
        }
        else if (source.getDirectEntity() instanceof LivingEntity attacker) {
            knockbackAttacker(player, attacker);
            spawnParticle(server, (player.getX() + attacker.getX()) / 2, attacker.getEyeY(), (player.getZ() + attacker.getZ()) / 2);
        }
        player.causeFoodExhaustion(GuardingConfig.exhaustionCost());
        server.playSound(null, player.blockPosition(), GuardingSounds.ITEM_SHIELD_PARRY.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        player.awardStat(GuardingStats.ATTACKS_PARRIED_BY_SHIELD.get(), 1);
        EnchantmentUtils.runParriedEffects(server, player, source);
    }

    public static void knockbackAttacker(Player player, LivingEntity attacker) {
        double strength = getKnockbackStrength(player) * (1.0d - attacker.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        if (strength <= 0.0d) {
            return;
        }
        double dx = player.getX() - attacker.getX();
        double dz = player.getZ() - attacker.getZ();
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length < 1.0E-4d) {
            dx = 0.0d;
            dz = 1.0d;
            length = 1.0d;
        }
        Vec3 motion = attacker.getDeltaMovement();
        attacker.setDeltaMovement(
                motion.x / 2.0d - dx / length * strength,
                attacker.onGround() ? Math.min(0.4d, motion.y / 2.0d + strength) : motion.y,
                motion.z / 2.0d - dz / length * strength
        );
        attacker.needsSync = true;
        attacker.syncVelocity = true;
    }

    public static void deflectProjectile(Player player, Projectile projectile) {
        projectile.setData(GuardingAttachments.PARRIER.get(), player.getUUID());
        projectile.setDeltaMovement(projectile.getDeltaMovement().scale(5.0d));

        float reverseRot = 170.0F + projectile.getRandom().nextFloat() * 20.0F;
        projectile.setYRot(projectile.getYRot() + reverseRot);
        projectile.needsSync = true;
        projectile.syncVelocity = true;
    }

    public static float getKnockbackStrength(Player player) {
        float baseStrength = GuardingConfig.knockbackStrength();
        return EnchantmentUtils.modifyParryKnockback(player, baseStrength);
    }

    public static void spawnParticle(ServerLevel server, double x, double y, double z) {
        server.sendParticles(GuardingParticles.PARRY.get(), x, y, z, 1, 0.0d, 0.0d, 0.0d, 0.0d);
    }

    public static boolean canPerformParry(Player player, DamageSource source) {
        return player.getTicksUsingItem() <= 3 && !source.is(GuardingDamageTypeTags.NO_PARRY);
    }
}
