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

package org.infernalstudios.miningmaster;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.infernalstudios.miningmaster.init.MMFeatures;
import org.infernalstudios.miningmaster.init.MMRecipes;
import org.infernalstudios.miningmaster.recipes.ForgingRecipe;

@Mod.EventBusSubscriber(modid = MiningMaster.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MiningMasterEvents {

    // Register the custom ore features and recipe types
    @SubscribeEvent
    public static void registerFeaturesAndRecipeTypes(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.FEATURES, helper -> MMFeatures.features.forEach(helper::register));
        event.register(ForgeRegistries.Keys.RECIPE_TYPES, helper -> helper.register(ForgingRecipe.TYPE_ID, MMRecipes.FORGING_RECIPE_TYPE));
    }

/*
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onBiomeLoad(BiomeLoadingEvent event) {
        if (event.getCategory() == null) {
            return;
        } else if (event.getName() == null) {
            return;
        }

        ResourceLocation name = event.getName();
        ResourceKey<Biome> biome = ResourceKey.create(Registry.BIOME_REGISTRY, name);


        Biome.BiomeCategory biomeCategory = event.getCategory();

        // NATIVE ORES
        if (biomeCategory == Biome.BiomeCategory.DESERT) {
            if (MiningMasterConfig.CONFIG.fireRubyEnabled.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_FIRE_RUBY_NATIVE);
            }
        } else if (biomeCategory == Biome.BiomeCategory.ICY) {
            if (MiningMasterConfig.CONFIG.iceSapphireEnabled.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_ICE_SAPPHIRE_NATIVE);
            }
        } else if (biomeCategory == Biome.BiomeCategory.EXTREME_HILLS) {
            if (MiningMasterConfig.CONFIG.spiritGarnetEnabled.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_SPIRIT_GARNET_NATIVE);
            }
        } else if (biomeCategory == Biome.BiomeCategory.JUNGLE) {
            if (MiningMasterConfig.CONFIG.hastePeridotEnabled.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_HASTE_PERIDOT_NATIVE);
            }
        } else if (biomeCategory == Biome.BiomeCategory.MESA) {
            if (MiningMasterConfig.CONFIG.luckyCitrineEnabled.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_LUCKY_CITRINE_NATIVE);
            }
        } else if (biomeCategory == Biome.BiomeCategory.OCEAN) {
            if (MiningMasterConfig.CONFIG.diveAquamarineEnabled.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_DIVE_AQUAMARINE_NATIVE);
            }
        } else if (biome == Biomes.SOUL_SAND_VALLEY) {
            if (MiningMasterConfig.CONFIG.heartRhodoniteEnabled.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_HEART_RHODONITE_NATIVE);
            }
        } else if (biome == Biomes.NETHER_WASTES) {
            if (MiningMasterConfig.CONFIG.powerPyriteEnabled.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_POWER_PYRITE_NATIVE);
            }
        } else if (biome == Biomes.BASALT_DELTAS) {
            if (MiningMasterConfig.CONFIG.kineticOpalEnabled.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_KINETIC_OPAL_NATIVE);
            }
        } else if (biome == Biomes.LUSH_CAVES) {
            if (MiningMasterConfig.CONFIG.spiritGarnetEnabled.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_SPIRIT_GARNET_NATIVE);
            }
        } else if (biome == Biomes.DRIPSTONE_CAVES) {
            if (MiningMasterConfig.CONFIG.diveAquamarineEnabled.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_DIVE_AQUAMARINE_NATIVE);
            }
        }

        // OVERWORLD RANDOM ORES
        if (!(biomeCategory == Biome.BiomeCategory.NETHER || biomeCategory == Biome.BiomeCategory.THEEND)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_GEM_RANDOM);
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_GEM_DEEPSLATE_RANDOM);
        }

        // NETHER RANDOM ORES
        if (biomeCategory == Biome.BiomeCategory.NETHER) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_NETHER_GEM_RANDOM);
        }

        // MALACHITE METEORITES
        if (biomeCategory == Biome.BiomeCategory.THEEND) {
            if (MiningMasterConfig.CONFIG.airMalachiteEnabled.get()) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.MALACHITE_METEORITE);
            }
        }
    }
    */
}
