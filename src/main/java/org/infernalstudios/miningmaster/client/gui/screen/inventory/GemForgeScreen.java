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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.AbstractRecipeBookGui;
import net.minecraft.client.gui.recipebook.FurnaceRecipeGui;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.containers.GemForgeContainer;
import org.infernalstudios.miningmaster.network.MMNetworkHandler;
import org.infernalstudios.miningmaster.network.UpdateGemForgePacket;

@OnlyIn(Dist.CLIENT)
public class GemForgeScreen extends ContainerScreen<GemForgeContainer> implements IRecipeShownListener {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(MiningMaster.MOD_ID + ':' + "textures/gui/container/gem_forge.png");
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(MiningMaster.MOD_ID + ':' + "textures/gui/recipe_button.png");
    private boolean widthTooNarrowIn;
    public final AbstractRecipeBookGui recipeGui;

    public GemForgeScreen(GemForgeContainer screenContainer, PlayerInventory inv, ITextComponent name) {
        super(screenContainer, inv, name);
        this.recipeGui = new FurnaceRecipeGui();
    }

    public void init() {
        super.init();
        this.widthTooNarrowIn = this.width < 379;
        this.recipeGui.init(this.width, this.height, this.minecraft, this.widthTooNarrowIn, this.container);
        this.guiLeft = this.recipeGui.updateScreenPosition(this.widthTooNarrowIn, this.width, this.xSize);
        this.addButton(new ConfirmButton(142, 33, this));
        this.addButton(new ImageButton(this.guiLeft + 15, this.height / 2 - 49, 20, 18, 0, 0, 19, BUTTON_TEXTURE, (button) -> {
            this.recipeGui.initSearchBar(this.widthTooNarrowIn);
            this.recipeGui.toggleVisibility();
            this.guiLeft = this.recipeGui.updateScreenPosition(this.widthTooNarrowIn, this.width, this.xSize);
            ((ImageButton)button).setPosition(this.guiLeft + 15, this.height / 2 - 49);
        }));
        this.titleX = (this.xSize - this.font.getStringPropertyWidth(this.title)) / 2;
    }

    public void tick() {
        super.tick();
        this.recipeGui.tick();
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        if (this.recipeGui.isVisible() && this.widthTooNarrowIn) {
            this.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
            this.recipeGui.render(matrixStack, mouseX, mouseY, partialTicks);
        } else {
            this.recipeGui.render(matrixStack, mouseX, mouseY, partialTicks);
            super.render(matrixStack, mouseX, mouseY, partialTicks);
            this.recipeGui.func_230477_a_(matrixStack, this.guiLeft, this.guiTop, true, partialTicks);
        }

        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        this.recipeGui.func_238924_c_(matrixStack, this.guiLeft, this.guiTop, mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

        if (this.container.isForgeActive()) {
            int k = this.container.getForgeTimeScaled();
            this.blit(matrixStack, i + 60 + (2 * k), j + 33 + k, 176 + (2 * k),  k, 56 - (4 * k), 36 - (2 * k));
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
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
        this.recipeGui.slotClicked(slotIn);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return !this.recipeGui.keyPressed(keyCode, scanCode, modifiers) && super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        boolean flag = mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.xSize) || mouseY >= (double)(guiTopIn + this.ySize);
        return this.recipeGui.func_195604_a(mouseX, mouseY, this.guiLeft, this.guiTop, this.xSize, this.ySize, mouseButton) && flag;
    }

    public boolean charTyped(char codePoint, int modifiers) {
        return this.recipeGui.charTyped(codePoint, modifiers) || super.charTyped(codePoint, modifiers);
    }

    public void recipesUpdated() {
        this.recipeGui.recipesUpdated();
    }

    public RecipeBookGui getRecipeGui() {
        return this.recipeGui;
    }

    public void onClose() {
        this.recipeGui.removed();
        super.onClose();
    }

    @OnlyIn(Dist.CLIENT)
    class ConfirmButton extends GemForgeScreen.Button {
        public ConfirmButton(int x, int y, GemForgeScreen screen) {
            super(x, y, screen);
        }

        @Override
        protected void func_230454_a_(MatrixStack p_230454_1_) {
        }

        public void onPress() {
            if (GemForgeScreen.this.container.isRecipeValid()) {
                MMNetworkHandler.sendToServer(new UpdateGemForgePacket(true));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class Button extends AbstractButton {
        private final GemForgeScreen screen;
        private final int xOffset;
        private final int yOffset;

        protected Button(int x, int y, GemForgeScreen screen) {
            super(x, y, 20, 20, StringTextComponent.EMPTY);
            this.screen = screen;
            this.xOffset = x;
            this.yOffset = y;
            this.x = this.xOffset + this.screen.guiLeft;
            this.y = this. yOffset + this.screen.guiTop;
        }

        public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bindTexture(GemForgeScreen.GUI_TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 165;
            int j = 0;
            if (this.screen.container.isForgeActive()) {
                j += this.width;
            } else if (!this.screen.container.isRecipeValid()) {
                j += this.width * 2;
            } else if (this.isHovered()) {
                j += this.width * 3;
            }

            this.x = this.xOffset + this.screen.guiLeft;
            this.y = this. yOffset + this.screen.guiTop;

            this.active = this.screen.container.isRecipeValid() && !this.screen.container.isForgeActive();

            this.blit(matrixStack, this.x, this.y, j, i, this.width, this.height);
            this.screen.itemRenderer.renderItemIntoGUI(new ItemStack(Items.LAVA_BUCKET), this.x + 2, this.y + 2);
            this.func_230454_a_(matrixStack);
        }

        protected abstract void func_230454_a_(MatrixStack p_230454_1_);
    }
}
