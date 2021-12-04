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
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import org.infernalstudios.miningmaster.config.MiningMasterConfig;
import org.infernalstudios.miningmaster.gen.features.config.RandomGemOreFeatureConfig;
import org.infernalstudios.miningmaster.init.MMBlocks;

import java.util.ArrayList;
import java.util.Random;

public class RandomGemOreFeature extends Feature<RandomGemOreFeatureConfig> {
    public RandomGemOreFeature(Codec<RandomGemOreFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, RandomGemOreFeatureConfig config) {
        ArrayList<BlockState> weightedOreStatesEnabled = new ArrayList<>();

        // Adds all enabled ores to the list, double copies for common to give them double chance of being chosen
        if (MiningMasterConfig.CONFIG.fireRubyEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.FIRE_RUBY_ORE.get().getDefaultState());
            weightedOreStatesEnabled.add(MMBlocks.FIRE_RUBY_ORE.get().getDefaultState());
        }

        if (MiningMasterConfig.CONFIG.iceSapphireEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.ICE_SAPPHIRE_ORE.get().getDefaultState());
            weightedOreStatesEnabled.add(MMBlocks.ICE_SAPPHIRE_ORE.get().getDefaultState());
        }

        if (MiningMasterConfig.CONFIG.spiritGarnetEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.SPIRIT_GARNET_ORE.get().getDefaultState());
            weightedOreStatesEnabled.add(MMBlocks.SPIRIT_GARNET_ORE.get().getDefaultState());
        }

        if (MiningMasterConfig.CONFIG.diveAquamarineEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.DIVE_AQUAMARINE_ORE.get().getDefaultState());
            weightedOreStatesEnabled.add(MMBlocks.DIVE_AQUAMARINE_ORE.get().getDefaultState());
        }

        if (MiningMasterConfig.CONFIG.hastePeridotEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.HASTE_PERIDOT_ORE.get().getDefaultState());
        }

        if (MiningMasterConfig.CONFIG.luckyCitrineEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.LUCKY_CITRINE_ORE.get().getDefaultState());
        }

        if (weightedOreStatesEnabled.size() == 0) {
            return false;
        }

        BlockState blockState = weightedOreStatesEnabled.get(rand.nextInt(weightedOreStatesEnabled.size()));

        if (config.target.test(reader.getBlockState(pos), rand)) {
            reader.setBlockState(pos, blockState, 2);
        }

        return true;
    }
}

