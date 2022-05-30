/*
 * Copyright 2022 Infernal Studios
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.infernalstudios.miningmaster.init.MMRecipes;

import javax.annotation.Nullable;
import java.util.Map;

public class GemSmithingRecipe extends UpgradeRecipe implements Recipe<Container> {
    private final Ingredient blacklist;
    private final ItemStack gem;
    private final NonNullList<Enchantment> enchantments;
    private final ResourceLocation recipeId;

    public GemSmithingRecipe(ResourceLocation recipeId, Ingredient blacklist, ItemStack gem, NonNullList<Enchantment> enchantments) {
        super(recipeId, blacklist, Ingredient.EMPTY, ItemStack.EMPTY);
        this.recipeId = recipeId;
        this.blacklist = blacklist;
        this.gem = gem;
        this.enchantments = enchantments;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return !this.blacklist.test(inv.getItem(0)) && this.gem.sameItem(inv.getItem(1)) && assemble(inv) != null;
    }

    @Override
    public ItemStack assemble(Container inv) {
        ItemStack itemstack = inv.getItem(0).copy();
        CompoundTag compoundnbt = inv.getItem(0).getTag();
        if (compoundnbt != null) {
            itemstack.setTag(compoundnbt.copy());
        }

        boolean itemEnchanted = false;

        outerLoop:
        for (Enchantment enchantment : enchantments) {
            if (enchantment.canEnchant(itemstack) && areEnchantsCompatible(itemstack, enchantment)) {
                ListTag nbtList = itemstack.getEnchantmentTags();

                for (int i = 0; i < nbtList.size(); i++) {
                    CompoundTag idTag = nbtList.getCompound(i);

                    if (idTag.getString("id").equals(enchantment.getRegistryName().toString())) {
                        int targetLevel = idTag.getInt("lvl") + 1;
                        if (targetLevel > enchantment.getMaxLevel()) {
                            break outerLoop;
                        }
                        itemEnchanted = true;
                        nbtList.remove(i);
                        itemstack.enchant(enchantment, targetLevel);
                        break outerLoop;
                    }
                }

                itemEnchanted = true;
                itemstack.enchant(enchantment, 1);
                break;
            }
        }
        return itemEnchanted ? itemstack : null;
    }

    private boolean areEnchantsCompatible(ItemStack itemStack, Enchantment enchant) {
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
        for (Enchantment e : map.keySet()) {
            if (enchant != e && !enchant.isCompatibleWith(e)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MMRecipes.GEM_SMITHING_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.SMITHING;
    }

    @Override
    public boolean isSpecial() {
        return true;
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

    public static class GemSmithingRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<GemSmithingRecipe> {

        private static NonNullList<Enchantment> readEnchantments(JsonArray enchantmentArray) {
            NonNullList<Enchantment> enchantments = NonNullList.create();

            for(int i = 0; i < enchantmentArray.size(); ++i) {
                Enchantment enchantment = parseEnchantment(enchantmentArray.get(i));
                enchantments.add(enchantment);
            }

            return enchantments;
        }

        private static Enchantment parseEnchantment(JsonElement element) {
            if (element.isJsonArray()) {
                throw new JsonSyntaxException("Expected object to be a single Enchantment");
            }
            Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation.tryParse(GsonHelper.convertToString(element, "enchantment")));

            if (enchantment == null) {
                throw new JsonSyntaxException("No valid Enchantment name supplied");
            }

            return enchantment;
        }

        @Override
        public GemSmithingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "blacklist"));
            ItemStack gem = GemSmithingRecipe.deserializeItem(GsonHelper.getAsJsonObject(json, "gem"));
            NonNullList<Enchantment> enchantmentList = readEnchantments(GsonHelper.getAsJsonArray(json, "enchantments"));

            if (enchantmentList.isEmpty()) {
                throw new JsonParseException("No enchantments for smithing recipe");
            }

            return new GemSmithingRecipe(recipeId, ingredient, gem, enchantmentList);
        }

        @Nullable
        @Override
        public GemSmithingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack gem = buffer.readItem();

            int k = buffer.readVarInt();
            NonNullList<Enchantment> enchantmentList = NonNullList.create();

            for (int j = 0; j < k; j++) {
                Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(buffer.readResourceLocation());
                if (enchantment != null) {
                    enchantmentList.add(enchantment);
                }
            }

            return new GemSmithingRecipe(recipeId, ingredient, gem, enchantmentList);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, GemSmithingRecipe recipe) {
            recipe.blacklist.toNetwork(buffer);
            buffer.writeItem(recipe.gem);

            buffer.writeVarInt(recipe.enchantments.size());
            for(Enchantment enchantment : recipe.enchantments) {
                buffer.writeResourceLocation(enchantment.getRegistryName());
            }
        }
    }
}
