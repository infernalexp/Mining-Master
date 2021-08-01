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
import org.infernalstudios.miningmaster.gen.features.config.RandomGemOreFeatureConfig;
import org.infernalstudios.miningmaster.init.MMBlocks;

import java.util.Random;

public class RandomGemOreFeature extends Feature<RandomGemOreFeatureConfig> {
    public RandomGemOreFeature(Codec<RandomGemOreFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, RandomGemOreFeatureConfig config) {
        int choice = rand.nextInt(9);
        BlockState blockState;

        if (choice <= 2) {
            blockState = MMBlocks.FIRE_RUBY_ORE.get().getDefaultState();
        } else if (choice <= 4) {
            blockState = MMBlocks.ICE_SAPPHIRE_ORE.get().getDefaultState();
        } else if (choice <= 6) {
            blockState = MMBlocks.SPIRIT_GARNET_ORE.get().getDefaultState();
        } else if (choice == 7) {
            blockState = MMBlocks.HASTE_PERIDOT_ORE.get().getDefaultState();
        } else {
            blockState = MMBlocks.LUCKY_CITRINE_ORE.get().getDefaultState();
        }

        if (config.target.test(reader.getBlockState(pos), rand)) {
            reader.setBlockState(pos, blockState, 2);
        }

        return true;
    }
}

