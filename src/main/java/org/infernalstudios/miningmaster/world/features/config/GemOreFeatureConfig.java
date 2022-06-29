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

package org.infernalstudios.miningmaster.world.features.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.ArrayList;
import java.util.List;

public record GemOreFeatureConfig(List<TargetWeightedState> targetStates, float discardChanceOnAirExposure) implements FeatureConfiguration {
    public static final Codec<GemOreFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                Codec.list(TargetWeightedState.CODEC).fieldOf("targets").forGetter(GemOreFeatureConfig::targetStates),
                Codec.floatRange(0.0F, 1.0F).fieldOf("discard_chance_on_air_exposure").forGetter(GemOreFeatureConfig::discardChanceOnAirExposure))
            .apply(builder, GemOreFeatureConfig::new));

    public static class TargetWeightedState {
        public static final Codec<TargetWeightedState> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                        RuleTest.CODEC.fieldOf("target").forGetter((config) -> config.target),
                        Codec.list(WeightedState.CODEC).fieldOf("states").forGetter((config) -> config.weightedStates))
                .apply(builder, TargetWeightedState::new));

        public final RuleTest target;
        public final List<WeightedState> weightedStates;
        public final List<BlockState> states;

        TargetWeightedState(RuleTest ruleTest, List<WeightedState> weightedStates) {
            this.target = ruleTest;
            this.weightedStates = weightedStates;

            this.states = new ArrayList<>();

            for (GemOreFeatureConfig.WeightedState weightedState : weightedStates) {
                for (int i = 0; i < weightedState.weight; i++) {
                    this.states.add(weightedState.state);
                }
            }
        }
    }

    public record WeightedState(BlockState state, int weight) {
        public static final Codec<WeightedState> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                    BlockState.CODEC.fieldOf("state").forGetter(WeightedState::state),
                    Codec.intRange(0, 100).optionalFieldOf("weight", 1).forGetter(WeightedState::weight))
                .apply(builder, WeightedState::new));
    }

}
