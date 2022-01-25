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

package org.infernalstudios.miningmaster.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.infernalstudios.miningmaster.init.MMContainerTypes;
import org.infernalstudios.miningmaster.init.MMTags;

import javax.annotation.Nonnull;

public class GemForgeContainer extends RecipeBookContainer<IInventory> {
    private final ItemStackHandler forgeInventory;
    protected final World world;
    public Boolean active;

    public GemForgeContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new ItemStackHandler(10), false);
    }

    public GemForgeContainer(int id, PlayerInventory playerInventory, ItemStackHandler inventory, Boolean active) {
        super(MMContainerTypes.GEM_FORGE_CONTAINER.get(), id);
        this.active = active;
        this.forgeInventory = inventory;
        this.world = playerInventory.player.world;
        this.addSlot(new GemSlot(inventory, 0, 44, 53));
        this.addSlot(new GemSlot(inventory, 1, 44, 35));
        this.addSlot(new GemSlot(inventory, 2, 44, 17));
        this.addSlot(new GemSlot(inventory, 3, 62, 17));
        this.addSlot(new GemSlot(inventory, 4, 80, 17));
        this.addSlot(new GemSlot(inventory, 5, 98, 17));
        this.addSlot(new GemSlot(inventory, 6, 116, 17));
        this.addSlot(new GemSlot(inventory, 7, 116, 35));
        this.addSlot(new GemSlot(inventory, 8, 116, 53));

        this.addSlot(new CatalystSlot(inventory, 9, 80, 43));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public void updateProgressBar(int id, int data) {
        super.updateProgressBar(id, data);
        this.detectAndSendChanges();
    }

    public void fillStackedContents(RecipeItemHelper itemHelperIn) {
        if (this.forgeInventory instanceof IRecipeHelperPopulator) {
            ((IRecipeHelperPopulator)this.forgeInventory).fillStackedContents(itemHelperIn);
        }
    }

    public void clear() {
        for(int i = 0; i < 10; i++) {
            this.forgeInventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public boolean matches(IRecipe<? super IInventory> recipeIn) {
        Inventory inventory = new Inventory();
        for(int i = 0; i < 10; i++) {
            inventory.setInventorySlotContents(i, this.forgeInventory.getStackInSlot(i));
        }
        return recipeIn.matches(inventory, this.world);
    }

    public int getOutputSlot() {
        return 9;
    }

    public int getWidth() {
        return 3;
    }

    public int getHeight() {
        return 3;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 10;
    }

    @Override
    public RecipeBookCategory func_241850_m() {
        return RecipeBookCategory.FURNACE;
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < 10) {
                if (!this.mergeItemStack(itemstack1, 10, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 10, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    class GemSlot extends SlotItemHandler {
        public GemSlot(ItemStackHandler inventoryIn, int index, int xIn, int yIn) {
            super(inventoryIn, index, xIn, yIn);
        }

        /**
         * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
         */
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem().isIn(MMTags.Items.GEMS);
        }

        /**
         * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the
         * case of armor slots)
         */
        @Override
        public int getSlotStackLimit() {
            return 1;
        }

        @Override
        public int getItemStackLimit(@Nonnull ItemStack stack) {
            return 1;
        }
    }

    class CatalystSlot extends SlotItemHandler {
        public CatalystSlot(ItemStackHandler inventoryIn, int index, int xIn, int yIn) {
            super(inventoryIn, index, xIn, yIn);
        }

        /**
         * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
         */
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem().isIn(MMTags.Items.CATALYSTS);
        }

        /**
         * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the
         * case of armor slots)
         */
        @Override
        public int getSlotStackLimit() {
            return 1;
        }

        @Override
        public int getItemStackLimit(@Nonnull ItemStack stack) {
            return 1;
        }
    }
}
