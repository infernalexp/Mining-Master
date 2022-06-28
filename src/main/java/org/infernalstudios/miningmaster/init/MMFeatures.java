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

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.world.features.MalachiteMeteoriteFeature;
import org.infernalstudios.miningmaster.world.features.GemOreFeature;
import org.infernalstudios.miningmaster.world.features.config.MalachiteMeteoriteFeatureConfig;
import org.infernalstudios.miningmaster.world.features.config.GemOreFeatureConfig;

public class MMFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MiningMaster.MOD_ID);

    public static final RegistryObject<Feature<GemOreFeatureConfig>> NATIVE_GEM_ORE_FEATURE = FEATURES.register("ore_gem_feature", () -> new GemOreFeature(GemOreFeatureConfig.CODEC));
    public static final RegistryObject<Feature<MalachiteMeteoriteFeatureConfig>> MALACHITE_METEORITE_FEATURE = FEATURES.register("malachite_meteorite_feature", () -> new MalachiteMeteoriteFeature(MalachiteMeteoriteFeatureConfig.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
        MiningMaster.LOGGER.info("Mining Master: Features Registered!");
    }

}
