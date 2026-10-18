package com.teamabode.guarding.core.registry;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.teamabode.guarding.Guarding;
import com.teamabode.guarding.GuardingConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.RegistryOps;
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
     * Since 1.21.5 shields are plain items driven by data components. The BLOCKS_ATTACKS component
     * is added in {@link #onModifyDefaultComponents}, because vanilla components are not bound yet
     * while items are being registered.
     */
    public static final DeferredItem<Item> NETHERITE_SHIELD = ITEMS.registerItem("netherite_shield", Item::new,
            properties -> properties
                    .durability(614)
                    .fireResistant()
                    .repairable(Items.NETHERITE_INGOT)
                    .enchantable(GuardingConfig.netheriteShieldEnchantability())
                    .component(DataComponents.BREAK_SOUND, GuardingSounds.ITEM_NETHERITE_SHIELD_BREAK)
                    .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.OFFHAND)
                            .setEquipSound(GuardingSounds.ITEM_NETHERITE_SHIELD_EQUIP)
                            .setSwappable(false)
                            .build()));

    /** Vanilla shield values, but with no use delay (replaces ShieldDelayMixin). */
    private static Optional<BlocksAttacks> blocksAttacks(HolderLookup.Provider registries, String blockSound) {
        String json = """
                {
                  "block_delay_seconds": 0.0,
                  "disable_cooldown_scale": 1.0,
                  "damage_reductions": [
                    { "horizontal_blocking_angle": 90.0, "base": 0.0, "factor": 1.0 }
                  ],
                  "item_damage": { "threshold": 3.0, "base": 1.0, "factor": 1.0 },
                  "bypassed_by": "#minecraft:bypasses_shield",
                  "block_sound": "%s",
                  "disabled_sound": "minecraft:item.shield.break"
                }
                """.formatted(blockSound);

        return BlocksAttacks.CODEC
                .parse(RegistryOps.create(JsonOps.INSTANCE, registries), JsonParser.parseString(json))
                .resultOrPartial(error -> Guarding.LOGGER.error("Could not build blocks_attacks component: {}", error));
    }

    public static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        event.modify(Items.SHIELD, (builder, registries, item) -> {
            builder.set(DataComponents.ENCHANTABLE, new Enchantable(GuardingConfig.shieldEnchantability()));
            blocksAttacks(registries, "minecraft:item.shield.block")
                    .ifPresent(component -> builder.set(DataComponents.BLOCKS_ATTACKS, component));
        });

        event.modify(NETHERITE_SHIELD.get(), (builder, registries, item) -> {
            blocksAttacks(registries, "guarding:item.netherite_shield.block")
                    .ifPresent(component -> builder.set(DataComponents.BLOCKS_ATTACKS, component));
        });
    }

    public static void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.COMBAT)) {
            event.insertAfter(new ItemStack(Items.SHIELD), new ItemStack(NETHERITE_SHIELD.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
