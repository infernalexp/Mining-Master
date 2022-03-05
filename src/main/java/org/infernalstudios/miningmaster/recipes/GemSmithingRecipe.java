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
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
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
import java.util.Map;

public class GemSmithingRecipe extends SmithingRecipe implements IRecipe<IInventory> {
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
    public boolean matches(IInventory inv, World worldIn) {
        return !this.blacklist.test(inv.getStackInSlot(0)) && this.gem.isItemEqual(inv.getStackInSlot(1)) && getCraftingResult(inv) != null;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        ItemStack itemstack = inv.getStackInSlot(0).copy();
        CompoundNBT compoundnbt = inv.getStackInSlot(0).getTag();
        if (compoundnbt != null) {
            itemstack.setTag(compoundnbt.copy());
        }

        boolean itemEnchanted = false;

        outerLoop:
        for (Enchantment enchantment : enchantments) {
            if (enchantment.canApply(itemstack) && areEnchantsCompatible(itemstack, enchantment)) {
                ListNBT nbtList = itemstack.getEnchantmentTagList();

                for (int i = 0; i < nbtList.size(); i++) {
                    CompoundNBT idTag = nbtList.getCompound(i);

                    if (idTag.getString("id").equals(enchantment.getRegistryName().toString())) {
                        int targetLevel = idTag.getInt("lvl") + 1;
                        if (targetLevel > enchantment.getMaxLevel()) {
                            break outerLoop;
                        }
                        itemEnchanted = true;
                        nbtList.remove(i);
                        itemstack.addEnchantment(enchantment, targetLevel);
                        break outerLoop;
                    }
                }

                itemEnchanted = true;
                itemstack.addEnchantment(enchantment, 1);
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
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return MMRecipes.GEM_SMITHING_RECIPE.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return IRecipeType.SMITHING;
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

    public static class GemSmithingRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GemSmithingRecipe> {

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
            Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(JSONUtils.getString(element, "enchantment")));

            if (enchantment == null) {
                throw new JsonSyntaxException("No valid Enchantment name supplied");
            }

            return enchantment;
        }

        @Override
        public GemSmithingRecipe read(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "blacklist"));
            ItemStack gem = GemSmithingRecipe.deserializeItem(JSONUtils.getJsonObject(json, "gem"));
            NonNullList<Enchantment> enchantmentList = readEnchantments(JSONUtils.getJsonArray(json, "enchantments"));

            if (enchantmentList.isEmpty()) {
                throw new JsonParseException("No enchantments for smithing recipe");
            }

            return new GemSmithingRecipe(recipeId, ingredient, gem, enchantmentList);
        }

        @Nullable
        @Override
        public GemSmithingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack gem = buffer.readItemStack();

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
        public void write(PacketBuffer buffer, GemSmithingRecipe recipe) {
            recipe.blacklist.write(buffer);
            buffer.writeItemStack(recipe.gem);

            buffer.writeVarInt(recipe.enchantments.size());
            for(Enchantment enchantment : recipe.enchantments) {
                buffer.writeResourceLocation(enchantment.getRegistryName());
            }
        }
    }
}
