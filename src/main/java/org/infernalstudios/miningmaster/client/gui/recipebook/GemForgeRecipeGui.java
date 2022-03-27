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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.recipebook.IRecipeUpdateListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.recipebook.RecipeTabToggleWidget;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipePlacer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.infernalstudios.miningmaster.MiningMaster;

import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GemForgeRecipeGui extends RecipeBookGui implements IRenderable, IGuiEventListener, IRecipeUpdateListener, IRecipePlacer<Ingredient> {
    protected static final ResourceLocation RECIPE_BOOK_GEM_FORGE = new ResourceLocation(MiningMaster.MOD_ID, "textures/gui/recipe_book_gem_forge.png");
    private static final ITextComponent translationKeyForgable = new TranslationTextComponent(MiningMaster.MOD_ID + ".gui.recipebook.toggleRecipes.forgable");

    protected ITextComponent func_230479_g_() {
        return translationKeyForgable;
    }

    protected void func_205702_a() {
        this.toggleRecipesBtn.initTextureValues(152, 41, 28, 18, RECIPE_BOOK_GEM_FORGE);
    }

    @Override
    public void setupGhostRecipe(IRecipe<?> recipe, List<Slot> slots) {
        this.ghostRecipe.setRecipe(recipe);
        this.placeRecipe(this.field_201522_g.getWidth(), this.field_201522_g.getHeight(), this.field_201522_g.getOutputSlot(), recipe, recipe.getIngredients().iterator(), 0);
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

    @Override
    public void func_230477_a_(MatrixStack matrixStack, int xOffset, int yOffset, boolean displayOutputSquare, float time) {
        super.func_230477_a_(matrixStack, xOffset, yOffset, false, time);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.isVisible()) {
            RenderSystem.pushMatrix();
            RenderSystem.translatef(0.0F, 0.0F, 100.0F);
            this.mc.getTextureManager().bindTexture(RECIPE_BOOK_GEM_FORGE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = (this.width - 147) / 2 - this.xOffset;
            int j = (this.height - 166) / 2;
            this.blit(matrixStack, i, j, 1, 1, 147, 166);
            if (!this.searchBar.isFocused() && this.searchBar.getText().isEmpty()) {
                drawString(matrixStack, this.mc.fontRenderer, field_241620_l_, i + 25, j + 14, -1);
            } else {
                this.searchBar.render(matrixStack, mouseX, mouseY, partialTicks);
            }

            for (RecipeTabToggleWidget recipetabtogglewidget : this.recipeTabs) {
                recipetabtogglewidget.render(matrixStack, mouseX, mouseY, partialTicks);
            }

            this.toggleRecipesBtn.render(matrixStack, mouseX, mouseY, partialTicks);
            this.recipeBookPage.func_238927_a_(matrixStack, i, j, mouseX, mouseY, partialTicks);
            RenderSystem.popMatrix();
        }
    }
}
