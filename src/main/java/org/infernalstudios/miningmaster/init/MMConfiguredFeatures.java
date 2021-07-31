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
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import org.infernalstudios.miningmaster.MiningMaster;

public class MMConfiguredFeatures {

    public static ConfiguredFeature<?, ?> ORE_FIRE_RUBY = registerConfiguredFeature("ore_fire_ruby", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, MMBlocks.FIRE_RUBY_ORE.get().getDefaultState(), 1)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(10, 10, 40))).square());
    public static ConfiguredFeature<?, ?> ORE_ICE_SAPPHIRE = registerConfiguredFeature("ore_ice_sapphire", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, MMBlocks.ICE_SAPPHIRE_ORE.get().getDefaultState(), 1)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(10, 10, 40))).square());
    public static ConfiguredFeature<?, ?> ORE_SPIRIT_AMETHYST = registerConfiguredFeature("ore_spirit_amethyst", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, MMBlocks.SPIRIT_AMETHYST_ORE.get().getDefaultState(), 1)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(10, 10, 40))).square());

    public static ConfiguredFeature<?, ?> ORE_HASTE_PERIDOT = registerConfiguredFeature("ore_haste_peridot", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, MMBlocks.HASTE_PERIDOT_ORE.get().getDefaultState(), 1)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(10, 10, 40))).square());
    public static ConfiguredFeature<?, ?> ORE_LUCKY_CITRINE = registerConfiguredFeature("ore_lucky_citrine", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, MMBlocks.LUCKY_CITRINE_ORE.get().getDefaultState(), 1)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(10, 10, 40))).square());

    public static ConfiguredFeature<?, ?> registerConfiguredFeature(String registryName, ConfiguredFeature<?, ?> configuredFeature) {
        ResourceLocation resourceLocation = new ResourceLocation(MiningMaster.MOD_ID, registryName);

        if (WorldGenRegistries.CONFIGURED_FEATURE.keySet().contains(resourceLocation))
            throw new IllegalStateException("Configured Feature ID: \"" + resourceLocation.toString() + "\" is already in the registry!");

        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, resourceLocation, configuredFeature);
    }
}
