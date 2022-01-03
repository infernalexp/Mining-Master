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

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import org.infernalstudios.miningmaster.MiningMaster;

public class MMTags {

    public static class EntityTypes {
        public static final ITag.INamedTag<EntityType<?>> FIRE_ENTITIES = tag("fire_entities");

        private static ITag.INamedTag<EntityType<?>> tag(String name) {
            return EntityTypeTags.createOptional(new ResourceLocation(MiningMaster.MOD_ID, name));
        }
    }

    public static class Items {
        public static final ITag.INamedTag<Item> GEM_ENCHANTING_BLACKLIST = tag("gem_enchanting_blacklist");
        public static final ITag.INamedTag<Item> STONEBREAKER_ITEMS = tag("stonebreaker_items");

        private static ITag.INamedTag<Item> tag(String name) {
            return ItemTags.createOptional(new ResourceLocation(MiningMaster.MOD_ID, name));
        }
    }

    public static class Blocks {
        public static final ITag.INamedTag<Block> SNOWPIERCER_BLOCKS = tag("snowpiercer_blocks");

        private static ITag.INamedTag<Block> tag(String name) {
            return BlockTags.createOptional(new ResourceLocation(MiningMaster.MOD_ID, name));
        }
    }

}
