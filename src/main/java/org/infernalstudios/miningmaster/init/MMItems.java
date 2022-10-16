/*
 * Copyright 2022 Infernal Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.infernalstudios.miningmaster.init;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.items.GemArmorItem;
import org.infernalstudios.miningmaster.items.GemAxeItem;
import org.infernalstudios.miningmaster.items.GemBowItem;
import org.infernalstudios.miningmaster.items.GemItem;
import org.infernalstudios.miningmaster.items.GemPickaxeItem;
import org.infernalstudios.miningmaster.items.GemSwordItem;

import java.util.function.Supplier;

public class MMItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MiningMaster.MOD_ID);

    // GEMS
    public static final RegistryObject<Item> FIRE_RUBY = registerItem("fire_ruby", GemItem::new);
    public static final RegistryObject<Item> ICE_SAPPHIRE = registerItem("ice_sapphire", GemItem::new);
    public static final RegistryObject<Item> SPIRIT_GARNET = registerItem("spirit_garnet", GemItem::new);
    public static final RegistryObject<Item> HASTE_PERIDOT = registerItem("haste_peridot", GemItem::new);
    public static final RegistryObject<Item> LUCKY_CITRINE = registerItem("lucky_citrine", GemItem::new);
    public static final RegistryObject<Item> DIVE_AQUAMARINE = registerItem("dive_aquamarine", GemItem::new);
    public static final RegistryObject<Item> DIVINE_BERYL = registerItem("divine_beryl", GemItem::new);
    public static final RegistryObject<Item> SPIDER_KUNZITE = registerItem("spider_kunzite", GemItem::new);
    public static final RegistryObject<Item> UNBREAKING_IOLITE = registerItem("unbreaking_iolite", GemItem::new);
    public static final RegistryObject<Item> HEART_RHODONITE = registerItem("heart_rhodonite", GemItem::new);
    public static final RegistryObject<Item> POWER_PYRITE = registerItem("power_pyrite", GemItem::new);
    public static final RegistryObject<Item> KINETIC_OPAL = registerItem("kinetic_opal", GemItem::new);
    public static final RegistryObject<Item> AIR_MALACHITE = registerItem("air_malachite", GemItem::new);

    // TOOLS
    public static final RegistryObject<GemSwordItem> FIRE_RUBY_SWORD = registerItem("fire_ruby_sword", () -> new GemSwordItem(MMItemTiers.SUPRA, Ingredient.of(FIRE_RUBY.get()), 3, -2.4F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(() -> Enchantments.FIRE_ASPECT, 3)));
    public static final RegistryObject<GemSwordItem> ICE_SAPPHIRE_SWORD = registerItem("ice_sapphire_sword", () -> new GemSwordItem(MMItemTiers.SUPRA, Ingredient.of(ICE_SAPPHIRE.get()), 3, -2.4F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(MMEnchantments.FREEZING, 3)));
    public static final RegistryObject<GemSwordItem> SPIRIT_GARNET_SWORD = registerItem("spirit_garnet_sword", () -> new GemSwordItem(MMItemTiers.SUPRA, Ingredient.of(SPIRIT_GARNET.get()), 3, -2.4F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(MMEnchantments.LEECHING, 2)));
    public static final RegistryObject<GemSwordItem> LUCKY_CITRINE_SWORD = registerItem("lucky_citrine_sword", () -> new GemSwordItem(MMItemTiers.SUPRA, Ingredient.of(LUCKY_CITRINE.get()), 3, -2.4F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(() -> Enchantments.MOB_LOOTING, 3)));
    public static final RegistryObject<GemSwordItem> POWER_PYRITE_SWORD = registerItem("power_pyrite_sword", () -> new GemSwordItem(MMItemTiers.SUPRA, Ingredient.of(POWER_PYRITE.get()), 3, -2.4F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(() -> Enchantments.SHARPNESS, 6)));
    public static final RegistryObject<GemSwordItem> ULTIMA_SWORD = registerItem("ultima_sword", () -> new GemSwordItem(MMItemTiers.ULTIMA, Ingredient.of(FIRE_RUBY.get(), ICE_SAPPHIRE.get(), SPIRIT_GARNET.get(), LUCKY_CITRINE.get()), 3, -2.4F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(() -> Enchantments.FIRE_ASPECT, 3), new Pair<>(MMEnchantments.FREEZING, 3), new Pair<>(() -> Enchantments.MOB_LOOTING, 3), new Pair<>(MMEnchantments.LEECHING, 1)));

    public static final RegistryObject<GemAxeItem> HASTE_PERIDOT_AXE = registerItem("haste_peridot_axe", () -> new GemAxeItem(MMItemTiers.SUPRA, Ingredient.of(HASTE_PERIDOT.get()), 5, -3.0F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(() -> Enchantments.BLOCK_EFFICIENCY, 6)));
    public static final RegistryObject<GemAxeItem> POWER_PYRITE_AXE = registerItem("power_pyrite_axe", () -> new GemAxeItem(MMItemTiers.SUPRA, Ingredient.of(POWER_PYRITE.get()), 5, -3.0F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(() -> Enchantments.SHARPNESS, 6)));
    public static final RegistryObject<GemAxeItem> KINETIC_OPAL_AXE = registerItem("kinetic_opal_axe", () -> new GemAxeItem(MMItemTiers.SUPRA, Ingredient.of(KINETIC_OPAL.get()), 5, -3.0F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(() -> Enchantments.KNOCKBACK, 3)));
    public static final RegistryObject<GemAxeItem> ULTIMA_AXE = registerItem("ultima_axe", () -> new GemAxeItem(MMItemTiers.ULTIMA, Ingredient.of(POWER_PYRITE.get(), KINETIC_OPAL.get(), HASTE_PERIDOT.get()), 5, -3.0F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(() -> Enchantments.SHARPNESS, 5), new Pair<>(() -> Enchantments.BLOCK_EFFICIENCY, 5), new Pair<>(() -> Enchantments.KNOCKBACK, 3)));

    public static final RegistryObject<GemPickaxeItem> HASTE_PERIDOT_PICKAXE = registerItem("haste_peridot_pickaxe", () -> new GemPickaxeItem(MMItemTiers.SUPRA, Ingredient.of(HASTE_PERIDOT.get()), 1, -2.8F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(() -> Enchantments.BLOCK_EFFICIENCY, 6)));
    public static final RegistryObject<GemPickaxeItem> LUCKY_CITRINE_PICKAXE = registerItem("lucky_citrine_pickaxe", () -> new GemPickaxeItem(MMItemTiers.SUPRA, Ingredient.of(LUCKY_CITRINE.get()), 1, -2.8F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(() -> Enchantments.BLOCK_FORTUNE, 3)));
    public static final RegistryObject<GemPickaxeItem> UNBREAKING_IOLITE_PICKAXE = registerItem("unbreaking_iolite_pickaxe", () -> new GemPickaxeItem(MMItemTiers.SUPRA, Ingredient.of(UNBREAKING_IOLITE.get()), 1, -2.8F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(() -> Enchantments.UNBREAKING, 4)));
    public static final RegistryObject<GemPickaxeItem> ULTIMA_PICKAXE = registerItem("ultima_pickaxe", () -> new GemPickaxeItem(MMItemTiers.ULTIMA, Ingredient.of(KINETIC_OPAL.get(), LUCKY_CITRINE.get(), HASTE_PERIDOT.get(), UNBREAKING_IOLITE.get()), 1, -2.8F, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(() -> Enchantments.BLOCK_EFFICIENCY, 6), new Pair<>(() -> Enchantments.BLOCK_FORTUNE, 3), new Pair<>(() -> Enchantments.UNBREAKING, 3), new Pair<>(MMEnchantments.SMELTING, 1)));

    public static final RegistryObject<GemBowItem> AIR_MALACHITE_BOW = registerItem("air_malachite_bow", () -> new GemBowItem(new Item.Properties().tab(MiningMaster.TAB).durability(753), new Pair<>(MMEnchantments.FLOATATION, 5)));

    // ARMOR
    public static final RegistryObject<GemArmorItem> PARAGON_HELMET = registerItem("paragon_helmet", () -> new GemArmorItem(MMArmorMaterials.PARAGON, Ingredient.of(FIRE_RUBY.get(), KINETIC_OPAL.get(), SPIRIT_GARNET.get()), EquipmentSlot.HEAD, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(() -> Enchantments.THORNS, 3), new Pair<>(() -> Enchantments.FIRE_PROTECTION, 3), new Pair<>(() -> Enchantments.BLAST_PROTECTION, 3)));
    public static final RegistryObject<GemArmorItem> PARAGON_CHESTPLATE = registerItem("paragon_chestplate", () -> new GemArmorItem(MMArmorMaterials.PARAGON, Ingredient.of(DIVE_AQUAMARINE.get(), HEART_RHODONITE.get()), EquipmentSlot.CHEST, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(MMEnchantments.GRACE, 5), new Pair<>(MMEnchantments.HEARTFELT, 4)));
    public static final RegistryObject<GemArmorItem> PARAGON_LEGGINGS = registerItem("paragon_leggings", () -> new GemArmorItem(MMArmorMaterials.PARAGON, Ingredient.of(AIR_MALACHITE.get(), ICE_SAPPHIRE.get()), EquipmentSlot.LEGS, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(MMEnchantments.KNIGHT_JUMP, 4), new Pair<>(MMEnchantments.SNOWPIERCER, 1)));
    public static final RegistryObject<GemArmorItem> PARAGON_BOOTS = registerItem("paragon_boots", () -> new GemArmorItem(MMArmorMaterials.PARAGON, Ingredient.of(KINETIC_OPAL.get(), ICE_SAPPHIRE.get(), AIR_MALACHITE.get()), EquipmentSlot.FEET, new Item.Properties().tab(MiningMaster.TAB), new Pair<>(MMEnchantments.RUNNER, 3), new Pair<>(() -> Enchantments.FALL_PROTECTION, 3), new Pair<>(() -> Enchantments.FROST_WALKER, 2)));

    // ICON
    public static final RegistryObject<Item> TAB_ITEM = registerItem("mm_tab_icon", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        MiningMaster.LOGGER.info("Mining Master: Items Registered!");
    }

    public static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<? extends T> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }
}
