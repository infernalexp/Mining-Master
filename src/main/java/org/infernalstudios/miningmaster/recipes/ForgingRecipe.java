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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.infernalstudios.miningmaster.init.MMRecipes;

import javax.annotation.Nullable;

public class ForgingRecipe implements IForgingRecipe {
    private final ResourceLocation recipeId;
    private final Ingredient catalyst;
    private final NonNullList<Ingredient> recipeGems;
    private final ItemStack result;

    public ForgingRecipe(ResourceLocation recipeId, Ingredient catalyst, NonNullList<Ingredient> recipeGems, ItemStack result) {
        this.recipeId = recipeId;
        this.catalyst = catalyst;
        this.recipeGems = recipeGems;
        this.result = result;
    }

    @Override
    public String getGroup() {
        return "Forging";
    }

    public boolean matches(IInventory inv, World worldIn) {
        boolean catalystMatches = this.catalyst.test(inv.getStackInSlot(9));

        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for(int j = 0; j < 9; ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
            }
        }

        return catalystMatches && i == this.recipeGems.size() && net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.recipeGems) != null;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        ItemStack itemstack = this.result.copy();
        CompoundNBT compoundnbt = inv.getStackInSlot(0).getTag();
        if (compoundnbt != null) {
            itemstack.setTag(compoundnbt.copy());
        }

        return itemstack;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return MMRecipes.FORGING_RECIPE.get();
    }

    public static ItemStack deserializeItem(JsonObject object) {
        String s = JSONUtils.getString(object, "item");
        Item item = Registry.ITEM.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown item '" + s + "'");
        });
        if (object.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        } else {
            int i = JSONUtils.getInt(object, "count", 1);
            return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(object, true);
        }
    }

    public static class ForgingRecipeType implements IRecipeType<ForgingRecipe> {
        @Override
        public String toString() {
            return ForgingRecipe.TYPE_ID.toString();
        }
    }

    public static class ForgingRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ForgingRecipe> {

        @Override
        public ForgingRecipe read(ResourceLocation recipeId, JsonObject json) {
            NonNullList<Ingredient> gemList = readIngredients(JSONUtils.getJsonArray(json, "gems"));

            if (gemList.isEmpty()) {
                throw new JsonParseException("No gems for forging recipe");
            } else if (gemList.size() > 8) {
                throw new JsonParseException("Too many gems for forging recipe the max is 8");
            } else {
                ItemStack result = ForgingRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
                Ingredient catalyst = Ingredient.deserialize(JSONUtils.getJsonObject(json, "catalyst"));
                return new ForgingRecipe(recipeId, catalyst, gemList, result);
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < ingredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.deserialize(ingredientArray.get(i));
                if (!ingredient.hasNoMatchingItems()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        @Nullable
        @Override
        public ForgingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient catalyst = Ingredient.read(buffer);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> gemList = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < gemList.size(); ++j) {
                gemList.set(j, Ingredient.read(buffer));
            }

            ItemStack result = buffer.readItemStack();
            return new ForgingRecipe(recipeId, catalyst, gemList, result);
        }

        @Override
        public void write(PacketBuffer buffer, ForgingRecipe recipe) {
            recipe.catalyst.write(buffer);
            buffer.writeVarInt(recipe.recipeGems.size());

            for(Ingredient ingredient : recipe.recipeGems) {
                ingredient.write(buffer);
            }

            buffer.writeItemStack(recipe.result);
        }
    }
}
