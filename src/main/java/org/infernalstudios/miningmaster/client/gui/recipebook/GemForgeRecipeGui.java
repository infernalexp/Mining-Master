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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.infernalstudios.miningmaster.MiningMaster;

import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GemForgeRecipeGui extends RecipeBookComponent {
    protected static final ResourceLocation RECIPE_BOOK_GEM_FORGE = new ResourceLocation(MiningMaster.MOD_ID, "textures/gui/recipe_book_gem_forge.png");
    private static final Component translationKeyForgable = Component.translatable(MiningMaster.MOD_ID + ".gui.recipebook.toggleRecipes.forgable");

    protected Component getRecipeFilterName() {
        return translationKeyForgable;
    }

    protected void initFilterButtonTextures() {
        this.filterButton.initTextureValues(152, 41, 28, 18, RECIPE_BOOK_GEM_FORGE);
    }

    @Override
    public void setupGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
        this.ghostRecipe.setRecipe(recipe);
        this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe, recipe.getIngredients().iterator(), 0);
    }

    @Override
    public void addItemToSlot(Iterator<Ingredient> ingredients, int slotIn, int maxAmount, int y, int x) {
        Ingredient ingredient = ingredients.next();
        if (!ingredient.isEmpty()) {
            Slot slot;

            if (ingredients.hasNext()) {
                slot = this.menu.slots.get(slotIn);
            } else {
                slot = this.menu.slots.get(this.menu.getResultSlotIndex());
            }

            this.ghostRecipe.addIngredient(ingredient, slot.x, slot.y);
        }
    }

    @Override
    public void placeRecipe(int width, int height, int outputSlot, Recipe<?> recipe, Iterator<Ingredient> ingredients, int maxAmount) {
        int i = width;
        int j = height;

        int k1 = 0;

        for(int k = 0; k < height; ++k) {

            boolean flag = (float)j < (float)height / 2.0F;
            int l = Mth.floor((float)height / 2.0F - (float)j / 2.0F);
            if (flag && l > k) {
                k1 += width;
                ++k;
            }

            for(int i1 = 0; i1 < width; ++i1) {
                if (!ingredients.hasNext()) {
                    return;
                }

                flag = (float)i < (float)width / 2.0F;
                l = Mth.floor((float)width / 2.0F - (float)i / 2.0F);
                int j1 = i;
                boolean flag1 = i1 < i;
                if (flag) {
                    j1 = l + i;
                    flag1 = l <= i1 && i1 < l + i;
                }

                if (flag1) {
                    this.addItemToSlot(ingredients, k1, maxAmount, k, i1);
                } else if (j1 == i1) {
                    k1 += width - i1;
                    break;
                }

                ++k1;
            }
        }

    }

    @Override
    public void renderGhostRecipe(PoseStack matrixStack, int xOffset, int yOffset, boolean displayOutputSquare, float time) {
        super.renderGhostRecipe(matrixStack, xOffset, yOffset, false, time);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.isVisible()) {
            matrixStack.pushPose();
            matrixStack.translate(0.0F, 0.0F, 100.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, RECIPE_BOOK_GEM_FORGE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = (this.width - 147) / 2 - this.xOffset;
            int j = (this.height - 166) / 2;
            this.blit(matrixStack, i, j, 1, 1, 147, 166);
            if (!this.searchBox.isFocused() && this.searchBox.getValue().isEmpty()) {
                drawString(matrixStack, this.minecraft.font, SEARCH_HINT, i + 25, j + 14, -1);
            } else {
                this.searchBox.render(matrixStack, mouseX, mouseY, partialTicks);
            }

            for (RecipeBookTabButton recipetabtogglewidget : this.tabButtons) {
                recipetabtogglewidget.render(matrixStack, mouseX, mouseY, partialTicks);
            }

            this.filterButton.render(matrixStack, mouseX, mouseY, partialTicks);
            this.recipeBookPage.render(matrixStack, i, j, mouseX, mouseY, partialTicks);
            matrixStack.popPose();
        }
    }
}
