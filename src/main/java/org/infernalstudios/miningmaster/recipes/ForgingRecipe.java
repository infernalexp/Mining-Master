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
import com.mojang.datafixers.util.Pair;
import net.minecraft.enchantment.Enchantment;
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
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.infernalstudios.miningmaster.init.MMRecipes;

import javax.annotation.Nullable;

public class ForgingRecipe implements IForgingRecipe {
    private final ResourceLocation recipeId;
    private final Ingredient catalyst;
    private final NonNullList<Ingredient> recipeGems;
    private final NonNullList<Pair<Enchantment,Integer>> enchantments;
    private final ItemStack result;

    public ForgingRecipe(ResourceLocation recipeId, Ingredient catalyst, NonNullList<Ingredient> recipeGems, NonNullList<Pair<Enchantment,Integer>> enchantments, ItemStack result) {
        this.recipeId = recipeId;
        this.catalyst = catalyst;
        this.recipeGems = recipeGems;
        this.enchantments = enchantments;
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

        for (Pair<Enchantment, Integer> enchantment : this.enchantments) {
            itemstack.addEnchantment(enchantment.getFirst(), enchantment.getSecond());
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
            NonNullList<Pair<Enchantment,Integer>> enchantmentList = readEnchantments(JSONUtils.getJsonArray(json, "enchantments"));

            if (gemList.isEmpty()) {
                throw new JsonParseException("No gems for forging recipe");
            } else if (gemList.size() > 9) {
                throw new JsonParseException("Too many gems for forging recipe the max is 9");
            } else {
                ItemStack result = ForgingRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
                Ingredient catalyst = Ingredient.deserialize(JSONUtils.getJsonObject(json, "catalyst"));

                return new ForgingRecipe(recipeId, catalyst, gemList, enchantmentList, result);
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

        private static NonNullList<Pair<Enchantment,Integer>> readEnchantments(JsonArray enchantmentArray) {
            NonNullList<Pair<Enchantment,Integer>> enchantments = NonNullList.create();

            for(int i = 0; i < enchantmentArray.size(); ++i) {
                Enchantment enchantment = parseEnchantment(JSONUtils.getJsonObject(enchantmentArray.get(i),"enchantment"));
                int lvl = parseEnchantmentLevel(JSONUtils.getJsonObject(enchantmentArray.get(i), "lvl"));

                enchantments.add(new Pair<>(enchantment, lvl));
            }

            return enchantments;
        }

        private static Enchantment parseEnchantment(JsonObject object) {
            if (object.isJsonArray()) {
                throw new JsonSyntaxException("Expected object to be a single Enchantment");
            }
            Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(JSONUtils.getString(object, "enchantment")));

            if (enchantment == null) {
                throw new JsonSyntaxException("No valid Enchantment name supplied");
            }

            return enchantment;
        }

        private static int parseEnchantmentLevel(JsonObject object) {
            if (object.isJsonArray()) {
                throw new JsonSyntaxException("Expected object to be a single integer");
            }
            return JSONUtils.getInt(object, "lvl");
        }

        @Nullable
        @Override
        public ForgingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient catalyst = Ingredient.read(buffer);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> gemList = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < gemList.size(); j++) {
                gemList.set(j, Ingredient.read(buffer));
            }

            int k = buffer.readVarInt();
            NonNullList<Pair<Enchantment,Integer>> enchantmentList = NonNullList.create();

            for (int j = 0; j < k; j++) {
                Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(buffer.readResourceLocation());
                if (enchantment != null) {
                    enchantmentList.add(new Pair<>(enchantment, buffer.readVarInt()));
                }
            }

            ItemStack result = buffer.readItemStack();
            return new ForgingRecipe(recipeId, catalyst, gemList, enchantmentList, result);
        }

        @Override
        public void write(PacketBuffer buffer, ForgingRecipe recipe) {
            recipe.catalyst.write(buffer);
            buffer.writeVarInt(recipe.recipeGems.size());

            for(Ingredient ingredient : recipe.recipeGems) {
                ingredient.write(buffer);
            }

            buffer.writeVarInt(recipe.enchantments.size());
            for(Pair<Enchantment,Integer> enchantment : recipe.enchantments) {
                buffer.writeResourceLocation(enchantment.getFirst().getRegistryName());
                buffer.writeVarInt(enchantment.getSecond());
            }

            buffer.writeItemStack(recipe.result);
        }
    }
}
