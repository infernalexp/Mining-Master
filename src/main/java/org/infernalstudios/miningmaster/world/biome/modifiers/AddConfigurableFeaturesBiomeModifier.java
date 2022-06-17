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

package org.infernalstudios.miningmaster.world.biome.modifiers;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.miningmaster.config.MiningMasterConfig;
import org.infernalstudios.miningmaster.init.MMPlacedFeatures;

import java.util.Optional;

public record AddConfigurableFeaturesBiomeModifier() implements BiomeModifier {
    public static final Codec<AddConfigurableFeaturesBiomeModifier> CODEC = Codec.unit(AddConfigurableFeaturesBiomeModifier::new);

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        Optional<ResourceKey<Biome>> biomeKey = biome.unwrapKey();

        if (phase != Phase.ADD || biomeKey.isEmpty()) return;

        this.addGen(builder, biomeKey.get());
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return CODEC;
    }

    private void addGen(ModifiableBiomeInfo.BiomeInfo.Builder builder, ResourceKey<Biome> biomeKey) {
        Optional<Holder<Biome>> optionalBiomeHolder = ForgeRegistries.BIOMES.getHolder(biomeKey);

        if (optionalBiomeHolder.isEmpty()) {
            return;
        }

        Holder<Biome> biome = optionalBiomeHolder.get();

        if (biomeKey.equals(Biomes.DESERT)) {
            if (MiningMasterConfig.CONFIG.fireRubyEnabled.get()) {
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_FIRE_RUBY_NATIVE);
            }
        } else if (biome.is(Tags.Biomes.IS_COLD_OVERWORLD)) {
            if (MiningMasterConfig.CONFIG.iceSapphireEnabled.get()) {
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_ICE_SAPPHIRE_NATIVE);
            }
        } else if (biomeKey.equals(Biomes.SUNFLOWER_PLAINS) || biomeKey.equals(Biomes.FLOWER_FOREST) || biomeKey.equals(Biomes.LUSH_CAVES)) {
            if (MiningMasterConfig.CONFIG.spiritGarnetEnabled.get()) {
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_SPIRIT_GARNET_NATIVE);
            }
        } else if (biome.is(BiomeTags.IS_JUNGLE)) {
            if (MiningMasterConfig.CONFIG.hastePeridotEnabled.get()) {
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_HASTE_PERIDOT_NATIVE);
            }
        } else if (biome.is(BiomeTags.IS_BADLANDS)) {
            if (MiningMasterConfig.CONFIG.luckyCitrineEnabled.get()) {
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_LUCKY_CITRINE_NATIVE);
            }
        } else if (biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_OCEAN) || biomeKey.equals(Biomes.DRIPSTONE_CAVES)) {
            if (MiningMasterConfig.CONFIG.diveAquamarineEnabled.get()) {
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_DIVE_AQUAMARINE_NATIVE);
            }
        } else if (biomeKey.equals(Biomes.SOUL_SAND_VALLEY)) {
            if (MiningMasterConfig.CONFIG.heartRhodoniteEnabled.get()) {
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_HEART_RHODONITE_NATIVE);
            }
        } else if (biomeKey.equals(Biomes.NETHER_WASTES)) {
            if (MiningMasterConfig.CONFIG.powerPyriteEnabled.get()) {
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_POWER_PYRITE_NATIVE);
            }
        } else if (biomeKey.equals(Biomes.BASALT_DELTAS)) {
            if (MiningMasterConfig.CONFIG.kineticOpalEnabled.get()) {
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_KINETIC_OPAL_NATIVE);
            }
        }

        // OVERWORLD RANDOM ORES
        if (biome.is(BiomeTags.IS_OVERWORLD)) {
            builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_GEM_RANDOM);
            builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_GEM_DEEPSLATE_RANDOM);
        }

        // NETHER RANDOM ORES
        if (biome.is(BiomeTags.IS_NETHER)) {
            builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.ORE_NETHER_GEM_RANDOM);
        }

        // MALACHITE METEORITES
        if (biome.is(BiomeTags.IS_END)) {
            if (MiningMasterConfig.CONFIG.airMalachiteEnabled.get()) {
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MMPlacedFeatures.MALACHITE_METEORITE);
            }
        }
    }
}
