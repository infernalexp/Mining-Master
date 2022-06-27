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

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.config.MiningMasterConfig;

import java.util.List;

public class MMPlacedFeatures {

    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, MiningMaster.MOD_ID);

    public static final RegistryObject<PlacedFeature> ORE_GEM_RANDOM = PLACED_FEATURES.register("ore_gem_random", () -> new PlacedFeature(MMConfiguredFeatures.ORE_GEM_RANDOM.getHolder().get(), List.of(CountPlacement.of(MiningMasterConfig.CONFIG.randomGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(12), VerticalAnchor.aboveBottom(80)), BiomeFilter.biome())));
    public static final RegistryObject<PlacedFeature> ORE_GEM_DEEPSLATE_RANDOM = PLACED_FEATURES.register("ore_gem_deepslate_random", () -> new PlacedFeature(MMConfiguredFeatures.ORE_GEM_DEEPSLATE_RANDOM.getHolder().get(), List.of(CountPlacement.of(MiningMasterConfig.CONFIG.randomGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)), BiomeFilter.biome())));
    public static final RegistryObject<PlacedFeature> ORE_NETHER_GEM_RANDOM = PLACED_FEATURES.register("ore_nether_gem_random", () -> new PlacedFeature(MMConfiguredFeatures.ORE_NETHER_GEM_RANDOM.getHolder().get(), List.of(CountPlacement.of(MiningMasterConfig.CONFIG.randomGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.top()), BiomeFilter.biome())));

    public static final RegistryObject<PlacedFeature> ORE_FIRE_RUBY_NATIVE = PLACED_FEATURES.register("ore_fire_ruby_native", () -> new PlacedFeature(MMConfiguredFeatures.ORE_FIRE_RUBY_NATIVE.getHolder().get(), List.of(CountPlacement.of(MiningMasterConfig.CONFIG.commonGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)), BiomeFilter.biome())));
    public static final RegistryObject<PlacedFeature> ORE_ICE_SAPPHIRE_NATIVE = PLACED_FEATURES.register("ore_ice_sapphire_native", () -> new PlacedFeature(MMConfiguredFeatures.ORE_ICE_SAPPHIRE_NATIVE.getHolder().get(), List.of(CountPlacement.of(MiningMasterConfig.CONFIG.commonGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)), BiomeFilter.biome())));
    public static final RegistryObject<PlacedFeature> ORE_SPIRIT_GARNET_NATIVE = PLACED_FEATURES.register("ore_spirit_garnet_native", () -> new PlacedFeature(MMConfiguredFeatures.ORE_SPIRIT_GARNET_NATIVE.getHolder().get(), List.of(CountPlacement.of(MiningMasterConfig.CONFIG.commonGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)), BiomeFilter.biome())));
    public static final RegistryObject<PlacedFeature> ORE_DIVE_AQUAMARINE_NATIVE = PLACED_FEATURES.register("ore_dive_aquamarine_native", () -> new PlacedFeature(MMConfiguredFeatures.ORE_DIVE_AQUAMARINE_NATIVE.getHolder().get(), List.of(CountPlacement.of(MiningMasterConfig.CONFIG.commonGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)), BiomeFilter.biome())));
    public static final RegistryObject<PlacedFeature> ORE_HASTE_PERIDOT_NATIVE = PLACED_FEATURES.register("ore_haste_peridot_native", () -> new PlacedFeature(MMConfiguredFeatures.ORE_HASTE_PERIDOT_NATIVE.getHolder().get(), List.of(CountPlacement.of(MiningMasterConfig.CONFIG.rareGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)), BiomeFilter.biome())));
    public static final RegistryObject<PlacedFeature> ORE_LUCKY_CITRINE_NATIVE = PLACED_FEATURES.register("ore_lucky_citrine_native", () -> new PlacedFeature(MMConfiguredFeatures.ORE_LUCKY_CITRINE_NATIVE.getHolder().get(), List.of(CountPlacement.of(MiningMasterConfig.CONFIG.rareGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)), BiomeFilter.biome())));

    public static final RegistryObject<PlacedFeature> ORE_POWER_PYRITE_NATIVE = PLACED_FEATURES.register("ore_power_pyrite_native", () -> new PlacedFeature(MMConfiguredFeatures.ORE_POWER_PYRITE_NATIVE.getHolder().get(), List.of(CountPlacement.of(MiningMasterConfig.CONFIG.commonGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.top()), BiomeFilter.biome())));
    public static final RegistryObject<PlacedFeature> ORE_HEART_RHODONITE_NATIVE = PLACED_FEATURES.register("ore_heart_rhodonite_native", () -> new PlacedFeature(MMConfiguredFeatures.ORE_HEART_RHODONITE_NATIVE.getHolder().get(), List.of(CountPlacement.of(MiningMasterConfig.CONFIG.rareGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.top()), BiomeFilter.biome())));
    public static final RegistryObject<PlacedFeature> ORE_KINETIC_OPAL_NATIVE = PLACED_FEATURES.register("ore_kinetic_opal_native", () -> new PlacedFeature(MMConfiguredFeatures.ORE_KINETIC_OPAL_NATIVE.getHolder().get(), List.of(CountPlacement.of(MiningMasterConfig.CONFIG.rareGemsPerChunk.get()), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.top()), BiomeFilter.biome())));

    public static final RegistryObject<PlacedFeature> MALACHITE_METEORITE = PLACED_FEATURES.register("malachite_meteorite", () -> new PlacedFeature(MMConfiguredFeatures.MALACHITE_METEORITE.getHolder().get(), List.of(RarityFilter.onAverageOnceEvery(14), PlacementUtils.countExtra(1, 0.25F, 1), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(55), VerticalAnchor.absolute(70)), BiomeFilter.biome())));

    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
        MiningMaster.LOGGER.info("Mining Master: Placed Features Registered!");
    }
}
