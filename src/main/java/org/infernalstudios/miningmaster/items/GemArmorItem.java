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
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import java.util.function.Supplier;

public class GemArmorItem extends ArmorItem {
    private final Ingredient repairItems;
    private final Pair<Supplier<Enchantment>, Integer>[] enchantments;

    public GemArmorItem(IArmorMaterial materialIn, Ingredient repairItems, EquipmentSlotType slot, Properties builderIn, Pair<Supplier<Enchantment>, Integer>... enchantments) {
        super(materialIn, slot, builderIn);
        this.repairItems = repairItems;
        this.enchantments = enchantments;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return this.repairItems.test(repair);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            ItemStack itemStack = new ItemStack(this);

            for (Pair<Supplier<Enchantment>, Integer> enchantmentPair : enchantments) {
                itemStack.addEnchantment(enchantmentPair.getFirst().get(), enchantmentPair.getSecond());
            }

            items.add(itemStack);
        }
    }
}
