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

package org.infernalstudios.miningmaster.events;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.init.MMItems;
import org.infernalstudios.miningmaster.init.MMRecipes;

import java.util.List;

@Mod.EventBusSubscriber(modid = MiningMaster.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MiningMasterClientEvents {
    private static RecipeBookCategories GEM_FORGE;

    @SubscribeEvent
    public static void registerRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        GEM_FORGE = RecipeBookCategories.create("GEM_FORGE", new ItemStack(MMItems.FIRE_RUBY.get()));
        event.registerBookCategories(MMRecipes.GEM_FORGE, List.of(GEM_FORGE));
        event.registerRecipeCategoryFinder(MMRecipes.FORGING_RECIPE_TYPE, MiningMasterClientEvents::getForgingCategory);
    }

    private static RecipeBookCategories getForgingCategory(Recipe<?> recipe) {
        RecipeType<?> recipeType = recipe.getType();
        if (recipeType.equals(MMRecipes.FORGING_RECIPE_TYPE)) {
            return GEM_FORGE;
        } else {
            return RecipeBookCategories.UNKNOWN;
        }
    }

}
