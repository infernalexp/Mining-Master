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
import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class GemForgeServerRecipePlacer<C extends Container> implements PlaceRecipe<Integer> {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected final StackedContents recipeItemHelper = new StackedContents();
    protected Inventory playerInventory;
    protected RecipeBookMenu<C> recipeBookContainer;

    public GemForgeServerRecipePlacer(RecipeBookMenu<C> recipeBookContainer) {
        this.recipeBookContainer = recipeBookContainer;
    }

    protected void clear() {
        for(int i = 0; i < 10; i++) {
            this.giveToPlayer(i);
        }

        this.recipeBookContainer.clearCraftingContent();
    }

    protected void giveToPlayer(int slotIn) {
        ItemStack itemstack = this.recipeBookContainer.getSlot(slotIn).getItem();
        if (!itemstack.isEmpty()) {
            while (itemstack.getCount() > 0) {
                int i = this.playerInventory.getSlotWithRemainingSpace(itemstack);
                if (i == -1) {
                    i = this.playerInventory.getFreeSlot();
                }

                ItemStack itemstack1 = itemstack.copy();
                itemstack1.setCount(1);
                this.playerInventory.add(i, itemstack1);
                this.recipeBookContainer.getSlot(slotIn).remove(1);
                itemstack = this.recipeBookContainer.getSlot(slotIn).getItem();
            }

        }
    }

    public void place(ServerPlayer player, @Nullable Recipe<C> recipe, boolean placeAll) {
        if (recipe != null && player.getRecipeBook().contains(recipe)) {
            this.playerInventory = player.getInventory();
            if (this.placeIntoInventory() || player.isCreative()) {
                this.recipeItemHelper.clear();
                player.getInventory().fillStackedContents(this.recipeItemHelper);
                this.recipeBookContainer.fillCraftSlotsStackedContents(this.recipeItemHelper);
                if (this.recipeItemHelper.canCraft(recipe, null)) {
                    this.tryPlaceRecipe(recipe, placeAll);
                } else {
                    this.clear();
                    player.connection.send(new ClientboundPlaceGhostRecipePacket(player.containerMenu.containerId, recipe));
                }
                player.getInventory().setChanged();
            }
        }
    }

    protected void tryPlaceRecipe(Recipe<C> recipe, boolean placeAll) {
        boolean flag = this.recipeBookContainer.recipeMatches(recipe);
        int i = this.recipeItemHelper.getBiggestCraftableStack(recipe, null);
        if (flag) {
            for (int j = 0; j < 10; ++j) {
                ItemStack itemstack = this.recipeBookContainer.getSlot(j).getItem();
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
                int i1 = StackedContents.fromStackingIndex(l).getMaxStackSize();
                if (i1 < k) {
                    k = i1;
                }
            }

            if (this.recipeItemHelper.canCraft(recipe, intlist, k)) {
                this.clear();
                this.placeRecipe(this.recipeBookContainer.getGridWidth(), this.recipeBookContainer.getGridHeight(), this.recipeBookContainer.getResultSlotIndex(), recipe, intlist.iterator(), k);
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
                if (j != this.recipeBookContainer.getResultSlotIndex()) {
                    ItemStack itemstack = this.recipeBookContainer.getSlot(j).getItem();
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
            ItemStack itemstack = this.playerInventory.getItem(i).copy();
            if (!itemstack.isEmpty()) {
                if (itemstack.getCount() > 1) {
                    this.playerInventory.removeItem(i, 1);
                } else {
                    this.playerInventory.removeItemNoUpdate(i);
                }

                itemstack.setCount(1);
                if (slotToFill.getItem().isEmpty()) {
                    slotToFill.set(itemstack);
                } else {
                    slotToFill.getItem().grow(1);
                }

            }
        }
    }

    @Override
    public void placeRecipe(int width, int height, int outputSlot, Recipe<?> recipe, Iterator<Integer> ingredients, int maxAmount) {
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
    public void addItemToSlot(Iterator<Integer> ingredients, int slotIn, int maxAmount, int y, int x) {
        ItemStack itemstack = StackedContents.fromStackingIndex(ingredients.next());
        Slot slot;

        if (ingredients.hasNext()) {
            slot = this.recipeBookContainer.slots.get(slotIn);
        } else {
            slot = this.recipeBookContainer.slots.get(this.recipeBookContainer.getResultSlotIndex());
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
                ItemStack itemstack = this.recipeBookContainer.getSlot(j).getItem().copy();
                if (!itemstack.isEmpty()) {
                    int k = this.playerInventory.getSlotWithRemainingSpace(itemstack);
                    if (k == -1 && list.size() <= i) {
                        for(ItemStack itemstack1 : list) {
                            if (itemstack1.sameItem(itemstack) && itemstack1.getCount() != itemstack1.getMaxStackSize() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
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

        for(ItemStack itemstack : this.playerInventory.items) {
            if (itemstack.isEmpty()) {
                ++i;
            }
        }

        return i;
    }
}
