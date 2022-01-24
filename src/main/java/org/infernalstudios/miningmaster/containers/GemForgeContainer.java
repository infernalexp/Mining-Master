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
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.infernalstudios.miningmaster.init.MMContainerTypes;
import org.infernalstudios.miningmaster.init.MMTags;

public class GemForgeContainer extends RecipeBookContainer<IInventory> {
    private final ItemStackHandler forgeInventory;
    protected final World world;

    public GemForgeContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new ItemStackHandler(9));
    }

    public GemForgeContainer(int id, PlayerInventory playerInventory, ItemStackHandler inventory) {
        super(MMContainerTypes.GEM_FORGE_CONTAINER.get(), id);
        this.forgeInventory = inventory;
        this.world = playerInventory.player.world;
        this.addSlot(new GemSlot(inventory, 0, 50, 10));
        this.addSlot(new GemSlot(inventory, 1, 50, 30));
        this.addSlot(new GemSlot(inventory, 2, 50, 50));
        this.addSlot(new GemSlot(inventory, 3, 50, 70));
        this.addSlot(new GemSlot(inventory, 4, 100, 10));
        this.addSlot(new GemSlot(inventory, 5, 100, 30));
        this.addSlot(new GemSlot(inventory, 6, 100, 50));
        this.addSlot(new GemSlot(inventory, 7, 100, 70));

        this.addSlot(new CatalystSlot(inventory, 8, 75, 40));

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
        for(int i = 0; i < 9; i++) {
            this.forgeInventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public boolean matches(IRecipe<? super IInventory> recipeIn) {
        Inventory inventory = new Inventory();
        for(int i = 0; i < 9; i++) {
            inventory.setInventorySlotContents(i, this.forgeInventory.getStackInSlot(i));
        }
        return recipeIn.matches(inventory, this.world);
    }

    public int getOutputSlot() {
        return 8;
    }

    public int getWidth() {
        return 3;
    }

    public int getHeight() {
        return 3;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 9;
    }

    @Override
    public RecipeBookCategory func_241850_m() {
        return RecipeBookCategory.FURNACE;
    }

    // TWEAK
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0) {
                if (this.hasRecipe(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    protected boolean hasRecipe(ItemStack stack) {
        return this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(stack), this.world).isPresent();
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
        public int getSlotStackLimit() {
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
        public int getSlotStackLimit() {
            return 1;
        }
    }
}
