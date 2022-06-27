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

import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.world.features.config.MalachiteMeteoriteFeatureConfig;
import org.infernalstudios.miningmaster.world.features.config.NativeGemOreFeatureConfig;
import org.infernalstudios.miningmaster.world.features.config.NativeNetherGemOreFeatureConfig;
import org.infernalstudios.miningmaster.world.features.config.RandomGemOreFeatureConfig;
import org.infernalstudios.miningmaster.world.features.config.RandomNetherGemOreFeatureConfig;

import java.util.List;

public class MMConfiguredFeatures {

    public static final DeferredRegister<ConfiguredFeature<?,?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, MiningMaster.MOD_ID);

    public static final RuleTest STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    public static final RuleTest DEEPSLATE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_FIRE_RUBY_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.FIRE_RUBY_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_FIRE_RUBY_ORE.get().defaultBlockState()));
    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_ICE_SAPPHIRE_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.ICE_SAPPHIRE_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_ICE_SAPPHIRE_ORE.get().defaultBlockState()));
    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_SPIRIT_GARNET_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.SPIRIT_GARNET_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_SPIRIT_GARNET_ORE.get().defaultBlockState()));
    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_DIVE_AQUAMARINE_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.DIVE_AQUAMARINE_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_DIVE_AQUAMARINE_ORE.get().defaultBlockState()));
    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_HASTE_PERIDOT_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.HASTE_PERIDOT_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_HASTE_PERIDOT_ORE.get().defaultBlockState()));
    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_LUCKY_CITRINE_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.LUCKY_CITRINE_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_LUCKY_CITRINE_ORE.get().defaultBlockState()));

    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_GEM_RANDOM = CONFIGURED_FEATURES.register("ore_gem_random", () -> new ConfiguredFeature<>(MMFeatures.RANDOM_GEM_ORE_FEATURE.get(), new RandomGemOreFeatureConfig(RandomGemOreFeatureConfig.STONE_ORE_REPLACEABLES)));
    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_GEM_DEEPSLATE_RANDOM = CONFIGURED_FEATURES.register("ore_gem_deepslate_random", () -> new ConfiguredFeature<>(MMFeatures.RANDOM_GEM_ORE_DEEPSLATE_FEATURE.get(), new RandomGemOreFeatureConfig(RandomGemOreFeatureConfig.DEEPSLATE_ORE_REPLACEABLES)));
    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_NETHER_GEM_RANDOM = CONFIGURED_FEATURES.register("ore_nether_gem_random", () -> new ConfiguredFeature<>(MMFeatures.RANDOM_NETHER_GEM_ORE_FEATURE.get(), new RandomNetherGemOreFeatureConfig(RandomNetherGemOreFeatureConfig.NETHERRACK)));

    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_FIRE_RUBY_NATIVE = CONFIGURED_FEATURES.register("ore_fire_ruby_native", () -> new ConfiguredFeature<>(MMFeatures.NATIVE_GEM_ORE_FEATURE.get(), new NativeGemOreFeatureConfig(ORE_FIRE_RUBY_TARGET_LIST)));
    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_ICE_SAPPHIRE_NATIVE = CONFIGURED_FEATURES.register("ore_ice_sapphire_native", () -> new ConfiguredFeature<>(MMFeatures.NATIVE_GEM_ORE_FEATURE.get(), new NativeGemOreFeatureConfig(ORE_ICE_SAPPHIRE_TARGET_LIST)));
    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_SPIRIT_GARNET_NATIVE = CONFIGURED_FEATURES.register("ore_spirit_garnet_native", () -> new ConfiguredFeature<>(MMFeatures.NATIVE_GEM_ORE_FEATURE.get(), new NativeGemOreFeatureConfig(ORE_SPIRIT_GARNET_TARGET_LIST)));
    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_DIVE_AQUAMARINE_NATIVE = CONFIGURED_FEATURES.register("ore_dive_aquamarine_native", () -> new ConfiguredFeature<>(MMFeatures.NATIVE_GEM_ORE_FEATURE.get(), new NativeGemOreFeatureConfig(ORE_DIVE_AQUAMARINE_TARGET_LIST)));
    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_HASTE_PERIDOT_NATIVE = CONFIGURED_FEATURES.register("ore_haste_peridot_native", () -> new ConfiguredFeature<>(MMFeatures.NATIVE_GEM_ORE_FEATURE.get(), new NativeGemOreFeatureConfig(ORE_HASTE_PERIDOT_TARGET_LIST)));
    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_LUCKY_CITRINE_NATIVE = CONFIGURED_FEATURES.register("ore_lucky_citrine_native", () -> new ConfiguredFeature<>(MMFeatures.NATIVE_GEM_ORE_FEATURE.get(), new NativeGemOreFeatureConfig(ORE_LUCKY_CITRINE_TARGET_LIST)));

    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_POWER_PYRITE_NATIVE = CONFIGURED_FEATURES.register("ore_power_pyrite_native", () -> new ConfiguredFeature<>(MMFeatures.NATIVE_NETHER_GEM_ORE_FEATURE.get(), new NativeNetherGemOreFeatureConfig(NativeNetherGemOreFeatureConfig.NETHERRACK, MMBlocks.POWER_PYRITE_ORE.get().defaultBlockState())));
    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_HEART_RHODONITE_NATIVE = CONFIGURED_FEATURES.register("ore_heart_rhodonite_native", () -> new ConfiguredFeature<>(MMFeatures.NATIVE_NETHER_GEM_ORE_FEATURE.get(), new NativeNetherGemOreFeatureConfig(NativeNetherGemOreFeatureConfig.NETHERRACK, MMBlocks.HEART_RHODONITE_ORE.get().defaultBlockState())));
    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_KINETIC_OPAL_NATIVE = CONFIGURED_FEATURES.register("ore_kinetic_opal_native", () -> new ConfiguredFeature<>(MMFeatures.NATIVE_NETHER_GEM_ORE_FEATURE.get(), new NativeNetherGemOreFeatureConfig(NativeNetherGemOreFeatureConfig.NETHERRACK, MMBlocks.KINETIC_OPAL_ORE.get().defaultBlockState())));

    public static RegistryObject<ConfiguredFeature<?, ?>> MALACHITE_METEORITE = CONFIGURED_FEATURES.register("malachite_meteorite", () -> new ConfiguredFeature<>(MMFeatures.MALACHITE_METEORITE_FEATURE.get(), new MalachiteMeteoriteFeatureConfig(15, 24, 1)));

    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
        MiningMaster.LOGGER.info("Mining Master: Configured Features Registered!");
    }
}
