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
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;
import org.infernalstudios.miningmaster.gen.features.config.MalachiteMeteoriteFeatureConfig;
import org.infernalstudios.miningmaster.init.MMBlocks;

import java.util.Random;

import static java.lang.Math.sqrt;

public class MalachiteMeteoriteFeature extends Feature<MalachiteMeteoriteFeatureConfig> {

    public MalachiteMeteoriteFeature(Codec<MalachiteMeteoriteFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<MalachiteMeteoriteFeatureConfig> context) {
        Random rand = context.random();
        BlockPos pos = context.origin();
        WorldGenLevel level = context.level();
        MalachiteMeteoriteFeatureConfig config = context.config();

        if (rand.nextInt(100) < config.chanceToGenerate) {
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos.getX(), 0, pos.getZ());

            boolean flag = true;

            while (mutable.getY() <= level.getMaxBuildHeight()) {
                BlockState state = level.getBlockState(mutable);
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

                generateIsland(level, rand, pos, islandRadius);
                generateImpactCrater(level, rand, pos, meteoriteOffset, meteoriteRadius);
                generateMeteorite(level, rand, pos, meteoriteOffset, meteoriteRadius);
                scatterCrust(level, rand, pos, meteoriteRadius + rand.nextInt(3) + 3, islandRadius);

                return true;
            }
        }
        return false;
    }

    private void generateIsland(WorldGenLevel world, Random rand, BlockPos pos, float islandRadius) {
        for (int i = 0; islandRadius > 0.5F; --i) {
            for (int j = Mth.floor(-islandRadius); j <= Mth.ceil(islandRadius); ++j) {
                for (int k = Mth.floor(-islandRadius); k <= Mth.ceil(islandRadius); ++k) {
                    if ((float) (j * j + k * k) <= (islandRadius + 1.0F) * (islandRadius + 1.0F)) {
                        this.setBlock(world, pos.offset(j, i, k), Blocks.END_STONE.defaultBlockState());
                    }
                }
            }

            islandRadius = (float) ((double) islandRadius - ((double) rand.nextInt(2) + 0.5D));
        }
    }

    private void generateMeteorite(WorldGenLevel world, Random rand, BlockPos pos, double meteoriteOffset, double meteoriteRadius) {
        Vec3 meteoritePos = new Vec3(pos.getX(), pos.getY() + meteoriteOffset, pos.getZ());

        int airCoreSize = Math.max(2, (int) meteoriteRadius / 2);

            for (double x = Math.floor(-meteoriteRadius); x <= Math.ceil(meteoriteRadius); x++) {
                for (double y = Math.floor(-meteoriteRadius); y <= Math.ceil(meteoriteRadius); y++) {
                    for (double z = Math.floor(-meteoriteRadius); z <= Math.ceil(meteoriteRadius); z++) {
                        double squaring = new Vec3(x, y, z).lengthSqr();

                        if (squaring > (meteoriteRadius * meteoriteRadius)) {
                            continue;
                        }

                        BlockPos pointPos = new BlockPos(meteoritePos.x() + x, meteoritePos.y() + y, meteoritePos.z() + z);

                        if (squaring > (meteoriteRadius - 1) * (meteoriteRadius - 1)) {
                            if (pointPos.getY() > pos.getY() || rand.nextInt(100) < 85) {
                                world.setBlock(pointPos, MMBlocks.MALACRUST.get().defaultBlockState(), 2);
                            }
                        } else if (squaring > airCoreSize) {
                            world.setBlock(pointPos, MMBlocks.MALACORE.get().defaultBlockState(), 2);
                        } else if (squaring > 0) {
                            world.setBlock(pointPos, Blocks.AIR.defaultBlockState(), 2);
                        } else {
                            world.setBlock(pointPos, MMBlocks.AIR_MALACHITE_ORE.get().defaultBlockState(), 2);
                        }
                    }
                }
        }
    }

    private void generateImpactCrater(WorldGenLevel world, Random rand, BlockPos pos, double meteoriteOffset, double meteoriteRadius) {
        double craterRadius = meteoriteRadius + 3;
        Vec3 craterPos = new Vec3(pos.getX(), pos.getY() + (craterRadius - (meteoriteRadius + meteoriteOffset) + 1), pos.getZ());

        for (double y = Math.floor(-craterRadius); y <= Math.ceil(craterRadius); y++) {
            for (double x = Math.floor(-craterRadius); x <= Math.ceil(craterRadius); x++) {
                for (double z = Math.floor(-craterRadius); z <= Math.ceil(craterRadius); z++) {
                    double squaring = new Vec3(x, y, z).lengthSqr();

                    if (squaring > (craterRadius * craterRadius)) {
                        continue;
                    }

                    BlockPos pointPos = new BlockPos(craterPos.x() + x, craterPos.y() + y, craterPos.z() + z);
                    if (pointPos.getY() <= pos.getY() && rand.nextInt(5) < 4) {
                        world.setBlock(pointPos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }
    }

    private void scatterCrust(WorldGenLevel world, Random rand, BlockPos pos, double craterRadius, float islandRadius) {
        int numberOfChunks = rand.nextInt((int) islandRadius * 15) + (int) islandRadius * 8;
        for (int i = 0; i < numberOfChunks; i++) {
            double r = (rand.nextInt((int) (islandRadius - craterRadius + 1)) + craterRadius) * sqrt(rand.nextDouble());
            double theta = rand.nextDouble() * 2 * Math.PI;
            double xOffset = r * Math.cos(theta);
            double zOffset = r * Math.sin(theta);

            BlockPos randPos = new BlockPos(pos.getX() + xOffset, pos.getY() + rand.nextInt(2), pos.getZ() + zOffset);
            if (randPos.distToCenterSqr(pos.getX(), pos.getY(), pos.getZ()) >= craterRadius) {
                world.setBlock(randPos, MMBlocks.MALACRUST.get().defaultBlockState(), 2);
            }
        }
    }
}
