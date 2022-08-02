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

package org.infernalstudios.miningmaster.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Supplier;

import net.minecraft.world.item.Item.Properties;

public class GemHoeItem extends HoeItem {
    private final Ingredient repairItems;
    private final Pair<Supplier<Enchantment>, Integer>[] enchantments;

    @SafeVarargs
    public GemHoeItem(Tier itemTier, Ingredient repairItems, int attackDamage, float attackSpeed, Properties properties, Pair<Supplier<Enchantment>, Integer>... enchantments) {
        super(itemTier, attackDamage, attackSpeed, properties);
        this.repairItems = repairItems;
        this.enchantments = enchantments;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.repairItems.test(repair);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (allowedIn(group)) {
            ItemStack itemStack = new ItemStack(this);

            for (Pair<Supplier<Enchantment>, Integer> enchantmentPair : enchantments) {
                itemStack.enchant(enchantmentPair.getFirst().get(), enchantmentPair.getSecond());
            }

            items.add(itemStack);
        }
    }
}
