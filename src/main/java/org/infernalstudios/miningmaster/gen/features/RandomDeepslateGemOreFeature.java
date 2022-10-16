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

package org.infernalstudios.miningmaster.gen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.infernalstudios.miningmaster.config.MiningMasterConfig;
import org.infernalstudios.miningmaster.gen.features.config.RandomGemOreFeatureConfig;
import org.infernalstudios.miningmaster.init.MMBlocks;

import java.util.ArrayList;
import java.util.Random;

public class RandomDeepslateGemOreFeature extends Feature<RandomGemOreFeatureConfig> {
    static ArrayList<BlockState> weightedOreStatesEnabled = new ArrayList<>(10);

    public RandomDeepslateGemOreFeature(Codec<RandomGemOreFeatureConfig> codec) {
        super(codec);
    }

    public static void calculateEnabledOres() {
        weightedOreStatesEnabled = new ArrayList<>(4);

        // Adds all enabled ores to the list, double copies for common to give them double chance of being chosen
        if (MiningMasterConfig.CONFIG.fireRubyEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_FIRE_RUBY_ORE.get().defaultBlockState());
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_FIRE_RUBY_ORE.get().defaultBlockState());
        }

        if (MiningMasterConfig.CONFIG.iceSapphireEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_ICE_SAPPHIRE_ORE.get().defaultBlockState());
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_ICE_SAPPHIRE_ORE.get().defaultBlockState());
        }

        if (MiningMasterConfig.CONFIG.spiritGarnetEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_SPIRIT_GARNET_ORE.get().defaultBlockState());
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_SPIRIT_GARNET_ORE.get().defaultBlockState());
        }

        if (MiningMasterConfig.CONFIG.hastePeridotEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_HASTE_PERIDOT_ORE.get().defaultBlockState());
        }

        if (MiningMasterConfig.CONFIG.luckyCitrineEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_LUCKY_CITRINE_ORE.get().defaultBlockState());
        }

        if (MiningMasterConfig.CONFIG.diveAquamarineEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_DIVE_AQUAMARINE_ORE.get().defaultBlockState());
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_DIVE_AQUAMARINE_ORE.get().defaultBlockState());
        }

        if (MiningMasterConfig.CONFIG.divineBerylEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_DIVINE_BERYL_ORE.get().defaultBlockState());
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_DIVINE_BERYL_ORE.get().defaultBlockState());
        }

        if (MiningMasterConfig.CONFIG.spiderKunziteEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_SPIDER_KUNZITE_ORE.get().defaultBlockState());
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_SPIDER_KUNZITE_ORE.get().defaultBlockState());
        }

        if (MiningMasterConfig.CONFIG.unbreakingIoliteEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.DEEPSLATE_UNBREAKING_IOLITE_ORE.get().defaultBlockState());
        }

    }

    @Override
    public boolean place(FeaturePlaceContext<RandomGemOreFeatureConfig> context) {
        Random rand = context.random();
        BlockPos pos = context.origin();
        WorldGenLevel level = context.level();
        RandomGemOreFeatureConfig config = context.config();

        if (weightedOreStatesEnabled.size() == 0) {
            return false;
        }

        BlockState blockState = weightedOreStatesEnabled.get(rand.nextInt(weightedOreStatesEnabled.size()));

        if (config.target.test(level.getBlockState(pos), rand)) {
            level.setBlock(pos, blockState, 2);
        }

        return true;
    }
}
