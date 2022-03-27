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
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.items.GemArmorItem;
import org.infernalstudios.miningmaster.items.GemAxeItem;
import org.infernalstudios.miningmaster.items.GemBowItem;
import org.infernalstudios.miningmaster.items.GemHoeItem;
import org.infernalstudios.miningmaster.items.GemItem;
import org.infernalstudios.miningmaster.items.GemPickaxeItem;
import org.infernalstudios.miningmaster.items.GemShovelItem;
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
    public static final RegistryObject<Item> HEART_RHODONITE = registerItem("heart_rhodonite", GemItem::new);
    public static final RegistryObject<Item> POWER_PYRITE = registerItem("power_pyrite", GemItem::new);
    public static final RegistryObject<Item> KINETIC_OPAL = registerItem("kinetic_opal", GemItem::new);
    public static final RegistryObject<Item> AIR_MALACHITE = registerItem("air_malachite", GemItem::new);

    // TOOLS
    public static final RegistryObject<GemSwordItem> FIRE_RUBY_SWORD = registerItem("fire_ruby_sword", () -> new GemSwordItem(MMItemTiers.SUPRA, Ingredient.fromItems(FIRE_RUBY.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.FIRE_ASPECT, 3)));
    public static final RegistryObject<GemSwordItem> ICE_SAPPHIRE_SWORD = registerItem("ice_sapphire_sword", () -> new GemSwordItem(MMItemTiers.SUPRA, Ingredient.fromItems(ICE_SAPPHIRE.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(MMEnchantments.FREEZING, 3)));
    public static final RegistryObject<GemSwordItem> SPIRIT_GARNET_SWORD = registerItem("spirit_garnet_sword", () -> new GemSwordItem(MMItemTiers.SUPRA, Ingredient.fromItems(SPIRIT_GARNET.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(MMEnchantments.LEECHING, 1)));
    public static final RegistryObject<GemSwordItem> POWER_PYRITE_SWORD = registerItem("power_pyrite_sword", () -> new GemSwordItem(MMItemTiers.SUPRA, Ingredient.fromItems(POWER_PYRITE.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.SHARPNESS, 5)));
    public static final RegistryObject<GemSwordItem> LUCKY_CITRINE_SWORD = registerItem("lucky_citrine_sword", () -> new GemSwordItem(MMItemTiers.SUPRA, Ingredient.fromItems(LUCKY_CITRINE.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.LOOTING, 3)));
    public static final RegistryObject<GemSwordItem> KINETIC_OPAL_SWORD = registerItem("kinetic_opal_sword", () -> new GemSwordItem(MMItemTiers.SUPRA, Ingredient.fromItems(KINETIC_OPAL.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.KNOCKBACK, 2), new Pair<>(MMEnchantments.SMELTING, 1)));
    public static final RegistryObject<GemSwordItem> ULTIMA_SWORD = registerItem("ultima_sword", () -> new GemSwordItem(MMItemTiers.ULTIMA, Ingredient.fromItems(FIRE_RUBY.get(), ICE_SAPPHIRE.get(), SPIRIT_GARNET.get(), LUCKY_CITRINE.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.FIRE_ASPECT, 3), new Pair<>(MMEnchantments.FREEZING, 3), new Pair<>(() -> Enchantments.LOOTING, 3), new Pair<>(MMEnchantments.LEECHING, 1)));

    public static final RegistryObject<GemAxeItem> SPIRIT_GARNET_AXE = registerItem("spirit_garnet_axe", () -> new GemAxeItem(MMItemTiers.SUPRA, Ingredient.fromItems(SPIRIT_GARNET.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> MMEnchantments.LEECHING.get(), 1)));
    public static final RegistryObject<GemAxeItem> POWER_PYRITE_AXE = registerItem("power_pyrite_axe", () -> new GemAxeItem(MMItemTiers.SUPRA, Ingredient.fromItems(POWER_PYRITE.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.SHARPNESS, 5)));
    public static final RegistryObject<GemAxeItem> HASTE_PERIDOT_AXE = registerItem("haste_peridot_axe", () -> new GemAxeItem(MMItemTiers.SUPRA, Ingredient.fromItems(HASTE_PERIDOT.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.EFFICIENCY, 5)));
    public static final RegistryObject<GemAxeItem> KINETIC_OPAL_AXE = registerItem("kinetic_opal_axe", () -> new GemAxeItem(MMItemTiers.SUPRA, Ingredient.fromItems(KINETIC_OPAL.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.KNOCKBACK, 3), new Pair<>(MMEnchantments.SMELTING, 1)));
    public static final RegistryObject<GemAxeItem> ULTIMA_AXE = registerItem("ultima_axe", () -> new GemAxeItem(MMItemTiers.ULTIMA, Ingredient.fromItems(SPIRIT_GARNET.get(), POWER_PYRITE.get(), KINETIC_OPAL.get(), HASTE_PERIDOT.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.SHARPNESS, 5), new Pair<>(() -> MMEnchantments.LEECHING.get(), 1), new Pair<>(() -> Enchantments.EFFICIENCY, 5), new Pair<>(() -> Enchantments.KNOCKBACK, 3)));

    public static final RegistryObject<GemPickaxeItem> POWER_PYRITE_PICKAXE = registerItem("power_pyrite_pickaxe", () -> new GemPickaxeItem(MMItemTiers.SUPRA, Ingredient.fromItems(POWER_PYRITE.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(MMEnchantments.STONEBREAKER, 1), new Pair<>(() -> Enchantments.SHARPNESS, 1)));
    public static final RegistryObject<GemPickaxeItem> LUCKY_CITRINE_PICKAXE = registerItem("lucky_citrine_pickaxe", () -> new GemPickaxeItem(MMItemTiers.SUPRA, Ingredient.fromItems(LUCKY_CITRINE.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.FORTUNE, 3)));
    public static final RegistryObject<GemPickaxeItem> HASTE_PERIDOT_PICKAXE = registerItem("haste_peridot_pickaxe", () -> new GemPickaxeItem(MMItemTiers.SUPRA, Ingredient.fromItems(LUCKY_CITRINE.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.EFFICIENCY, 5)));
    public static final RegistryObject<GemPickaxeItem> KINETIC_OPAL_PICKAXE = registerItem("kinetic_opal_pickaxe", () -> new GemPickaxeItem(MMItemTiers.SUPRA, Ingredient.fromItems(KINETIC_OPAL.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.KNOCKBACK, 1), new Pair<>(MMEnchantments.SMELTING, 1)));
    public static final RegistryObject<GemPickaxeItem> ULTIMA_PICKAXE = registerItem("ultima_pickaxe", () -> new GemPickaxeItem(MMItemTiers.ULTIMA, Ingredient.fromItems(KINETIC_OPAL.get(), LUCKY_CITRINE.get(), HASTE_PERIDOT.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.EFFICIENCY, 4), new Pair<>(() -> Enchantments.FORTUNE, 3), new Pair<>(MMEnchantments.SMELTING, 1)));

    public static final RegistryObject<GemShovelItem> HASTE_PERIDOT_SHOVEL = registerItem("haste_peridot_shovel", () -> new GemShovelItem(MMItemTiers.SUPRA, Ingredient.fromItems(HASTE_PERIDOT.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.EFFICIENCY, 5)));
    public static final RegistryObject<GemShovelItem> KINETIC_OPAL_SHOVEL = registerItem("kinetic_opal_shovel", () -> new GemShovelItem(MMItemTiers.SUPRA, Ingredient.fromItems(KINETIC_OPAL.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.KNOCKBACK, 1), new Pair<>(MMEnchantments.SMELTING, 1)));
    public static final RegistryObject<GemShovelItem> ULTIMA_SHOVEL = registerItem("ultima_shovel", () -> new GemShovelItem(MMItemTiers.ULTIMA, Ingredient.fromItems(KINETIC_OPAL.get(), LUCKY_CITRINE.get(), HASTE_PERIDOT.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.EFFICIENCY, 5), new Pair<>(() -> Enchantments.FORTUNE, 3), new Pair<>(() -> MMEnchantments.SMELTING.get(), 1)));

    public static final RegistryObject<GemHoeItem> HASTE_PERIDOT_HOE = registerItem("haste_peridot_hoe", () -> new GemHoeItem(MMItemTiers.SUPRA, Ingredient.fromItems(HASTE_PERIDOT.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.EFFICIENCY, 5)));
    public static final RegistryObject<GemHoeItem> ULTIMA_HOE = registerItem("ultima_hoe", () -> new GemHoeItem(MMItemTiers.ULTIMA, Ingredient.fromItems(LUCKY_CITRINE.get(), HASTE_PERIDOT.get()), 5, 3.0F, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.EFFICIENCY, 6), new Pair<>(() -> Enchantments.FORTUNE, 3)));

    public static final RegistryObject<GemBowItem> AIR_MALACHITE_BOW = registerItem("air_malachite_bow", () -> new GemBowItem(new Item.Properties().group(MiningMaster.TAB).maxStackSize(1), new Pair<>(MMEnchantments.FLOATATION, 5)));

    // ARMOR
    public static final RegistryObject<GemArmorItem> PARAGON_HELMET = registerItem("paragon_helmet", () -> new GemArmorItem(MMArmorMaterials.PARAGON, Ingredient.fromItems(FIRE_RUBY.get()), EquipmentSlotType.HEAD, new Item.Properties().group(MiningMaster.TAB), new Pair<>(() -> Enchantments.THORNS, 3), new Pair<>(() -> Enchantments.FIRE_PROTECTION, 3), new Pair<>(() -> Enchantments.BLAST_PROTECTION, 3)));
    public static final RegistryObject<GemArmorItem> PARAGON_CHESTPLATE = registerItem("paragon_chestplate", () -> new GemArmorItem(MMArmorMaterials.PARAGON, Ingredient.fromItems(FIRE_RUBY.get()), EquipmentSlotType.CHEST, new Item.Properties().group(MiningMaster.TAB), new Pair<>(MMEnchantments.GRACE, 5), new Pair<>(MMEnchantments.HEARTFELT, 4)));
    public static final RegistryObject<GemArmorItem> PARAGON_LEGGINGS = registerItem("paragon_leggings", () -> new GemArmorItem(MMArmorMaterials.PARAGON, Ingredient.fromItems(FIRE_RUBY.get()), EquipmentSlotType.LEGS, new Item.Properties().group(MiningMaster.TAB), new Pair<>(MMEnchantments.KNIGHT_JUMP, 4), new Pair<>(MMEnchantments.SNOWPIERCER, 1)));
    public static final RegistryObject<GemArmorItem> PARAGON_BOOTS = registerItem("paragon_boots", () -> new GemArmorItem(MMArmorMaterials.PARAGON, Ingredient.fromItems(FIRE_RUBY.get()), EquipmentSlotType.FEET, new Item.Properties().group(MiningMaster.TAB), new Pair<>(MMEnchantments.RUNNER, 3), new Pair<>(() -> Enchantments.FEATHER_FALLING, 3), new Pair<>(() -> Enchantments.FROST_WALKER, 2)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        MiningMaster.LOGGER.info("Mining Master: Items Registered!");
    }

    public static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<? extends T> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }
}
