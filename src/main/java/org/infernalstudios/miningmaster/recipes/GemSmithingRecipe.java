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

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
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
        GEM_ENCHANTMENTS.put(MMItems.ICE_SAPPHIRE.get(), Arrays.asList(Enchantments.FROST_WALKER, MMEnchantments.FREEZING.get(), MMEnchantments.SNOWPIERCER.get()));
        GEM_ENCHANTMENTS.put(MMItems.SPIRIT_GARNET.get(), Arrays.asList(Enchantments.THORNS, Enchantments.LOYALTY, MMEnchantments.LEECHING.get()));
        GEM_ENCHANTMENTS.put(MMItems.HASTE_PERIDOT.get(), Arrays.asList(Enchantments.EFFICIENCY, Enchantments.LURE, Enchantments.QUICK_CHARGE));
        GEM_ENCHANTMENTS.put(MMItems.LUCKY_CITRINE.get(), Arrays.asList(Enchantments.FORTUNE, Enchantments.LUCK_OF_THE_SEA, Enchantments.LOOTING));
        GEM_ENCHANTMENTS.put(MMItems.DIVE_AQUAMARINE.get(), Arrays.asList(Enchantments.AQUA_AFFINITY, Enchantments.RIPTIDE, MMEnchantments.GRACE.get()));
        GEM_ENCHANTMENTS.put(MMItems.HEART_RHODONITE.get(), Arrays.asList(MMEnchantments.HEARTFELT.get()));
        GEM_ENCHANTMENTS.put(MMItems.POWER_PYRITE.get(), Arrays.asList(Enchantments.SHARPNESS, Enchantments.POWER, Enchantments.IMPALING, MMEnchantments.STONEBREAKER.get()));
        GEM_ENCHANTMENTS.put(MMItems.KINETIC_OPAL.get(), Arrays.asList(MMEnchantments.RUNNER.get(), MMEnchantments.SMELTING.get(), Enchantments.BLAST_PROTECTION));
        GEM_ENCHANTMENTS.put(MMItems.AIR_MALACHITE.get(), Arrays.asList(Enchantments.FEATHER_FALLING, Enchantments.RESPIRATION, MMEnchantments.FLOATATION.get(), MMEnchantments.KNIGHT_JUMP.get()));
    }

    private final Ingredient blacklist;
    private final ItemStack gem;
    private final ResourceLocation recipeId;

    public GemSmithingRecipe(ResourceLocation recipeId, Ingredient blacklist, ItemStack gem) {
        super(recipeId, blacklist, Ingredient.EMPTY, ItemStack.EMPTY);
        this.recipeId = recipeId;
        this.blacklist = blacklist;
        this.gem = gem;
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

        List<Enchantment> gemEnchantments = GEM_ENCHANTMENTS.get(this.gem.getItem());

        boolean itemEnchanted = false;

        outerLoop:
        for (Enchantment enchantment : gemEnchantments) {
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


        @Override
        public GemSmithingRecipe read(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "blacklist"));
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
            recipe.blacklist.write(buffer);
            buffer.writeItemStack(recipe.gem);
        }
    }
}
