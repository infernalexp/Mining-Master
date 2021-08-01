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

package org.infernalstudios.miningmaster.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MiningMasterConfig {
    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final MiningMasterConfig CONFIG;

    static {
        final Pair<MiningMasterConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(MiningMasterConfig::new);
        CONFIG_SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    public MiningMasterConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Enchantments");


        builder.pop();
    }
}
