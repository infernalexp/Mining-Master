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

import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import org.infernalstudios.miningmaster.init.MMRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class MixinClientRecipeBook {

    @Inject(method = "getCategory", at = @At(value = "HEAD"), cancellable = true)
    private static void MM_getGemForgeCategory(IRecipe<?> recipe, CallbackInfoReturnable<RecipeBookCategories> cir) {
        IRecipeType<?> irecipetype = recipe.getType();
        if (irecipetype == MMRecipes.FORGING_RECIPE_TYPE) {
            cir.setReturnValue(RecipeBookCategories.valueOf("GEM_FORGE"));
        }
    }
}
