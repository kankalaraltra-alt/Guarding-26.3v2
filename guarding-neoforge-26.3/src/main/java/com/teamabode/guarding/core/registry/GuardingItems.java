package com.teamabode.guarding.core.registry;

import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.GuardingConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class GuardingItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Guarding.MOD_ID);

    /**
     * Since 1.21.5 shields are plain items driven by data components (BLOCKS_ATTACKS, EQUIPPABLE, ...),
     * so NetheriteShieldItem and the ShieldItem mixins are gone. BLOCKS_ATTACKS is copied from the vanilla
     * shield (without the use delay) and given the netherite block sound.
     */
    public static final DeferredItem<Item> NETHERITE_SHIELD = ITEMS.registerItem("netherite_shield", Item::new,
            properties -> {
                properties = properties
                        .durability(614)
                        .fireResistant()
                        .repairable(Items.NETHERITE_INGOT)
                        .enchantable(GuardingConfig.netheriteShieldEnchantability())
                        .component(DataComponents.BREAK_SOUND, GuardingSounds.ITEM_NETHERITE_SHIELD_BREAK)
                        .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.OFFHAND)
                                .setEquipSound(GuardingSounds.ITEM_NETHERITE_SHIELD_EQUIP)
                                .setSwappable(false)
                                .build());

                BlocksAttacks vanilla = Items.SHIELD.components().get(DataComponents.BLOCKS_ATTACKS);
                if (vanilla != null) {
                    properties = properties.component(DataComponents.BLOCKS_ATTACKS,
                            withoutDelay(vanilla, Optional.of(GuardingSounds.ITEM_NETHERITE_SHIELD_BLOCK)));
                }
                return properties;
            });

    /** Copy of a vanilla BlocksAttacks with the use delay removed. Replaces ShieldDelayMixin. */
    private static BlocksAttacks withoutDelay(BlocksAttacks vanilla, Optional<Holder<SoundEvent>> blockSound) {
        return new BlocksAttacks(
                0.0f,
                vanilla.disableCooldownScale(),
                vanilla.damageReductions(),
                vanilla.itemDamage(),
                vanilla.bypassedBy(),
                blockSound,
                vanilla.disableSound()
        );
    }

    /** Vanilla shield: enchantable and no use delay. Replaces ShieldItemMixin / ShieldDelayMixin. */
    public static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        BlocksAttacks vanilla = Items.SHIELD.components().get(DataComponents.BLOCKS_ATTACKS);

        event.modify(Items.SHIELD, (builder, registries, item) -> {
            builder.set(DataComponents.ENCHANTABLE, new Enchantable(GuardingConfig.shieldEnchantability()));
            if (vanilla != null) {
                builder.set(DataComponents.BLOCKS_ATTACKS, withoutDelay(vanilla, vanilla.blockSound()));
            }
        });
    }

    public static void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.COMBAT)) {
            event.insertAfter(new ItemStack(Items.SHIELD), new ItemStack(NETHERITE_SHIELD.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
