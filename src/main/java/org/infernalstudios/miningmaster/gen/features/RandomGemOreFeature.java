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

public class RandomGemOreFeature extends Feature<RandomGemOreFeatureConfig> {
    static ArrayList<BlockState> weightedOreStatesEnabled = new ArrayList<>(10);

    public RandomGemOreFeature(Codec<RandomGemOreFeatureConfig> codec) {
        super(codec);
    }

    public static void calculateEnabledOres() {
        weightedOreStatesEnabled = new ArrayList<>(4);

        // Adds all enabled ores to the list, double copies for common to give them double chance of being chosen
        if (MiningMasterConfig.CONFIG.fireRubyEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.FIRE_RUBY_ORE.get().defaultBlockState());
            weightedOreStatesEnabled.add(MMBlocks.FIRE_RUBY_ORE.get().defaultBlockState());
        }

        if (MiningMasterConfig.CONFIG.iceSapphireEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.ICE_SAPPHIRE_ORE.get().defaultBlockState());
            weightedOreStatesEnabled.add(MMBlocks.ICE_SAPPHIRE_ORE.get().defaultBlockState());
        }

        if (MiningMasterConfig.CONFIG.spiritGarnetEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.SPIRIT_GARNET_ORE.get().defaultBlockState());
            weightedOreStatesEnabled.add(MMBlocks.SPIRIT_GARNET_ORE.get().defaultBlockState());
        }

        if (MiningMasterConfig.CONFIG.diveAquamarineEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.DIVE_AQUAMARINE_ORE.get().defaultBlockState());
            weightedOreStatesEnabled.add(MMBlocks.DIVE_AQUAMARINE_ORE.get().defaultBlockState());
        }

        if (MiningMasterConfig.CONFIG.hastePeridotEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.HASTE_PERIDOT_ORE.get().defaultBlockState());
        }

        if (MiningMasterConfig.CONFIG.luckyCitrineEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.LUCKY_CITRINE_ORE.get().defaultBlockState());
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

