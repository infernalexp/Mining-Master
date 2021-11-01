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
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import org.infernalstudios.miningmaster.gen.features.config.MalachiteMeteoriteFeatureConfig;
import org.infernalstudios.miningmaster.init.MMBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MalachiteMeteoriteFeature extends Feature<MalachiteMeteoriteFeatureConfig> {

    public MalachiteMeteoriteFeature(Codec<MalachiteMeteoriteFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, MalachiteMeteoriteFeatureConfig config) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(pos.getX(), 0, pos.getZ());
        IBlockReader blockView = generator.func_230348_a_(mutable.getX(), mutable.getZ());

        boolean flag = true;

        while (mutable.getY() <= world.getHeight()) {
            BlockState state = blockView.getBlockState(mutable);
            if (!state.isAir()) {
                flag = false;
                break;
            } else {
                mutable.move(Direction.UP);
            }
        }

        if (rand.nextInt(100) < 1 && flag) {
            float f = (float) (rand.nextInt(config.maxIslandRadius - config.minIslandRadius) + config.minIslandRadius);

            for (int i = 0; f > 0.5F; --i) {
                for (int j = MathHelper.floor(-f); j <= MathHelper.ceil(f); ++j) {
                    for (int k = MathHelper.floor(-f); k <= MathHelper.ceil(f); ++k) {
                        if ((float) (j * j + k * k) <= (f + 1.0F) * (f + 1.0F)) {
                            this.setBlockState(world, pos.add(j, i, k), Blocks.END_STONE.getDefaultState());
                        }
                    }
                }

                f = (float) ((double) f - ((double) rand.nextInt(2) + 0.5D));
            }

            int meteoriteRadius = rand.nextInt(config.maxMeteoriteRadius - config.minMeteoriteRadius) + config.minMeteoriteRadius;

            List<BlockPos> crustPosList = new ArrayList<>();

            for (int x = (int) -meteoriteRadius; x < meteoriteRadius; x++) {
                for (int y = (int) -meteoriteRadius; y < meteoriteRadius; y++) {
                    for (int z = (int) -meteoriteRadius; z < meteoriteRadius; z++) {
                        if ((x * x) + (y * y) + (z * z) <= (meteoriteRadius * meteoriteRadius)) {
                            crustPosList.add(new BlockPos(x, y, z));
                        }
                    }
                }
            }

            for (BlockPos point : crustPosList) {
                BlockPos pointPos = new BlockPos(pos.getX() + point.getX(), pos.getY() + point.getY(), pos.getZ() + point.getZ());
                world.setBlockState(pointPos, MMBlocks.MALACRUST.get().getDefaultState(), 2);
            }

            List<BlockPos> corePosList = new ArrayList<>();

            for (int x = (int) -(meteoriteRadius - 1); x < (meteoriteRadius - 1); x++) {
                for (int y = (int) -(meteoriteRadius - 1); y < (meteoriteRadius - 1); y++) {
                    for (int z = (int) -(meteoriteRadius - 1); z < (meteoriteRadius - 1); z++) {
                        if ((x * x) + (y * y) + (z * z) <= ((meteoriteRadius - 1) * (meteoriteRadius - 1))) {
                            corePosList.add(new BlockPos(x, y, z));
                        }
                    }
                }
            }

            for (BlockPos point : corePosList) {
                BlockPos pointPos = new BlockPos(pos.getX() + point.getX(), pos.getY() + point.getY(), pos.getZ() + point.getZ());
                world.setBlockState(pointPos, MMBlocks.MALACORE.get().getDefaultState(), 2);
            }

            world.setBlockState(pos, MMBlocks.AIR_MALACHITE_ORE.get().getDefaultState(), 2);

            return true;
        } else {
            return false;
        }

    }
}
