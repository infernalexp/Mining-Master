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

package org.infernalstudios.miningmaster.gen.features.config;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

public class NativeGemOreFeatureConfig implements FeatureConfiguration {
    public static final Codec<NativeGemOreFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            Codec.list(NativeGemOreFeatureConfig.TargetBlockState.CODEC).fieldOf("targets").forGetter((config) -> config.targetStates),
            Codec.floatRange(0.0F, 1.0F).fieldOf("discard_chance_on_air_exposure").forGetter((config) -> config.discardChanceOnAirExposure))
            .apply(builder, NativeGemOreFeatureConfig::new));

    public final List<NativeGemOreFeatureConfig.TargetBlockState> targetStates;
    public final float discardChanceOnAirExposure;

    public NativeGemOreFeatureConfig(List<NativeGemOreFeatureConfig.TargetBlockState> targets, float discardChance) {
        this.targetStates = targets;
        this.discardChanceOnAirExposure = discardChance;
    }

    public NativeGemOreFeatureConfig(List<NativeGemOreFeatureConfig.TargetBlockState> targets) {
        this(targets,0.0F);
    }

    public NativeGemOreFeatureConfig(RuleTest target, BlockState state,float discardChance) {
        this(ImmutableList.of(new NativeGemOreFeatureConfig.TargetBlockState(target, state)), discardChance);
    }

    public NativeGemOreFeatureConfig(RuleTest target, BlockState state) {
        this(ImmutableList.of(new NativeGemOreFeatureConfig.TargetBlockState(target, state)), 0.0F);
    }

    public static NativeGemOreFeatureConfig.TargetBlockState target(RuleTest target, BlockState state) {
        return new NativeGemOreFeatureConfig.TargetBlockState(target, state);
    }

    public static class TargetBlockState {
        public static final Codec<NativeGemOreFeatureConfig.TargetBlockState> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                RuleTest.CODEC.fieldOf("target").forGetter((config) -> config.target),
                BlockState.CODEC.fieldOf("state").forGetter((config) -> config.state))
                .apply(builder, TargetBlockState::new));

        public final RuleTest target;
        public final BlockState state;

        TargetBlockState(RuleTest ruleTest, BlockState state) {
            this.target = ruleTest;
            this.state = state;
        }
    }
}
