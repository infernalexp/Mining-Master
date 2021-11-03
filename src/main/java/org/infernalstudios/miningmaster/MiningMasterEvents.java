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

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
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
        } else if (event.getName() == null) {
            return;
        }

        ResourceLocation name = event.getName();
        RegistryKey<Biome> biome = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, name);


        Biome.Category biomeCategory = event.getCategory();

        // NATIVE ORES
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
        } else if (biomeCategory == Biome.Category.OCEAN) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.ORE_DIVE_AQUAMARINE_NATIVE);
        } else if (biome == Biomes.SOUL_SAND_VALLEY) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.ORE_HEART_RHODONITE_NATIVE);
        } else if (biome == Biomes.NETHER_WASTES) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.ORE_POWER_PYRITE_NATIVE);
        } else if (biome == Biomes.BASALT_DELTAS) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.ORE_KINETIC_OPAL_NATIVE);
        }

        // OVERWORLD RANDOM ORES
        if (!(biomeCategory == Biome.Category.NETHER || biomeCategory == Biome.Category.THEEND)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.ORE_GEM_RANDOM);
        }

        // NETHER RANDOM ORES
        if (biomeCategory == Biome.Category.NETHER) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.ORE_NETHER_GEM_RANDOM);
        }

        // MALACHITE METEORITES
        if (biomeCategory == Biome.Category.THEEND) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MMConfiguredFeatures.MALACHITE_METEORITE);
        }
    }
}
