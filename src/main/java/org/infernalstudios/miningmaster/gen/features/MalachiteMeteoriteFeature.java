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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import org.infernalstudios.miningmaster.gen.features.config.MalachiteMeteoriteFeatureConfig;
import org.infernalstudios.miningmaster.init.MMBlocks;

import java.util.Random;

import static java.lang.Math.sqrt;

public class MalachiteMeteoriteFeature extends Feature<MalachiteMeteoriteFeatureConfig> {

    public MalachiteMeteoriteFeature(Codec<MalachiteMeteoriteFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, MalachiteMeteoriteFeatureConfig config) {

        if (rand.nextInt(100) < config.chanceToGenerate) {
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

            if (flag) {
                float islandRadius = (float) (rand.nextInt(config.maxRadius - config.minRadius) + config.minRadius);

                double meteoriteRadius = islandRadius / (rand.nextInt(2) + 2) + rand.nextInt(3) + 0.5;
                double meteoriteOffset = rand.nextInt( 2 * (int) meteoriteRadius / 3) - (meteoriteRadius / 3);

                generateIsland(world, rand, pos, islandRadius);
                generateImpactCrater(world, rand, pos, meteoriteOffset, meteoriteRadius);
                generateMeteorite(world, rand, pos, meteoriteOffset, meteoriteRadius);
                scatterCrust(world, rand, pos, meteoriteRadius + rand.nextInt(3) + 3, islandRadius);

                return true;
            }
        }
        return false;
    }

    private void generateIsland(ISeedReader world, Random rand, BlockPos pos, float islandRadius) {
        for (int i = 0; islandRadius > 0.5F; --i) {
            for (int j = MathHelper.floor(-islandRadius); j <= MathHelper.ceil(islandRadius); ++j) {
                for (int k = MathHelper.floor(-islandRadius); k <= MathHelper.ceil(islandRadius); ++k) {
                    if ((float) (j * j + k * k) <= (islandRadius + 1.0F) * (islandRadius + 1.0F)) {
                        this.setBlockState(world, pos.add(j, i, k), Blocks.END_STONE.getDefaultState());
                    }
                }
            }

            islandRadius = (float) ((double) islandRadius - ((double) rand.nextInt(2) + 0.5D));
        }
    }

    private void generateMeteorite(ISeedReader world, Random rand, BlockPos pos, double meteoriteOffset, double meteoriteRadius) {
        Vector3d meteoritePos = new Vector3d(pos.getX(), pos.getY() + meteoriteOffset, pos.getZ());

        int airCoreSize = Math.max(2, (int) meteoriteRadius / 2);

            for (double x = Math.floor(-meteoriteRadius); x <= Math.ceil(meteoriteRadius); x++) {
                for (double y = Math.floor(-meteoriteRadius); y <= Math.ceil(meteoriteRadius); y++) {
                    for (double z = Math.floor(-meteoriteRadius); z <= Math.ceil(meteoriteRadius); z++) {
                        double squaring = new Vector3d(x, y, z).lengthSquared();

                        if (squaring > (meteoriteRadius * meteoriteRadius)) {
                            continue;
                        }

                        BlockPos pointPos = new BlockPos(meteoritePos.getX() + x, meteoritePos.getY() + y, meteoritePos.getZ() + z);

                        if (squaring > (meteoriteRadius - 1) * (meteoriteRadius - 1)) {
                            if (pointPos.getY() > pos.getY() || rand.nextInt(100) < 85) {
                                world.setBlockState(pointPos, MMBlocks.MALACRUST.get().getDefaultState(), 2);
                            }
                        } else if (squaring > airCoreSize) {
                            world.setBlockState(pointPos, MMBlocks.MALACORE.get().getDefaultState(), 2);
                        } else if (squaring > 0) {
                            world.setBlockState(pointPos, Blocks.AIR.getDefaultState(), 2);
                        } else {
                            world.setBlockState(pointPos, MMBlocks.AIR_MALACHITE_ORE.get().getDefaultState(), 2);
                        }
                    }
                }
        }
    }

    private void generateImpactCrater(ISeedReader world, Random rand, BlockPos pos, double meteoriteOffset, double meteoriteRadius) {
        double craterRadius = meteoriteRadius + 3;
        Vector3d craterPos = new Vector3d(pos.getX(), pos.getY() + (craterRadius - (meteoriteRadius + meteoriteOffset) + 1), pos.getZ());

        for (double y = Math.floor(-craterRadius); y <= Math.ceil(craterRadius); y++) {
            for (double x = Math.floor(-craterRadius); x <= Math.ceil(craterRadius); x++) {
                for (double z = Math.floor(-craterRadius); z <= Math.ceil(craterRadius); z++) {
                    double squaring = new Vector3d(x, y, z).lengthSquared();

                    if (squaring > (craterRadius * craterRadius)) {
                        continue;
                    }

                    BlockPos pointPos = new BlockPos(craterPos.getX() + x, craterPos.getY() + y, craterPos.getZ() + z);
                    if (pointPos.getY() <= pos.getY() && rand.nextInt(5) < 4) {
                        world.setBlockState(pointPos, Blocks.AIR.getDefaultState(), 2);
                    }
                }
            }
        }
    }

    private void scatterCrust(ISeedReader world, Random rand, BlockPos pos, double craterRadius, float islandRadius) {
        int numberOfChunks = rand.nextInt((int) islandRadius * 15) + (int) islandRadius * 8;
        for (int i = 0; i < numberOfChunks; i++) {
            double r = (rand.nextInt((int) (islandRadius - craterRadius + 1)) + craterRadius) * sqrt(rand.nextDouble());
            double theta = rand.nextDouble() * 2 * Math.PI;
            double xOffset = r * Math.cos(theta);
            double zOffset = r * Math.sin(theta);

            BlockPos randPos = new BlockPos(pos.getX() + xOffset, pos.getY() + rand.nextInt(2), pos.getZ() + zOffset);
            if (randPos.distanceSq(pos.getX(), pos.getY(), pos.getZ(), true) >= craterRadius) {
                world.setBlockState(randPos, MMBlocks.MALACRUST.get().getDefaultState(), 2);
            }
        }
    }
}
