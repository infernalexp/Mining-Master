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

package org.infernalstudios.miningmaster.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.infernalstudios.miningmaster.MiningMaster;

public class MiningMasterConfig {
    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final MiningMasterConfig CONFIG;

    static {
        Pair<MiningMasterConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(MiningMasterConfig::new);

        CONFIG_SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    public final ForgeConfigSpec.BooleanValue fireRubyEnabled;
    public final ForgeConfigSpec.BooleanValue iceSapphireEnabled;
    public final ForgeConfigSpec.BooleanValue spiritGarnetEnabled;
    public final ForgeConfigSpec.BooleanValue hastePeridotEnabled;
    public final ForgeConfigSpec.BooleanValue luckyCitrineEnabled;
    public final ForgeConfigSpec.BooleanValue diveAquamarineEnabled;
    public final ForgeConfigSpec.BooleanValue divineBerylEnabled;
    public final ForgeConfigSpec.BooleanValue spiderKunziteEnabled;
    public final ForgeConfigSpec.BooleanValue unbreakingIoliteEnabled;
    public final ForgeConfigSpec.BooleanValue heartRhodoniteEnabled;
    public final ForgeConfigSpec.BooleanValue powerPyriteEnabled;
    public final ForgeConfigSpec.BooleanValue kineticOpalEnabled;
    public final ForgeConfigSpec.BooleanValue airMalachiteEnabled;

    public final ForgeConfigSpec.IntValue commonGemsPerChunk;
    public final ForgeConfigSpec.IntValue rareGemsPerChunk;
    public final ForgeConfigSpec.IntValue randomGemsPerChunk;

    public MiningMasterConfig(ForgeConfigSpec.Builder builder) {

        fireRubyEnabled = builder
                .comment("Determines if Fire Ruby will generate.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.fireRubyEnabled")
                .define("fireRubyEnabled", true);

        iceSapphireEnabled = builder
                .comment("Determines if Ice Sapphire will generate.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.iceSapphireEnabled")
                .define("iceSapphireEnabled", true);

        spiritGarnetEnabled = builder
                .comment("Determines if Spirit Garnet will generate.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.spiritGarnetEnabled")
                .define("spiritGarnetEnabled", true);

        hastePeridotEnabled = builder
                .comment("Determines if Haste Peridot will generate.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.hastePeridotEnabled")
                .define("hastePeridotEnabled", true);

        luckyCitrineEnabled = builder
                .comment("Determines if Lucky Citrine will generate.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.luckyCitrineEnabled")
                .define("luckyCitrineEnabled", true);

        diveAquamarineEnabled = builder
                .comment("Determines if Dive Aquamarine will generate.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.diveAquamarineEnabled")
                .define("diveAquamarineEnabled", true);

        divineBerylEnabled = builder
                .comment("Determines if Divine Beryl will generate.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.divineBerylEnabled")
                .define("diveAquamarineEnabled", true);

        spiderKunziteEnabled = builder
                .comment("Determines if Spider Kunzite will generate.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.spiderKunziteEnabled")
                .define("diveAquamarineEnabled", true);

        unbreakingIoliteEnabled = builder
                .comment("Determines if Unbreaking Iolite will generate.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.unbreakingIoliteEnabled")
                .define("diveAquamarineEnabled", true);

        heartRhodoniteEnabled = builder
                .comment("Determines if Heart Rhodonite will generate.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.heartRhodoniteEnabled")
                .define("heartRhodoniteEnabled", true);

        powerPyriteEnabled = builder
                .comment("Determines if Power Pyrite will generate.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.powerPyriteEnabled")
                .define("powerPyriteEnabled", true);

        kineticOpalEnabled = builder
                .comment("Determines if Kinetic Opal will generate.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.kineticOpalEnabled")
                .define("kineticOpalEnabled", true);

        airMalachiteEnabled = builder
                .comment("Determines if Air Malachite will generate.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.airMalachiteEnabled")
                .define("airMalachiteEnabled", true);

        commonGemsPerChunk = builder
                .comment("Determines how many times common gems will attempt to spawn per chunk in their native biomes. REQUIRES RESTART.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.commonGemsPerChunk")
                .defineInRange("commonGemsPerChunk", 3, 0, 100);

        rareGemsPerChunk = builder
                .comment("Determines how many times rare gems will attempt to spawn per chunk in their native biomes. REQUIRES RESTART.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.rareGemsPerChunk")
                .defineInRange("rareGemsPerChunk", 2, 0, 100);

        randomGemsPerChunk = builder
                .comment("Determines how many times a random gem ore will attempt to spawn per chunk. REQUIRES RESTART.")
                .translation(MiningMaster.MOD_ID + ".config.tooltip.randomGemsPerChunk")
                .defineInRange("randomGemsPerChunk", 1, 0, 100);

    }
}
