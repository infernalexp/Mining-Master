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

package org.infernalstudios.miningmaster.mixin;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeBookStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(RecipeBookStatus.class)
public class MixinRecipeBookStatus {
    @Shadow
    @Mutable
    @Final
    private static Map<RecipeBookCategory, Pair<String, String>> field_242147_a;

    static {
        Map<RecipeBookCategory, Pair<String, String>> mutableTagFields = Maps.newHashMap(field_242147_a);
        mutableTagFields.put(RecipeBookCategory.valueOf("GEM_FORGE"), Pair.of("isForgeGuiOpen", "isForgeFilteringCraftable"));
        field_242147_a = mutableTagFields;
    }
}
