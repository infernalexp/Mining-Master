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

package org.infernalstudios.miningmaster.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class GemOreBlock extends Block {

    public GemOreBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getExpDrop(BlockState state, LevelReader reader, RandomSource rand,BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? Mth.nextInt(rand, 5, 10) : 0;
    }


}
