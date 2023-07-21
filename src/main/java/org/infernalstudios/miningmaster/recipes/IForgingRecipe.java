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

package org.infernalstudios.miningmaster.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.miningmaster.MiningMaster;

public interface IForgingRecipe extends Recipe<Container> {
    ResourceLocation TYPE_ID = new ResourceLocation(MiningMaster.MOD_ID, "forging");

    @Override
    default RecipeType<?> getType() {
        return ForgeRegistries.RECIPE_TYPES.getValue(TYPE_ID);
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {return true;}

    @Override
    default boolean isSpecial() {
        return false;
    }
}
