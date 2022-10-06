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

import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.config.MiningMasterConfig;

import java.util.List;

public class MMPlacedFeatures {

    public static final Holder<PlacedFeature> ORE_GEM_RANDOM = registerPlacedFeature("ore_gem_random", MMConfiguredFeatures.ORE_GEM_RANDOM, CountPlacement.of(MiningMasterConfig.CONFIG.randomGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(192)), BiomeFilter.biome());
    public static final Holder<PlacedFeature> ORE_GEM_DEEPSLATE_RANDOM = registerPlacedFeature("ore_gem_deepslate_random", MMConfiguredFeatures.ORE_GEM_DEEPSLATE_RANDOM, CountPlacement.of(MiningMasterConfig.CONFIG.randomGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0)), BiomeFilter.biome());
    public static final Holder<PlacedFeature> ORE_NETHER_GEM_RANDOM = registerPlacedFeature("ore_nether_gem_random", MMConfiguredFeatures.ORE_NETHER_GEM_RANDOM, CountPlacement.of(MiningMasterConfig.CONFIG.randomGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.top()), BiomeFilter.biome());

    public static final Holder<PlacedFeature> ORE_FIRE_RUBY_NATIVE = registerPlacedFeature("ore_fire_ruby_native", MMConfiguredFeatures.ORE_FIRE_RUBY_NATIVE, CountPlacement.of(MiningMasterConfig.CONFIG.rareGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(192)), BiomeFilter.biome());
    public static final Holder<PlacedFeature> ORE_ICE_SAPPHIRE_NATIVE = registerPlacedFeature("ore_ice_sapphire_native", MMConfiguredFeatures.ORE_ICE_SAPPHIRE_NATIVE, CountPlacement.of(MiningMasterConfig.CONFIG.commonGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(192)), BiomeFilter.biome());
    public static final Holder<PlacedFeature> ORE_SPIRIT_GARNET_NATIVE = registerPlacedFeature("ore_spirit_garnet_native", MMConfiguredFeatures.ORE_SPIRIT_GARNET_NATIVE, CountPlacement.of(MiningMasterConfig.CONFIG.commonGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(192)), BiomeFilter.biome());
    public static final Holder<PlacedFeature> ORE_HASTE_PERIDOT_NATIVE = registerPlacedFeature("ore_haste_peridot_native", MMConfiguredFeatures.ORE_HASTE_PERIDOT_NATIVE, CountPlacement.of(MiningMasterConfig.CONFIG.rareGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(192)), BiomeFilter.biome());
    public static final Holder<PlacedFeature> ORE_LUCKY_CITRINE_NATIVE = registerPlacedFeature("ore_lucky_citrine_native", MMConfiguredFeatures.ORE_LUCKY_CITRINE_NATIVE, CountPlacement.of(MiningMasterConfig.CONFIG.rareGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(192)), BiomeFilter.biome());
    public static final Holder<PlacedFeature> ORE_DIVE_AQUAMARINE_NATIVE = registerPlacedFeature("ore_dive_aquamarine_native", MMConfiguredFeatures.ORE_DIVE_AQUAMARINE_NATIVE, CountPlacement.of(MiningMasterConfig.CONFIG.commonGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(192)), BiomeFilter.biome());
    public static final Holder<PlacedFeature> ORE_DIVINE_BERYL_NATIVE = registerPlacedFeature("ore_divine_beryl_native", MMConfiguredFeatures.ORE_DIVINE_BERYL_NATIVE, CountPlacement.of(MiningMasterConfig.CONFIG.commonGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(192)), BiomeFilter.biome());
    public static final Holder<PlacedFeature> ORE_SPIDER_KUNZITE_NATIVE = registerPlacedFeature("ore_spider_kunzite_native", MMConfiguredFeatures.ORE_SPIDER_KUNZITE_NATIVE, CountPlacement.of(MiningMasterConfig.CONFIG.commonGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(192)), BiomeFilter.biome());
    public static final Holder<PlacedFeature> ORE_UNBREAKING_IOLITE_NATIVE = registerPlacedFeature("ore_unbreaking_iolite_native", MMConfiguredFeatures.ORE_UNBREAKING_IOLITE_NATIVE, CountPlacement.of(MiningMasterConfig.CONFIG.rareGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(192)), BiomeFilter.biome());

    public static final Holder<PlacedFeature> ORE_POWER_PYRITE_NATIVE = registerPlacedFeature("ore_power_pyrite_native", MMConfiguredFeatures.ORE_POWER_PYRITE_NATIVE, CountPlacement.of(MiningMasterConfig.CONFIG.rareGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.top()), BiomeFilter.biome());
    public static final Holder<PlacedFeature> ORE_HEART_RHODONITE_NATIVE = registerPlacedFeature("ore_heart_rhodonite_native", MMConfiguredFeatures.ORE_HEART_RHODONITE_NATIVE, CountPlacement.of(MiningMasterConfig.CONFIG.rareGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.top()), BiomeFilter.biome());
    public static final Holder<PlacedFeature> ORE_KINETIC_OPAL_NATIVE = registerPlacedFeature("ore_kinetic_opal_native", MMConfiguredFeatures.ORE_KINETIC_OPAL_NATIVE, CountPlacement.of(MiningMasterConfig.CONFIG.rareGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.top()), BiomeFilter.biome());

    public static final Holder<PlacedFeature> MALACHITE_METEORITE = registerPlacedFeature("malachite_meteorite", MMConfiguredFeatures.MALACHITE_METEORITE, RarityFilter.onAverageOnceEvery(28), PlacementUtils.countExtra(1, 0.25F, 1), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(55), VerticalAnchor.absolute(70)), BiomeFilter.biome());

    private static Holder<PlacedFeature> registerPlacedFeature(String name, Holder<ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... placementModifiers) {
        return registerPlacedFeature(name, configuredFeature, List.of(placementModifiers));
    }

    private static Holder<PlacedFeature> registerPlacedFeature(String name, Holder<ConfiguredFeature<?, ?>> configuredFeature, List<PlacementModifier> placementModifiers) {
        ResourceLocation resourceLocation = new ResourceLocation(MiningMaster.MOD_ID, name);

        if (BuiltinRegistries.PLACED_FEATURE.keySet().contains(resourceLocation))
            throw new IllegalStateException("Placed Feature ID: \"" + resourceLocation + "\" is already in the registry!");

        return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, resourceLocation, new PlacedFeature(configuredFeature, placementModifiers));
    }
}
