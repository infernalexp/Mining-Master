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
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
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

    public boolean matches(Container inv, Level worldIn) {
        boolean catalystMatches = this.catalyst.test(inv.getItem(9));

        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for(int j = 0; j < 9; ++j) {
            ItemStack itemstack = inv.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
            }
        }

        return catalystMatches && i == this.recipeGems.size() && net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.recipeGems) != null;
    }

    public ItemStack getDefaultedOutput() {
        ItemStack itemstack = this.result.copy();
        
        for (Pair<Enchantment, Integer> enchantment : this.enchantments) {
            itemstack.enchant(enchantment.getFirst(), enchantment.getSecond());
        }
        
        return itemstack;
    }

    @Override
    public ItemStack assemble(Container inv, RegistryAccess access) {
        ItemStack itemstack = this.result.copy();
        CompoundTag compoundnbt = inv.getItem(0).getTag();
        if (compoundnbt != null) {
            itemstack.setTag(compoundnbt.copy());
        }

        for (Pair<Enchantment, Integer> enchantment : this.enchantments) {
            itemstack.enchant(enchantment.getFirst(), enchantment.getSecond());
        }

        return itemstack;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.addAll(this.recipeGems);
        ingredients.add(this.catalyst);
        return ingredients;
    }

    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MMRecipes.FORGING_RECIPE.get();
    }

    public static ItemStack deserializeItem(JsonObject object) {
        String s = GsonHelper.getAsString(object, "item");
        Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(s));
        if (item == null) {
            throw new JsonSyntaxException("Unknown item '" + s + "'");
        }
        if (object.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        } else {
            int i = GsonHelper.getAsInt(object, "count", 1);
            return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(object, true);
        }
    }

    public static class ForgingRecipeType implements RecipeType<ForgingRecipe> {
        @Override
        public String toString() {
            return ForgingRecipe.TYPE_ID.toString();
        }
    }

    public static class ForgingRecipeSerializer implements RecipeSerializer<ForgingRecipe> {

        @Override
        public ForgingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            NonNullList<Ingredient> gemList = readIngredients(GsonHelper.getAsJsonArray(json, "gems"));
            NonNullList<Pair<Enchantment,Integer>> enchantmentList = NonNullList.create();

            if (json.has("enchantments")) {
                   enchantmentList = readEnchantments(GsonHelper.getAsJsonArray(json, "enchantments"));
            }

            if (gemList.isEmpty()) {
                throw new JsonParseException("No gems for forging recipe");
            } else if (gemList.size() > 9) {
                throw new JsonParseException("Too many gems for forging recipe the max is 9");
            } else {
                Ingredient catalyst = Ingredient.EMPTY;

                ItemStack result = ForgingRecipe.deserializeItem(GsonHelper.getAsJsonObject(json, "result"));

                if (json.has("catalyst")) {
                    catalyst = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "catalyst"));
                }

                return new ForgingRecipe(recipeId, catalyst, gemList, enchantmentList, result);
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < ingredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        private static NonNullList<Pair<Enchantment,Integer>> readEnchantments(JsonArray enchantmentArray) {
            NonNullList<Pair<Enchantment,Integer>> enchantments = NonNullList.create();

            for(int i = 0; i < enchantmentArray.size(); ++i) {
                Enchantment enchantment = parseEnchantment(GsonHelper.convertToJsonObject(enchantmentArray.get(i),"enchantment"));
                int lvl = parseEnchantmentLevel(GsonHelper.convertToJsonObject(enchantmentArray.get(i), "lvl"));

                enchantments.add(new Pair<>(enchantment, lvl));
            }

            return enchantments;
        }

        private static Enchantment parseEnchantment(JsonObject object) {
            if (object.isJsonArray()) {
                throw new JsonSyntaxException("Expected object to be a single Enchantment");
            }
            Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "enchantment")));

            if (enchantment == null) {
                throw new JsonSyntaxException("No valid Enchantment name supplied");
            }

            return enchantment;
        }

        private static int parseEnchantmentLevel(JsonObject object) {
            if (object.isJsonArray()) {
                throw new JsonSyntaxException("Expected object to be a single integer");
            }
            return GsonHelper.getAsInt(object, "lvl");
        }

        @Nullable
        @Override
        public ForgingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient catalyst = Ingredient.fromNetwork(buffer);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> gemList = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < gemList.size(); j++) {
                gemList.set(j, Ingredient.fromNetwork(buffer));
            }

            int k = buffer.readVarInt();
            NonNullList<Pair<Enchantment,Integer>> enchantmentList = NonNullList.create();

            for (int j = 0; j < k; j++) {
                Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(buffer.readResourceLocation());
                if (enchantment != null) {
                    enchantmentList.add(new Pair<>(enchantment, buffer.readVarInt()));
                }
            }

            ItemStack result = buffer.readItem();
            return new ForgingRecipe(recipeId, catalyst, gemList, enchantmentList, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ForgingRecipe recipe) {
            recipe.catalyst.toNetwork(buffer);
            buffer.writeVarInt(recipe.recipeGems.size());

            for(Ingredient ingredient : recipe.recipeGems) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeVarInt(recipe.enchantments.size());
            for(Pair<Enchantment,Integer> enchantment : recipe.enchantments) {
                buffer.writeResourceLocation(ForgeRegistries.ENCHANTMENTS.getKey(enchantment.getFirst()));
                buffer.writeVarInt(enchantment.getSecond());
            }

            buffer.writeItem(recipe.result);
        }
    }
}
