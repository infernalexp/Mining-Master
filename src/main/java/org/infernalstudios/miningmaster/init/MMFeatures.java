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

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.world.features.MalachiteMeteoriteFeature;
import org.infernalstudios.miningmaster.world.features.NativeGemOreFeature;
import org.infernalstudios.miningmaster.world.features.NativeNetherGemOreFeature;
import org.infernalstudios.miningmaster.world.features.RandomDeepslateGemOreFeature;
import org.infernalstudios.miningmaster.world.features.RandomGemOreFeature;
import org.infernalstudios.miningmaster.world.features.RandomNetherGemOreFeature;
import org.infernalstudios.miningmaster.world.features.config.MalachiteMeteoriteFeatureConfig;
import org.infernalstudios.miningmaster.world.features.config.NativeGemOreFeatureConfig;
import org.infernalstudios.miningmaster.world.features.config.NativeNetherGemOreFeatureConfig;
import org.infernalstudios.miningmaster.world.features.config.RandomGemOreFeatureConfig;
import org.infernalstudios.miningmaster.world.features.config.RandomNetherGemOreFeatureConfig;

import java.util.HashMap;
import java.util.Map;

public class MMFeatures {
    public static Map<ResourceLocation, Feature<?>> features = new HashMap<>();

    public static final Feature<NativeGemOreFeatureConfig> NATIVE_GEM_ORE_FEATURE = registerFeature("native_gem_ore_feature", new NativeGemOreFeature(NativeGemOreFeatureConfig.CODEC));
    public static final Feature<RandomGemOreFeatureConfig> RANDOM_GEM_ORE_FEATURE = registerFeature("random_gem_ore_feature", new RandomGemOreFeature(RandomGemOreFeatureConfig.CODEC));
    public static final Feature<RandomGemOreFeatureConfig> RANDOM_GEM_ORE_DEEPSLATE_FEATURE = registerFeature("random_gem_ore_deepslate_feature", new RandomDeepslateGemOreFeature(RandomGemOreFeatureConfig.CODEC));
    public static final Feature<NativeNetherGemOreFeatureConfig> NATIVE_NETHER_GEM_ORE_FEATURE = registerFeature("native_nether_gem_ore_feature", new NativeNetherGemOreFeature(NativeNetherGemOreFeatureConfig.CODEC));
    public static final Feature<RandomNetherGemOreFeatureConfig> RANDOM_NETHER_GEM_ORE_FEATURE = registerFeature("random_nether_gem_ore_feature", new RandomNetherGemOreFeature(RandomNetherGemOreFeatureConfig.CODEC));

    public static final Feature<MalachiteMeteoriteFeatureConfig> MALACHITE_METEORITE_FEATURE = registerFeature("malachite_meteorite_feature", new MalachiteMeteoriteFeature(MalachiteMeteoriteFeatureConfig.CODEC));

    public static <C extends FeatureConfiguration, F extends Feature<C>> F registerFeature(String registryName, F feature) {
        ResourceLocation resourceLocation = new ResourceLocation(MiningMaster.MOD_ID, registryName);

        if (ForgeRegistries.FEATURES.getKeys().contains(resourceLocation))
            throw new IllegalStateException("Feature ID: \"" + resourceLocation.toString() + "\" is already in the registry!");

        features.put(resourceLocation, feature);

        return feature;
    }
}
