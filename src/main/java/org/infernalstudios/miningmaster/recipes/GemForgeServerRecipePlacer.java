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

package org.infernalstudios.miningmaster.recipes;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipePlacer;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.server.SPlaceGhostRecipePacket;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class GemForgeServerRecipePlacer<C extends IInventory> implements IRecipePlacer<Integer> {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected final RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
    protected PlayerInventory playerInventory;
    protected RecipeBookContainer<C> recipeBookContainer;

    public GemForgeServerRecipePlacer(RecipeBookContainer recipeBookContainer) {
        this.recipeBookContainer = recipeBookContainer;
    }

    protected void clear() {
        for(int i = 0; i < 10; i++) {
            this.giveToPlayer(i);
        }

        this.recipeBookContainer.clear();
    }

    protected void giveToPlayer(int slotIn) {
        ItemStack itemstack = this.recipeBookContainer.getSlot(slotIn).getStack();
        if (!itemstack.isEmpty()) {
            while (itemstack.getCount() > 0) {
                int i = this.playerInventory.storeItemStack(itemstack);
                if (i == -1) {
                    i = this.playerInventory.getFirstEmptyStack();
                }

                ItemStack itemstack1 = itemstack.copy();
                itemstack1.setCount(1);
                this.playerInventory.add(i, itemstack1);
                this.recipeBookContainer.getSlot(slotIn).decrStackSize(1);
                itemstack = this.recipeBookContainer.getSlot(slotIn).getStack();
            }

        }
    }

    public void place(ServerPlayerEntity player, @Nullable IRecipe<C> recipe, boolean placeAll) {
        if (recipe != null && player.getRecipeBook().isUnlocked(recipe)) {
            this.playerInventory = player.inventory;
            if (this.placeIntoInventory() || player.isCreative()) {
                this.recipeItemHelper.clear();
                player.inventory.accountStacks(this.recipeItemHelper);
                this.recipeBookContainer.fillStackedContents(this.recipeItemHelper);
                if (this.recipeItemHelper.canCraft(recipe, null)) {
                    this.tryPlaceRecipe(recipe, placeAll);
                } else {
                    this.clear();
                    player.connection.sendPacket(new SPlaceGhostRecipePacket(player.openContainer.windowId, recipe));
                }
                player.inventory.markDirty();
            }
        }
    }

    protected void tryPlaceRecipe(IRecipe<C> recipe, boolean placeAll) {
        boolean flag = this.recipeBookContainer.matches(recipe);
        int i = this.recipeItemHelper.getBiggestCraftableStack(recipe, null);
        if (flag) {
            for (int j = 0; j < 10; ++j) {
                ItemStack itemstack = this.recipeBookContainer.getSlot(j).getStack();
                if (!itemstack.isEmpty() && Math.min(i, itemstack.getMaxStackSize()) < itemstack.getCount() + 1) {
                    return;
                }
            }
        }

        int j1 = this.getMaxAmount(placeAll, i, flag);
        IntList intlist = new IntArrayList();
        if (this.recipeItemHelper.canCraft(recipe, intlist, j1)) {
            int k = j1;

            for(int l : intlist) {
                int i1 = RecipeItemHelper.unpack(l).getMaxStackSize();
                if (i1 < k) {
                    k = i1;
                }
            }

            if (this.recipeItemHelper.canCraft(recipe, intlist, k)) {
                this.clear();
                this.placeRecipe(this.recipeBookContainer.getWidth(), this.recipeBookContainer.getHeight(), this.recipeBookContainer.getOutputSlot(), recipe, intlist.iterator(), k);
            }
        }

    }

    protected int getMaxAmount(boolean placeAll, int maxPossible, boolean recipeMatches) {
        int i = 1;
        if (placeAll) {
            i = maxPossible;
        } else if (recipeMatches) {
            i = 64;

            for(int j = 0; j < 10; ++j) {
                if (j != this.recipeBookContainer.getOutputSlot()) {
                    ItemStack itemstack = this.recipeBookContainer.getSlot(j).getStack();
                    if (!itemstack.isEmpty() && i > itemstack.getCount()) {
                        i = itemstack.getCount();
                    }
                }
            }

            if (i < 64) {
                ++i;
            }
        }

        return i;
    }

    protected void consumeIngredient(Slot slotToFill, ItemStack ingredientIn) {
        int i = this.playerInventory.findSlotMatchingUnusedItem(ingredientIn);
        if (i != -1) {
            ItemStack itemstack = this.playerInventory.getStackInSlot(i).copy();
            if (!itemstack.isEmpty()) {
                if (itemstack.getCount() > 1) {
                    this.playerInventory.decrStackSize(i, 1);
                } else {
                    this.playerInventory.removeStackFromSlot(i);
                }

                itemstack.setCount(1);
                if (slotToFill.getStack().isEmpty()) {
                    slotToFill.putStack(itemstack);
                } else {
                    slotToFill.getStack().grow(1);
                }

            }
        }
    }

    @Override
    public void placeRecipe(int width, int height, int outputSlot, IRecipe recipe, Iterator ingredients, int maxAmount) {
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
    public void setSlotContents(Iterator<Integer> ingredients, int slotIn, int maxAmount, int y, int x) {
        ItemStack itemstack = RecipeItemHelper.unpack(ingredients.next());
        Slot slot;

        if (ingredients.hasNext()) {
            slot = this.recipeBookContainer.inventorySlots.get(slotIn);
        } else {
            slot = this.recipeBookContainer.inventorySlots.get(this.recipeBookContainer.getOutputSlot());
        }

        if (!itemstack.isEmpty()) {
            for (int i = 0; i < maxAmount; ++i) {
                this.consumeIngredient(slot, itemstack);
            }
        }
    }

    private boolean placeIntoInventory() {
        List<ItemStack> list = Lists.newArrayList();
        int i = this.getEmptyPlayerSlots();

        for(int j = 0; j < 10; j++) {
                ItemStack itemstack = this.recipeBookContainer.getSlot(j).getStack().copy();
                if (!itemstack.isEmpty()) {
                    int k = this.playerInventory.storeItemStack(itemstack);
                    if (k == -1 && list.size() <= i) {
                        for(ItemStack itemstack1 : list) {
                            if (itemstack1.isItemEqual(itemstack) && itemstack1.getCount() != itemstack1.getMaxStackSize() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
                                itemstack1.grow(itemstack.getCount());
                                itemstack.setCount(0);
                                break;
                            }
                        }

                        if (!itemstack.isEmpty()) {
                            if (list.size() >= i) {
                                return false;
                            }

                            list.add(itemstack);
                        }
                    } else if (k == -1) {
                        return false;
                    }
                }
        }

        return true;
    }

    private int getEmptyPlayerSlots() {
        int i = 0;

        for(ItemStack itemstack : this.playerInventory.mainInventory) {
            if (itemstack.isEmpty()) {
                ++i;
            }
        }

        return i;
    }
}
