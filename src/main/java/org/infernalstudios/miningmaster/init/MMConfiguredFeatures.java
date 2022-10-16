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

import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.gen.features.config.MalachiteMeteoriteFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.NativeGemOreFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.NativeNetherGemOreFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.RandomGemOreFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.RandomNetherGemOreFeatureConfig;

import java.util.List;

public class MMConfiguredFeatures {
    public static final RuleTest STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    public static final RuleTest DEEPSLATE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_FIRE_RUBY_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.FIRE_RUBY_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_FIRE_RUBY_ORE.get().defaultBlockState()));
    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_ICE_SAPPHIRE_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.ICE_SAPPHIRE_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_ICE_SAPPHIRE_ORE.get().defaultBlockState()));
    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_SPIRIT_GARNET_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.SPIRIT_GARNET_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_SPIRIT_GARNET_ORE.get().defaultBlockState()));
    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_HASTE_PERIDOT_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.HASTE_PERIDOT_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_HASTE_PERIDOT_ORE.get().defaultBlockState()));
    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_LUCKY_CITRINE_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.LUCKY_CITRINE_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_LUCKY_CITRINE_ORE.get().defaultBlockState()));
    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_DIVE_AQUAMARINE_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.DIVE_AQUAMARINE_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_DIVE_AQUAMARINE_ORE.get().defaultBlockState()));
    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_DIVINE_BERYL_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.DIVINE_BERYL_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_DIVINE_BERYL_ORE.get().defaultBlockState()));
    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_SPIDER_KUNZITE_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.SPIDER_KUNZITE_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_SPIDER_KUNZITE_ORE.get().defaultBlockState()));
    public static final List<NativeGemOreFeatureConfig.TargetBlockState> ORE_UNBREAKING_IOLITE_TARGET_LIST = List.of(NativeGemOreFeatureConfig.target(STONE_ORE_REPLACEABLES, MMBlocks.UNBREAKING_IOLITE_ORE.get().defaultBlockState()), NativeGemOreFeatureConfig.target(DEEPSLATE_ORE_REPLACEABLES, MMBlocks.DEEPSLATE_UNBREAKING_IOLITE_ORE.get().defaultBlockState()));

    public static Holder<ConfiguredFeature<?, ?>> ORE_GEM_RANDOM = registerConfiguredFeature("ore_gem_random", MMFeatures.RANDOM_GEM_ORE_FEATURE, new RandomGemOreFeatureConfig(RandomGemOreFeatureConfig.STONE_ORE_REPLACEABLES));
    public static Holder<ConfiguredFeature<?, ?>> ORE_GEM_DEEPSLATE_RANDOM = registerConfiguredFeature("ore_gem_deepslate_random", MMFeatures.RANDOM_GEM_ORE_DEEPSLATE_FEATURE, new RandomGemOreFeatureConfig(RandomGemOreFeatureConfig.DEEPSLATE_ORE_REPLACEABLES));
    public static Holder<ConfiguredFeature<?, ?>> ORE_NETHER_GEM_RANDOM = registerConfiguredFeature("ore_nether_gem_random", MMFeatures.RANDOM_NETHER_GEM_ORE_FEATURE, new RandomNetherGemOreFeatureConfig(RandomNetherGemOreFeatureConfig.NETHERRACK));

    public static Holder<ConfiguredFeature<?, ?>> ORE_FIRE_RUBY_NATIVE = registerConfiguredFeature("ore_fire_ruby_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(ORE_FIRE_RUBY_TARGET_LIST));
    public static Holder<ConfiguredFeature<?, ?>> ORE_ICE_SAPPHIRE_NATIVE = registerConfiguredFeature("ore_ice_sapphire_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(ORE_ICE_SAPPHIRE_TARGET_LIST));
    public static Holder<ConfiguredFeature<?, ?>> ORE_SPIRIT_GARNET_NATIVE = registerConfiguredFeature("ore_spirit_garnet_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(ORE_SPIRIT_GARNET_TARGET_LIST));
    public static Holder<ConfiguredFeature<?, ?>> ORE_HASTE_PERIDOT_NATIVE = registerConfiguredFeature("ore_haste_peridot_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(ORE_HASTE_PERIDOT_TARGET_LIST));
    public static Holder<ConfiguredFeature<?, ?>> ORE_LUCKY_CITRINE_NATIVE = registerConfiguredFeature("ore_lucky_citrine_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(ORE_LUCKY_CITRINE_TARGET_LIST));
    public static Holder<ConfiguredFeature<?, ?>> ORE_DIVE_AQUAMARINE_NATIVE = registerConfiguredFeature("ore_dive_aquamarine_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(ORE_DIVE_AQUAMARINE_TARGET_LIST));
    public static Holder<ConfiguredFeature<?, ?>> ORE_DIVINE_BERYL_NATIVE = registerConfiguredFeature("ore_divine_beryl_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(ORE_DIVINE_BERYL_TARGET_LIST));
    public static Holder<ConfiguredFeature<?, ?>> ORE_SPIDER_KUNZITE_NATIVE = registerConfiguredFeature("ore_spider_kunzite_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(ORE_SPIDER_KUNZITE_TARGET_LIST));
    public static Holder<ConfiguredFeature<?, ?>> ORE_UNBREAKING_IOLITE_NATIVE = registerConfiguredFeature("ore_unbreaking_iolite_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(ORE_UNBREAKING_IOLITE_TARGET_LIST));

    public static Holder<ConfiguredFeature<?, ?>> ORE_POWER_PYRITE_NATIVE = registerConfiguredFeature("ore_power_pyrite_native", MMFeatures.NATIVE_NETHER_GEM_ORE_FEATURE, new NativeNetherGemOreFeatureConfig(NativeNetherGemOreFeatureConfig.NETHERRACK, MMBlocks.POWER_PYRITE_ORE.get().defaultBlockState()));
    public static Holder<ConfiguredFeature<?, ?>> ORE_HEART_RHODONITE_NATIVE = registerConfiguredFeature("ore_heart_rhodonite_native", MMFeatures.NATIVE_NETHER_GEM_ORE_FEATURE, new NativeNetherGemOreFeatureConfig(NativeNetherGemOreFeatureConfig.NETHERRACK, MMBlocks.HEART_RHODONITE_ORE.get().defaultBlockState()));
    public static Holder<ConfiguredFeature<?, ?>> ORE_KINETIC_OPAL_NATIVE = registerConfiguredFeature("ore_kinetic_opal_native", MMFeatures.NATIVE_NETHER_GEM_ORE_FEATURE, new NativeNetherGemOreFeatureConfig(NativeNetherGemOreFeatureConfig.NETHERRACK, MMBlocks.KINETIC_OPAL_ORE.get().defaultBlockState()));

    public static Holder<ConfiguredFeature<?, ?>> MALACHITE_METEORITE = registerConfiguredFeature("malachite_meteorite", MMFeatures.MALACHITE_METEORITE_FEATURE, new MalachiteMeteoriteFeatureConfig(15, 24, 1));

    private static Holder<ConfiguredFeature<?, ?>> registerConfiguredFeature(String name, Feature<NoneFeatureConfiguration> feature) {
        return registerConfiguredFeature(name, feature, FeatureConfiguration.NONE);
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<?, ?>> registerConfiguredFeature(String name, F feature, FC featureConfiguration) {
        ResourceLocation resourceLocation = new ResourceLocation(MiningMaster.MOD_ID, name);

        if (BuiltinRegistries.CONFIGURED_FEATURE.keySet().contains(resourceLocation))
            throw new IllegalStateException("Configured Feature ID: \"" + resourceLocation + "\" is already in the registry!");

        return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, resourceLocation, new ConfiguredFeature<>(feature, featureConfiguration));
    }
}
