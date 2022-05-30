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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class MalachiteMeteoriteFeatureConfig implements FeatureConfiguration {
    public static final Codec<MalachiteMeteoriteFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(
            Codec.INT.fieldOf("min_radius").forGetter((config) -> config.minRadius),
            Codec.INT.fieldOf("max_radius").forGetter((config) -> config.maxRadius),
            Codec.INT.fieldOf("chance_to_generate").forGetter((config) -> config.chanceToGenerate))
        .apply(builder, MalachiteMeteoriteFeatureConfig::new);
    });

    public final int minRadius;
    public final int maxRadius;
    public final int chanceToGenerate;

    public MalachiteMeteoriteFeatureConfig(int minRadius, int maxRadius, int chanceToGenerate) {
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.chanceToGenerate = chanceToGenerate;
    }
}
