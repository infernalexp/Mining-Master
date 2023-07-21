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

package org.infernalstudios.miningmaster.client.gui.screen.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.client.gui.recipebook.GemForgeRecipeGui;
import org.infernalstudios.miningmaster.containers.GemForgeContainer;
import org.infernalstudios.miningmaster.network.MMNetworkHandler;
import org.infernalstudios.miningmaster.network.UpdateGemForgePacket;


@OnlyIn(Dist.CLIENT)
public class GemForgeScreen extends AbstractContainerScreen<GemForgeContainer> implements RecipeUpdateListener {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(MiningMaster.MOD_ID, "textures/gui/container/gem_forge.png");
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(MiningMaster.MOD_ID, "textures/gui/recipe_button.png");
    private boolean widthTooNarrowIn;
    public final RecipeBookComponent recipeGui;

    public GemForgeScreen(GemForgeContainer screenContainer, Inventory inv, Component name) {
        super(screenContainer, inv, name);
        this.recipeGui = new GemForgeRecipeGui();
    }

    public void init() {
        super.init();
        this.widthTooNarrowIn = this.width < 379;
        this.recipeGui.init(this.width, this.height, this.minecraft, this.widthTooNarrowIn, this.menu);
        this.leftPos = this.recipeGui.updateScreenPosition(this.width, this.imageWidth);
        this.addRenderableWidget(new ConfirmButton(142, 33, this));
        this.addRenderableWidget(new ImageButton(this.leftPos + 15, this.height / 2 - 49, 20, 18, 0, 0, 19, BUTTON_TEXTURE, (button) -> {
            this.recipeGui.initVisuals();
            this.recipeGui.toggleVisibility();
            this.leftPos = this.recipeGui.updateScreenPosition(this.width, this.imageWidth);
            button.setPosition(this.leftPos + 15, this.height / 2 - 49);
        }));
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void containerTick() {
        this.recipeGui.tick();
    }


    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        if (this.recipeGui.isVisible() && this.widthTooNarrowIn) {
            this.renderBg(matrixStack, partialTicks, mouseX, mouseY);
            this.recipeGui.render(matrixStack, mouseX, mouseY, partialTicks);
        } else {
            this.recipeGui.render(matrixStack, mouseX, mouseY, partialTicks);
            super.render(matrixStack, mouseX, mouseY, partialTicks);
            this.recipeGui.renderGhostRecipe(matrixStack, this.leftPos, this.topPos, true, partialTicks);
        }

        this.renderTooltip(matrixStack, mouseX, mouseY);
        this.recipeGui.renderTooltip(matrixStack, this.leftPos, this.topPos, mouseX, mouseY);
    }

    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isForgeActive()) {
            int k = this.menu.getForgeTimeScaled();
            this.blit(matrixStack, i + 60 + k, Math.min(j + 33 + k, j + 49), 176 + k,  Math.min(k, 16), 56 - (2 * k), Math.max(36 - (2 * k), 3));
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeGui.mouseClicked(mouseX, mouseY, button)) {
            return true;
        } else {
            return this.widthTooNarrowIn && this.recipeGui.isVisible() || super.mouseClicked(mouseX, mouseY, button);
        }
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void slotClicked(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        super.slotClicked(slotIn, slotId, mouseButton, type);
        this.recipeGui.slotClicked(slotIn);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return !this.recipeGui.keyPressed(keyCode, scanCode, modifiers) && super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        boolean flag = mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.imageWidth) || mouseY >= (double)(guiTopIn + this.imageHeight);
        return this.recipeGui.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, mouseButton) && flag;
    }

    public boolean charTyped(char codePoint, int modifiers) {
        return this.recipeGui.charTyped(codePoint, modifiers) || super.charTyped(codePoint, modifiers);
    }

    public void recipesUpdated() {
        this.recipeGui.recipesUpdated();
    }

    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeGui;
    }

    @OnlyIn(Dist.CLIENT)
    class ConfirmButton extends GemForgeScreen.Button {
        public ConfirmButton(int x, int y, GemForgeScreen screen) {
            super(x, y, screen);
        }

        @Override
        protected void renderIcon(PoseStack p_230454_1_) {
        }

        public void onPress() {
            if (GemForgeScreen.this.menu.isRecipeValid()) {
                MMNetworkHandler.sendToServer(new UpdateGemForgePacket(true));
            }
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class Button extends AbstractButton {
        private final GemForgeScreen screen;
        private final int xOffset;
        private final int yOffset;

        protected Button(int x, int y, GemForgeScreen screen) {
            super(x, y, 20, 20, Component.empty());
            this.screen = screen;
            this.xOffset = x;
            this.yOffset = y;
            this.setX(this.xOffset + this.screen.leftPos);
            this.setY(this.yOffset + this.screen.topPos);
        }

        public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, GUI_TEXTURE);
            int i = 165;
            int j = 0;
            if (this.screen.menu.isForgeActive()) {
                j += this.width;
            } else if (!this.screen.menu.isRecipeValid()) {
                j += this.width * 2;
            } else if (this.isHoveredOrFocused()) {
                j += this.width * 3;
            }

            this.setX(this.xOffset + this.screen.leftPos);
            this.setY(this.yOffset + this.screen.topPos);

            this.active = this.screen.menu.isRecipeValid() && !this.screen.menu.isForgeActive();

            this.blit(matrixStack, this.getX(), this.getY(), j, i, this.width, this.height);
            this.screen.itemRenderer.renderGuiItem(matrixStack, new ItemStack(Items.LAVA_BUCKET), this.getX() + 2, this.getY() + 2);
            this.renderIcon(matrixStack);
        }

        protected abstract void renderIcon(PoseStack p_230454_1_);
    }
}
