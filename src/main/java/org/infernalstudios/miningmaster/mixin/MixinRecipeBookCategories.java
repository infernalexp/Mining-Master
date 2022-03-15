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

import com.google.common.collect.ImmutableList;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeBookCategory;
import org.infernalstudios.miningmaster.init.MMItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(RecipeBookCategories.class)
public abstract class MixinRecipeBookCategories {
    @Shadow
    @Final
    @Mutable
    private static RecipeBookCategories[] $VALUES;

    private static final RecipeBookCategories GEM_FORGE = MM_addVariant("GEM_FORGE", new ItemStack(MMItems.FIRE_RUBY.get()));

    private static final List<RecipeBookCategories> FORGING_CATEGORIES = ImmutableList.of(GEM_FORGE);

    @Invoker("<init>")
    public static RecipeBookCategories MM_invokeInit(String internalName, int internalId, ItemStack... itemIcon) {
        throw new AssertionError();
    }

    private static RecipeBookCategories MM_addVariant(String internalName, ItemStack... itemIcon) {
        ArrayList<RecipeBookCategories> variants = new ArrayList<RecipeBookCategories>(Arrays.asList(MixinRecipeBookCategories.$VALUES));
        RecipeBookCategories recipeBookCategory = MM_invokeInit(internalName,variants.get(variants.size() - 1).ordinal() + 1, itemIcon);
        variants.add(recipeBookCategory);
        MixinRecipeBookCategories.$VALUES = variants.toArray(new RecipeBookCategories[0]);
        return recipeBookCategory;
    }

    @Inject(method = "func_243236_a", at = @At(value = "HEAD"), cancellable = true)
    private static void MM_getRecipeCategory(RecipeBookCategory category, CallbackInfoReturnable<List<RecipeBookCategories>> cir) {
        if (category == RecipeBookCategory.valueOf("GEM_FORGE")) {
            cir.setReturnValue(FORGING_CATEGORIES);
        }
    }
}
