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
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.gen.features.MalachiteMeteoriteFeature;
import org.infernalstudios.miningmaster.gen.features.NativeGemOreFeature;
import org.infernalstudios.miningmaster.gen.features.NativeNetherGemOreFeature;
import org.infernalstudios.miningmaster.gen.features.RandomGemOreFeature;
import org.infernalstudios.miningmaster.gen.features.RandomNetherGemOreFeature;
import org.infernalstudios.miningmaster.gen.features.config.MalachiteMeteoriteFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.NativeGemOreFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.NativeNetherGemOreFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.RandomGemOreFeatureConfig;
import org.infernalstudios.miningmaster.gen.features.config.RandomNetherGemOreFeatureConfig;

import java.util.ArrayList;
import java.util.List;

public class MMFeatures {
    public static List<Feature<?>> features = new ArrayList<>();

    public static final Feature<NativeGemOreFeatureConfig> NATIVE_GEM_ORE_FEATURE = registerFeature("native_gem_ore_feature", new NativeGemOreFeature(NativeGemOreFeatureConfig.CODEC));
    public static final Feature<RandomGemOreFeatureConfig> RANDOM_GEM_ORE_FEATURE = registerFeature("random_gem_ore_feature", new RandomGemOreFeature(RandomGemOreFeatureConfig.CODEC));
    public static final Feature<NativeNetherGemOreFeatureConfig> NATIVE_NETHER_GEM_ORE_FEATURE = registerFeature("native_nether_gem_ore_feature", new NativeNetherGemOreFeature(NativeNetherGemOreFeatureConfig.CODEC));
    public static final Feature<RandomNetherGemOreFeatureConfig> RANDOM_NETHER_GEM_ORE_FEATURE = registerFeature("random_nether_gem_ore_feature", new RandomNetherGemOreFeature(RandomNetherGemOreFeatureConfig.CODEC));

    public static final Feature<MalachiteMeteoriteFeatureConfig> MALACHITE_METEORITE_FEATURE = registerFeature("malachite_meteorite_feature", new MalachiteMeteoriteFeature(MalachiteMeteoriteFeatureConfig.CODEC));

    public static <C extends IFeatureConfig, F extends Feature<C>> F registerFeature(String registryName, F feature) {
        ResourceLocation resourceLocation = new ResourceLocation(MiningMaster.MOD_ID, registryName);

        if (Registry.FEATURE.keySet().contains(resourceLocation))
            throw new IllegalStateException("Feature ID: \"" + resourceLocation.toString() + "\" is already in the registry!");

        feature.setRegistryName(resourceLocation);
        features.add(feature);

        return feature;
    }
}
