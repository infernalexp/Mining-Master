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
import org.infernalstudios.miningmaster.gen.features.config.RandomNetherGemOreFeatureConfig;
import org.infernalstudios.miningmaster.init.MMBlocks;

import java.util.ArrayList;
import java.util.Random;

public class RandomNetherGemOreFeature extends Feature<RandomNetherGemOreFeatureConfig> {
    public RandomNetherGemOreFeature(Codec<RandomNetherGemOreFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, RandomNetherGemOreFeatureConfig config) {
        ArrayList<BlockState> weightedOreStatesEnabled = new ArrayList<>();

        // Adds all enabled ores to the list, double copies for common to give them double chance of being chosen
        if (MiningMasterConfig.CONFIG.powerPyriteEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.POWER_PYRITE_ORE.get().getDefaultState());
            weightedOreStatesEnabled.add(MMBlocks.POWER_PYRITE_ORE.get().getDefaultState());
        }

        if (MiningMasterConfig.CONFIG.kineticOpalEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.KINETIC_OPAL_ORE.get().getDefaultState());
        }

        if (MiningMasterConfig.CONFIG.heartRhodoniteEnabled.get()) {
            weightedOreStatesEnabled.add(MMBlocks.HEART_RHODONITE_ORE.get().getDefaultState());
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

