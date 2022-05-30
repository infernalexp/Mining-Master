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

package org.infernalstudios.miningmaster.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.infernalstudios.miningmaster.MiningMaster;

public class MMTags {

    public static class Items {
        public static final TagKey<Item> GEM_ENCHANTING_BLACKLIST = tag("gem_enchanting_blacklist");
        public static final TagKey<Item> STONEBREAKER_ITEMS = tag("stonebreaker_items");
        public static final TagKey<Item> GEMS = tag("gems");
        public static final TagKey<Item> CATALYSTS = tag("catalysts");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(MiningMaster.MOD_ID, name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> SNOWPIERCER_BLOCKS = tag("snowpiercer_blocks");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(MiningMaster.MOD_ID, name));
        }
    }

}
