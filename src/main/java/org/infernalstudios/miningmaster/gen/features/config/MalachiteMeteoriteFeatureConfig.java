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

package org.infernalstudios.miningmaster.gen.features.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class MalachiteMeteoriteFeatureConfig implements IFeatureConfig {
    public static final Codec<MalachiteMeteoriteFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(
            Codec.INT.fieldOf("min_island_radius").forGetter((config) -> config.minIslandRadius),
            Codec.INT.fieldOf("max_island_radius").forGetter((config) -> config.maxIslandRadius),
            Codec.INT.fieldOf("min_meteorite_radius").forGetter((config) -> config.minMeteoriteRadius),
            Codec.INT.fieldOf("max_meteorite_radius").forGetter((config) -> config.maxMeteoriteRadius))
        .apply(builder, MalachiteMeteoriteFeatureConfig::new);
    });

    public final int minIslandRadius;
    public final int maxIslandRadius;
    public final int minMeteoriteRadius;
    public final int maxMeteoriteRadius;

    public MalachiteMeteoriteFeatureConfig(int minIslandRadius, int maxIslandRadius, int minMeteoriteRadius, int maxMeteoriteRadius) {
        this.minIslandRadius = minIslandRadius;
        this.maxIslandRadius = maxIslandRadius;
        this.minMeteoriteRadius = minMeteoriteRadius;
        this.maxMeteoriteRadius = maxMeteoriteRadius;
    }
}
