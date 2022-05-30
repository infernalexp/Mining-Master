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
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.gen.features.config.MalachiteMeteoriteFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.NativeGemOreFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.NativeNetherGemOreFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.RandomGemOreFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.RandomNetherGemOreFeatureConfig;

public class MMConfiguredFeatures {

    public static Holder<ConfiguredFeature<?, ?>> ORE_GEM_RANDOM = registerConfiguredFeature("ore_gem_random", MMFeatures.RANDOM_GEM_ORE_FEATURE, new RandomGemOreFeatureConfig(RandomGemOreFeatureConfig.BASE_STONE_OVERWORLD));
    public static Holder<ConfiguredFeature<?, ?>> ORE_NETHER_GEM_RANDOM = registerConfiguredFeature("ore_nether_gem_random", MMFeatures.RANDOM_NETHER_GEM_ORE_FEATURE, new RandomNetherGemOreFeatureConfig(RandomNetherGemOreFeatureConfig.NETHERRACK));

    public static Holder<ConfiguredFeature<?, ?>> ORE_FIRE_RUBY_NATIVE = registerConfiguredFeature("ore_fire_ruby_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(NativeGemOreFeatureConfig.BASE_STONE_OVERWORLD, MMBlocks.FIRE_RUBY_ORE.get().defaultBlockState()));
    public static Holder<ConfiguredFeature<?, ?>> ORE_ICE_SAPPHIRE_NATIVE = registerConfiguredFeature("ore_ice_sapphire_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(NativeGemOreFeatureConfig.BASE_STONE_OVERWORLD, MMBlocks.ICE_SAPPHIRE_ORE.get().defaultBlockState()));
    public static Holder<ConfiguredFeature<?, ?>> ORE_SPIRIT_GARNET_NATIVE = registerConfiguredFeature("ore_spirit_garnet_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(NativeGemOreFeatureConfig.BASE_STONE_OVERWORLD, MMBlocks.SPIRIT_GARNET_ORE.get().defaultBlockState()));
    public static Holder<ConfiguredFeature<?, ?>> ORE_DIVE_AQUAMARINE_NATIVE = registerConfiguredFeature("ore_dive_aquamarine_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(NativeGemOreFeatureConfig.BASE_STONE_OVERWORLD, MMBlocks.DIVE_AQUAMARINE_ORE.get().defaultBlockState()));
    public static Holder<ConfiguredFeature<?, ?>> ORE_HASTE_PERIDOT_NATIVE = registerConfiguredFeature("ore_haste_peridot_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(NativeGemOreFeatureConfig.BASE_STONE_OVERWORLD, MMBlocks.HASTE_PERIDOT_ORE.get().defaultBlockState()));
    public static Holder<ConfiguredFeature<?, ?>> ORE_LUCKY_CITRINE_NATIVE = registerConfiguredFeature("ore_lucky_citrine_native", MMFeatures.NATIVE_GEM_ORE_FEATURE, new NativeGemOreFeatureConfig(NativeGemOreFeatureConfig.BASE_STONE_OVERWORLD, MMBlocks.LUCKY_CITRINE_ORE.get().defaultBlockState()));

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
