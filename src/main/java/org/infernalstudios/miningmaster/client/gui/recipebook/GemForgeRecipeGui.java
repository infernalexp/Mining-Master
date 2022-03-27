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

package org.infernalstudios.miningmaster.client.gui.recipebook;

import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.infernalstudios.miningmaster.MiningMaster;

import java.util.Iterator;

@OnlyIn(Dist.CLIENT)
public class GemForgeRecipeGui extends RecipeBookGui {
    private static final ITextComponent translationKeyForgable = new TranslationTextComponent(MiningMaster.MOD_ID + ".gui.recipebook.toggleRecipes.forgable");

    protected ITextComponent func_230479_g_() {
        return translationKeyForgable;
    }

    @Override
    public void setSlotContents(Iterator<Ingredient> ingredients, int slotIn, int maxAmount, int y, int x) {
        Ingredient ingredient = ingredients.next();
        if (!ingredient.hasNoMatchingItems()) {
            Slot slot;

            if (ingredients.hasNext()) {
                slot = this.field_201522_g.inventorySlots.get(slotIn);
            } else {
                slot = this.field_201522_g.inventorySlots.get(this.field_201522_g.getOutputSlot());
            }

            this.ghostRecipe.addIngredient(ingredient, slot.xPos, slot.yPos);
        }
    }

    @Override
    public void placeRecipe(int width, int height, int outputSlot, IRecipe<?> recipe, Iterator<Ingredient> ingredients, int maxAmount) {
        int i = width;
        int j = height;

        int k1 = 0;

        for(int k = 0; k < height; ++k) {

            boolean flag = (float)j < (float)height / 2.0F;
            int l = MathHelper.floor((float)height / 2.0F - (float)j / 2.0F);
            if (flag && l > k) {
                k1 += width;
                ++k;
            }

            for(int i1 = 0; i1 < width; ++i1) {
                if (!ingredients.hasNext()) {
                    return;
                }

                flag = (float)i < (float)width / 2.0F;
                l = MathHelper.floor((float)width / 2.0F - (float)i / 2.0F);
                int j1 = i;
                boolean flag1 = i1 < i;
                if (flag) {
                    j1 = l + i;
                    flag1 = l <= i1 && i1 < l + i;
                }

                if (flag1) {
                    this.setSlotContents(ingredients, k1, maxAmount, k, i1);
                } else if (j1 == i1) {
                    k1 += width - i1;
                    break;
                }

                ++k1;
            }
        }

    }
}
