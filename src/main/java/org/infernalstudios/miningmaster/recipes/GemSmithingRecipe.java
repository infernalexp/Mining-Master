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

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.infernalstudios.miningmaster.init.MMEnchantments;
import org.infernalstudios.miningmaster.init.MMItems;
import org.infernalstudios.miningmaster.init.MMRecipes;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GemSmithingRecipe extends SmithingRecipe implements IRecipe<IInventory> {
    public static final Map<Item, List<Enchantment>> GEM_ENCHANTMENTS = new HashMap<>();

    static {
        GEM_ENCHANTMENTS.put(MMItems.FIRE_RUBY.get(), Arrays.asList(Enchantments.FIRE_ASPECT, Enchantments.FLAME, Enchantments.FIRE_PROTECTION));
        GEM_ENCHANTMENTS.put(MMItems.ICE_SAPPHIRE.get(), Arrays.asList(Enchantments.FROST_WALKER, MMEnchantments.FREEZING.get()));
        GEM_ENCHANTMENTS.put(MMItems.SPIRIT_GARNET.get(), Arrays.asList(Enchantments.THORNS, Enchantments.LOYALTY, MMEnchantments.LEECHING.get()));
        GEM_ENCHANTMENTS.put(MMItems.HASTE_PERIDOT.get(), Arrays.asList(Enchantments.EFFICIENCY, Enchantments.LURE, Enchantments.QUICK_CHARGE));
        GEM_ENCHANTMENTS.put(MMItems.LUCKY_CITRINE.get(), Arrays.asList(Enchantments.FORTUNE, Enchantments.LUCK_OF_THE_SEA, Enchantments.LOOTING));
        GEM_ENCHANTMENTS.put(MMItems.DIVE_AQUAMARINE.get(), Arrays.asList(Enchantments.AQUA_AFFINITY, Enchantments.RIPTIDE));
        GEM_ENCHANTMENTS.put(MMItems.HEART_RHODONITE.get(), Arrays.asList());
        GEM_ENCHANTMENTS.put(MMItems.POWER_PYRITE.get(), Arrays.asList(Enchantments.SHARPNESS, Enchantments.PUNCH, Enchantments.IMPALING));
        GEM_ENCHANTMENTS.put(MMItems.KINETIC_OPAL.get(), Arrays.asList(Enchantments.BLAST_PROTECTION, MMEnchantments.SMELTING.get()));
        GEM_ENCHANTMENTS.put(MMItems.AIR_MALACHITE.get(), Arrays.asList(Enchantments.FEATHER_FALLING, Enchantments.RESPIRATION));
    }

    private final Ingredient base;
    private final ItemStack gem;
    private final ResourceLocation recipeId;

    public GemSmithingRecipe(ResourceLocation recipeId, Ingredient base, ItemStack gem) {
        super(recipeId, base, Ingredient.EMPTY, ItemStack.EMPTY);
        this.recipeId = recipeId;
        this.base = base;
        this.gem = gem;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return this.base.test(inv.getStackInSlot(0)) && this.gem.isItemEqual(inv.getStackInSlot(1)) && getCraftingResult(inv) != null;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        ItemStack itemstack = inv.getStackInSlot(0).copy();
        CompoundNBT compoundnbt = inv.getStackInSlot(0).getTag();
        if (compoundnbt != null) {
            itemstack.setTag(compoundnbt.copy());
        }

        List<Enchantment> gemEnchantments = GEM_ENCHANTMENTS.get(this.gem.getItem());

        boolean itemEnchanted = false;

        for (Enchantment enchantment : gemEnchantments) {
            if (itemstack.canApplyAtEnchantingTable(enchantment)) {
                ListNBT nbtList = itemstack.getEnchantmentTagList();
                boolean itemStackHasEnchantment = false;

                for (int i = 0; i < nbtList.size(); i++) {
                    CompoundNBT idTag = nbtList.getCompound(i);

                    if (idTag.getString("id").equals(enchantment.getRegistryName().toString())) {
                        itemStackHasEnchantment = true;
                        int targetLevel = idTag.getInt("lvl") + 1;
                        if (targetLevel > enchantment.getMaxLevel()) {
                            break;
                        }
                        itemEnchanted = true;
                        nbtList.remove(i);
                        itemstack.addEnchantment(enchantment, targetLevel);
                    }
                }

                if (!itemStackHasEnchantment) {
                    itemEnchanted = true;
                    itemstack.addEnchantment(enchantment, 1);
                }
            }
        }

        return itemEnchanted ? itemstack : null;
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


        @Override
        public GemSmithingRecipe read(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "base"));
            ItemStack gem = GemSmithingRecipe.deserializeItem(JSONUtils.getJsonObject(json, "gem"));
            return new GemSmithingRecipe(recipeId, ingredient, gem);
        }

        @Nullable
        @Override
        public GemSmithingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack gem = buffer.readItemStack();
            return new GemSmithingRecipe(recipeId, ingredient, gem);
        }

        @Override
        public void write(PacketBuffer buffer, GemSmithingRecipe recipe) {
            recipe.base.write(buffer);
            buffer.writeItemStack(recipe.gem);
        }
    }
}
