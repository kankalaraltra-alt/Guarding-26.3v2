package com.teamabode.guarding.client;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.client.particle.ParryParticle;
import com.teamabode.guarding.core.registry.GuardingParticles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = Guarding.MOD_ID, value = Dist.CLIENT)
public class GuardingClient {

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(GuardingParticles.PARRY.get(), sprites ->
                (type, level, x, y, z, xSpeed, ySpeed, zSpeed, random) -> new ParryParticle(level, x, y, z, sprites));
    }
}
