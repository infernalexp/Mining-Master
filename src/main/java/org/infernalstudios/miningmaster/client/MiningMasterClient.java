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

package org.infernalstudios.miningmaster.client;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.client.RecipeBookRegistry;
import net.minecraftforge.fml.ModLoadingContext;
import org.infernalstudios.miningmaster.client.gui.screen.inventory.GemForgeScreen;
import org.infernalstudios.miningmaster.config.gui.ConfigScreen;
import org.infernalstudios.miningmaster.init.MMContainerTypes;
import org.infernalstudios.miningmaster.init.MMItems;
import org.infernalstudios.miningmaster.init.MMRecipes;

import java.util.List;

public class MiningMasterClient {
    private static RecipeBookCategories GEM_FORGE;

    public static void init() {
        // Registering Config GUI Extension Point
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory(((minecraft, screen) -> new ConfigScreen())));

        MenuScreens.register(MMContainerTypes.GEM_FORGE_CONTAINER.get(), GemForgeScreen::new);

        GEM_FORGE = RecipeBookCategories.create("GEM_FORGE", new ItemStack(MMItems.FIRE_RUBY.get()));
        RecipeBookRegistry.addCategoriesToType(MMRecipes.GEM_FORGE, List.of(GEM_FORGE));
        RecipeBookRegistry.addCategoriesFinder(MMRecipes.FORGING_RECIPE_TYPE, MiningMasterClient::getForgingCategory);
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
