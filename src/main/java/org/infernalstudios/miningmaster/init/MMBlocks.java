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

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.blocks.GemForgeBlock;
import org.infernalstudios.miningmaster.blocks.GemOreBlock;

import java.util.function.Supplier;

public class MMBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MiningMaster.MOD_ID);

    // ORES
    public static final RegistryObject<Block> FIRE_RUBY_ORE = registerBlockWithDefaultItem("fire_ruby_ore", () -> new GemOreBlock(getProperties(Blocks.DIAMOND_ORE)));
    public static final RegistryObject<Block> ICE_SAPPHIRE_ORE = registerBlockWithDefaultItem("ice_sapphire_ore", () -> new GemOreBlock(getProperties(Blocks.DIAMOND_ORE)));
    public static final RegistryObject<Block> SPIRIT_GARNET_ORE = registerBlockWithDefaultItem("spirit_garnet_ore", () -> new GemOreBlock(getProperties(Blocks.DIAMOND_ORE)));
    public static final RegistryObject<Block> HASTE_PERIDOT_ORE = registerBlockWithDefaultItem("haste_peridot_ore", () -> new GemOreBlock(getProperties(Blocks.DIAMOND_ORE)));
    public static final RegistryObject<Block> LUCKY_CITRINE_ORE = registerBlockWithDefaultItem("lucky_citrine_ore", () -> new GemOreBlock(getProperties(Blocks.DIAMOND_ORE)));
    public static final RegistryObject<Block> DIVE_AQUAMARINE_ORE = registerBlockWithDefaultItem("dive_aquamarine_ore", () -> new GemOreBlock(getProperties(Blocks.DIAMOND_ORE)));

    public static final RegistryObject<Block> DEEPSLATE_FIRE_RUBY_ORE = registerBlockWithDefaultItem("deepslate_fire_ruby_ore", () -> new GemOreBlock(getProperties(Blocks.DEEPSLATE_DIAMOND_ORE)));
    public static final RegistryObject<Block> DEEPSLATE_ICE_SAPPHIRE_ORE = registerBlockWithDefaultItem("deepslate_ice_sapphire_ore", () -> new GemOreBlock(getProperties(Blocks.DEEPSLATE_DIAMOND_ORE)));
    public static final RegistryObject<Block> DEEPSLATE_SPIRIT_GARNET_ORE = registerBlockWithDefaultItem("deepslate_spirit_garnet_ore", () -> new GemOreBlock(getProperties(Blocks.DEEPSLATE_DIAMOND_ORE)));
    public static final RegistryObject<Block> DEEPSLATE_HASTE_PERIDOT_ORE = registerBlockWithDefaultItem("deepslate_haste_peridot_ore", () -> new GemOreBlock(getProperties(Blocks.DEEPSLATE_DIAMOND_ORE)));
    public static final RegistryObject<Block> DEEPSLATE_LUCKY_CITRINE_ORE = registerBlockWithDefaultItem("deepslate_lucky_citrine_ore", () -> new GemOreBlock(getProperties(Blocks.DEEPSLATE_DIAMOND_ORE)));
    public static final RegistryObject<Block> DEEPSLATE_DIVE_AQUAMARINE_ORE = registerBlockWithDefaultItem("deepslate_dive_aquamarine_ore", () -> new GemOreBlock(getProperties(Blocks.DEEPSLATE_DIAMOND_ORE)));

    public static final RegistryObject<Block> HEART_RHODONITE_ORE = registerBlockWithDefaultItem("heart_rhodonite_ore", () -> new GemOreBlock(getProperties(Blocks.NETHER_GOLD_ORE)));
    public static final RegistryObject<Block> POWER_PYRITE_ORE = registerBlockWithDefaultItem("power_pyrite_ore", () -> new GemOreBlock(getProperties(Blocks.NETHER_GOLD_ORE)));
    public static final RegistryObject<Block> KINETIC_OPAL_ORE = registerBlockWithDefaultItem("kinetic_opal_ore", () -> new GemOreBlock(getProperties(Blocks.NETHER_GOLD_ORE)));
    public static final RegistryObject<Block> AIR_MALACHITE_ORE = registerBlockWithDefaultItem("air_malachite_ore", () -> new GemOreBlock(getProperties(Blocks.NETHER_GOLD_ORE)));

    // GEM BLOCKS
    public static final RegistryObject<Block> FIRE_RUBY_BLOCK = registerBlockWithDefaultItem("fire_ruby_block", () -> new Block(getProperties(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Block> ICE_SAPPHIRE_BLOCK = registerBlockWithDefaultItem("ice_sapphire_block", () -> new Block(getProperties(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Block> SPIRIT_GARNET_BLOCK = registerBlockWithDefaultItem("spirit_garnet_block", () -> new Block(getProperties(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Block> HASTE_PERIDOT_BLOCK = registerBlockWithDefaultItem("haste_peridot_block", () -> new Block(getProperties(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Block> LUCKY_CITRINE_BLOCK = registerBlockWithDefaultItem("lucky_citrine_block", () -> new Block(getProperties(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Block> DIVE_AQUAMARINE_BLOCK = registerBlockWithDefaultItem("dive_aquamarine_block", () -> new Block(getProperties(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Block> HEART_RHODONITE_BLOCK = registerBlockWithDefaultItem("heart_rhodonite_block", () -> new Block(getProperties(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Block> POWER_PYRITE_BLOCK = registerBlockWithDefaultItem("power_pyrite_block", () -> new Block(getProperties(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Block> KINETIC_OPAL_BLOCK = registerBlockWithDefaultItem("kinetic_opal_block", () -> new Block(getProperties(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Block> AIR_MALACHITE_BLOCK = registerBlockWithDefaultItem("air_malachite_block", () -> new Block(getProperties(Blocks.DIAMOND_BLOCK)));

    //MISC BLOCKS
    public static final RegistryObject<Block> MALACORE = registerBlockWithDefaultItem("malacore", () -> new Block(getProperties(Blocks.END_STONE)));
    public static final RegistryObject<Block> MALACRUST = registerBlockWithDefaultItem("malacrust", () -> new Block(getProperties(Blocks.DEEPSLATE).strength(22.5F)));
    public static final RegistryObject<Block> GEM_FORGE = registerBlockWithDefaultItem("gem_forge", () -> new GemForgeBlock(getProperties(Blocks.FURNACE)));

    public static BlockBehaviour.Properties getProperties(Material materialIn, float hardnessAndResistanceIn) {
        return getProperties(materialIn, hardnessAndResistanceIn, hardnessAndResistanceIn);
    }

    public static BlockBehaviour.Properties getProperties(Material materialIn, float hardnessIn, float resistanceIn) {
        return BlockBehaviour.Properties.of(materialIn).strength(hardnessIn, resistanceIn);
    }

    public static BlockBehaviour.Properties getProperties(Material materialIn) {
        return BlockBehaviour.Properties.of(materialIn).instabreak();
    }

    public static BlockBehaviour.Properties getProperties(Block block) {
        return BlockBehaviour.Properties.copy(block);
    }

    public static <T extends Block> RegistryObject<T> registerBlockWithDefaultItem(String name, Supplier<? extends T> blockSupplier) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
        MMItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(MiningMaster.TAB)));
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        MiningMaster.LOGGER.info("Mining Master: Blocks Registered!");
    }
}
