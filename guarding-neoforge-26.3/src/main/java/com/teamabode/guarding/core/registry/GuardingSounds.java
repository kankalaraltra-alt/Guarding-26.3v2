package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GuardingSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, Guarding.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_SHIELD_PARRY = register("item.shield.parry");
    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_NETHERITE_SHIELD_BLOCK = register("item.netherite_shield.block");
    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_NETHERITE_SHIELD_BREAK = register("item.netherite_shield.break");
    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_NETHERITE_SHIELD_EQUIP = register("item.netherite_shield.equip");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(Guarding.id(name)));
    }
}
