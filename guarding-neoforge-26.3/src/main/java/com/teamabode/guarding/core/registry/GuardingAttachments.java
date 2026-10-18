package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.UUID;
import java.util.function.Supplier;

public class GuardingAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Guarding.MOD_ID);

    /**
     * UUID of the player that parried a projectile. Replaces the old ProjectileMixin/ProjectileAccessor.
     * Not serialized: a parried arrow forgets its parrier if it is saved and reloaded.
     */
    public static final Supplier<AttachmentType<UUID>> PARRIER = ATTACHMENTS.register("parrier",
            () -> AttachmentType.builder(() -> new UUID(0L, 0L)).build());
}
