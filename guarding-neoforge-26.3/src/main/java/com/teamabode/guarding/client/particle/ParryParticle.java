package com.teamabode.guarding.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;

public class ParryParticle extends SimpleAnimatedParticle {

    public ParryParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, sprites, 0.0f);
        this.xd = 0.0d;
        this.yd = 0.0d;
        this.zd = 0.0d;
        this.lifetime = 6;
        this.quadSize = 1.0f;

        float tint = this.random.nextFloat() * 0.6f + 0.4f;
        this.rCol = tint;
        this.gCol = tint;
        this.bCol = tint;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public int getLightCoords(float partialTick) {
        return 15728880;
    }
}
