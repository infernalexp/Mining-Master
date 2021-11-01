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

package org.infernalstudios.miningmaster;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.infernalstudios.miningmaster.init.MMConfiguredFeatures;
import org.infernalstudios.miningmaster.init.MMFeatures;

@Mod.EventBusSubscriber(modid = MiningMaster.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MiningMasterEvents {

    // Register the custom ore feature
    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        MMFeatures.features.forEach(feature -> event.getRegistry().register(feature));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onBiomeLoad(BiomeLoadingEvent event) {
        if (event.getCategory() == null) {
            return;
        }

        Biome.Category biomeCategory = event.getCategory();

        if (biomeCategory == Biome.Category.DESERT) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.ORE_FIRE_RUBY_NATIVE);
        } else if (biomeCategory == Biome.Category.ICY) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.ORE_ICE_SAPPHIRE_NATIVE);
        } else if (biomeCategory == Biome.Category.EXTREME_HILLS) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.ORE_SPIRIT_GARNET_NATIVE);
        } else if (biomeCategory == Biome.Category.JUNGLE) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.ORE_HASTE_PERIDOT_NATIVE);
        } else if (biomeCategory == Biome.Category.MESA) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.ORE_LUCKY_CITRINE_NATIVE);
        }

        if (!(biomeCategory == Biome.Category.NETHER || biomeCategory == Biome.Category.THEEND)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.ORE_GEM_RANDOM);
        }

        if (biomeCategory == Biome.Category.THEEND) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.MALACHITE_METEORITE);
        }
    }
}
