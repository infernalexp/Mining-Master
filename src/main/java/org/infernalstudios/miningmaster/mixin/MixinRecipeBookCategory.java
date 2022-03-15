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

import net.minecraft.item.crafting.RecipeBookCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(RecipeBookCategory.class)
public abstract class MixinRecipeBookCategory {
    @Shadow
    @Final
    @Mutable
    private static RecipeBookCategory[] $VALUES;

    private static final RecipeBookCategory GEM_FORGE = MM_addVariant("GEM_FORGE");

    @Invoker("<init>")
    public static RecipeBookCategory MM_invokeInit(String internalName, int internalId) {
        throw new AssertionError();
    }

    private static RecipeBookCategory MM_addVariant(String internalName) {
        ArrayList<RecipeBookCategory> variants = new ArrayList<RecipeBookCategory>(Arrays.asList(MixinRecipeBookCategory.$VALUES));
        RecipeBookCategory recipeBookCategory = MM_invokeInit(internalName,variants.get(variants.size() - 1).ordinal() + 1);
        variants.add(recipeBookCategory);
        MixinRecipeBookCategory.$VALUES = variants.toArray(new RecipeBookCategory[0]);
        return recipeBookCategory;
    }
}
