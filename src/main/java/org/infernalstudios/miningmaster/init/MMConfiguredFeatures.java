/*
 * Copyright 2021 Infernal Studios
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

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.gen.features.config.MalachiteMeteoriteFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.NativeGemOreFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.RandomGemOreFeatureConfig;

public class MMConfiguredFeatures {

    public static ConfiguredFeature<?, ?> ORE_GEM_RANDOM = registerConfiguredFeature("ore_gem_random", MMFeatures.RANDOM_GEM_ORE_FEATURE.withConfiguration(new RandomGemOreFeatureConfig(RandomGemOreFeatureConfig.BASE_STONE_OVERWORLD)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(12, 0, 48))).square().count(1));

    public static ConfiguredFeature<?, ?> ORE_FIRE_RUBY_NATIVE = registerConfiguredFeature("ore_fire_ruby_native", MMFeatures.NATIVE_GEM_ORE_FEATURE.withConfiguration(new NativeGemOreFeatureConfig(NativeGemOreFeatureConfig.BASE_STONE_OVERWORLD, MMBlocks.FIRE_RUBY_ORE.get().getDefaultState())).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(12, 0, 48))).square().count(2));
    public static ConfiguredFeature<?, ?> ORE_ICE_SAPPHIRE_NATIVE = registerConfiguredFeature("ore_ice_sapphire_native", MMFeatures.NATIVE_GEM_ORE_FEATURE.withConfiguration(new NativeGemOreFeatureConfig(NativeGemOreFeatureConfig.BASE_STONE_OVERWORLD, MMBlocks.ICE_SAPPHIRE_ORE.get().getDefaultState())).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(12, 0, 48))).square().count(2));
    public static ConfiguredFeature<?, ?> ORE_SPIRIT_GARNET_NATIVE = registerConfiguredFeature("ore_spirit_garnet_native", MMFeatures.NATIVE_GEM_ORE_FEATURE.withConfiguration(new NativeGemOreFeatureConfig(NativeGemOreFeatureConfig.BASE_STONE_OVERWORLD, MMBlocks.SPIRIT_GARNET_ORE.get().getDefaultState())).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(12, 0, 48))).square().count(2));
    public static ConfiguredFeature<?, ?> ORE_HASTE_PERIDOT_NATIVE = registerConfiguredFeature("ore_haste_peridot_native", MMFeatures.NATIVE_GEM_ORE_FEATURE.withConfiguration(new NativeGemOreFeatureConfig(NativeGemOreFeatureConfig.BASE_STONE_OVERWORLD, MMBlocks.HASTE_PERIDOT_ORE.get().getDefaultState())).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(12, 0, 48))).square().count(1));
    public static ConfiguredFeature<?, ?> ORE_LUCKY_CITRINE_NATIVE = registerConfiguredFeature("ore_lucky_citrine_native", MMFeatures.NATIVE_GEM_ORE_FEATURE.withConfiguration(new NativeGemOreFeatureConfig(NativeGemOreFeatureConfig.BASE_STONE_OVERWORLD, MMBlocks.LUCKY_CITRINE_ORE.get().getDefaultState())).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(12, 0, 48))).square().count(1));

    public static ConfiguredFeature<?, ?> MALACHITE_METEORITE = registerConfiguredFeature("malachite_meteorite", MMFeatures.MALACHITE_METEORITE_FEATURE.withConfiguration(new MalachiteMeteoriteFeatureConfig(15, 20, 5, 10, 1)).withPlacement(Placement.END_ISLAND.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));

    public static ConfiguredFeature<?, ?> registerConfiguredFeature(String registryName, ConfiguredFeature<?, ?> configuredFeature) {
        ResourceLocation resourceLocation = new ResourceLocation(MiningMaster.MOD_ID, registryName);

        if (WorldGenRegistries.CONFIGURED_FEATURE.keySet().contains(resourceLocation))
            throw new IllegalStateException("Configured Feature ID: \"" + resourceLocation.toString() + "\" is already in the registry!");

        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, resourceLocation, configuredFeature);
    }
}
