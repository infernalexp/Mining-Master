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

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.infernalstudios.miningmaster.init.MMContainerTypes;
import org.infernalstudios.miningmaster.recipes.GemForgeServerRecipePlacer;

import javax.annotation.Nonnull;

public class GemForgeContainer extends RecipeBookMenu<Container> {
    private final ItemStackHandler forgeInventory;
    protected final Level world;
    public ContainerData forgeData;

    public GemForgeContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new ItemStackHandler(10), new SimpleContainerData(4));
    }

    public GemForgeContainer(int id, Inventory playerInventory, ItemStackHandler inventory, ContainerData forgeData) {
        super(MMContainerTypes.GEM_FORGE_CONTAINER.get(), id);
        this.forgeData = forgeData;
        this.forgeInventory = inventory;
        this.world = playerInventory.player.level();
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

        this.addDataSlots(forgeData);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handlePlacement(boolean placeAll, Recipe<?> recipe, ServerPlayer player) {
        (new GemForgeServerRecipePlacer<>(this)).place(player, (Recipe<Container>) recipe, placeAll);
    }

    public boolean stillValid(Player playerIn) {
        return true;
    }

    public void setData(int id, int data) {
        super.setData(id, data);
        this.broadcastChanges();
    }

    public void fillCraftSlotsStackedContents(StackedContents itemHelperIn) {
        if (this.forgeInventory instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible)this.forgeInventory).fillStackedContents(itemHelperIn);
        }
    }

    public void clearCraftingContent() {
        for(int i = 0; i < 10; i++) {
            this.forgeInventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public boolean recipeMatches(Recipe<? super Container> recipeIn) {
        SimpleContainer inventory = new SimpleContainer(10);
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            inventory.setItem(i, this.forgeInventory.getStackInSlot(i));
        }
        return recipeIn.matches(inventory, this.world);
    }

    public int getResultSlotIndex() {
        return 9;
    }

    public int getGridWidth() {
        return 4;
    }

    public int getGridHeight() {
        return 3;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 10;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isForgeActive() {
        return this.forgeData.get(0) == 1;
    }

    @OnlyIn(Dist.CLIENT)
    public int getForgeTimeScaled() {
        int i = this.forgeData.get(3);
        if (i == 0) {
            i = 200;
        }

        return this.forgeData.get(2) * 20 / i;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isRecipeValid() {
        return this.forgeData.get(1) == 1;
    }

    public void setForgeActive(boolean active) {
        this.forgeData.set(0, active ? 1 : 0);
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.valueOf("GEM_FORGE");
    }

    @Override
    public boolean shouldMoveToInventory(int slot) {
        return slot != this.getResultSlotIndex();
    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 10) {
                if (!this.moveItemStackTo(itemstack1, 10, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 10, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
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
        public boolean mayPlace(ItemStack stack) {
            return true;
        }

        /**
         * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the
         * case of armor slots)
         */
        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int getMaxStackSize(@Nonnull ItemStack stack) {
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
        public boolean mayPlace(ItemStack stack) {
            return true;
        }

        /**
         * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the
         * case of armor slots)
         */
        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int getMaxStackSize(@Nonnull ItemStack stack) {
            return 1;
        }
    }
}
