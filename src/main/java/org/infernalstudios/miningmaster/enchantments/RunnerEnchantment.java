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

package org.infernalstudios.miningmaster.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.infernalstudios.miningmaster.init.MMEnchantments;

import java.util.UUID;

public class RunnerEnchantment extends Enchantment {

    public RunnerEnchantment(Rarity rarityIn, EquipmentSlotType... slots) {
        super(rarityIn, EnchantmentType.ARMOR_FEET, slots);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canVillagerTrade() {
        return false;
    }

    @Override
    public boolean canGenerateInLoot() {
        return false;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }

    @SubscribeEvent
    public static void onItemAttributeModifierCalculate(ItemAttributeModifierEvent event) {
        if (event.getSlotType().equals(EquipmentSlotType.FEET)) {
            ItemStack itemStack = event.getItemStack();
            ListNBT nbtList = itemStack.getEnchantmentTagList();
            for (int i = 0; i < nbtList.size(); i++) {
                CompoundNBT idTag = nbtList.getCompound(i);
                if (idTag.getString("id").equals(MMEnchantments.RUNNER.getId().toString())) {
                    event.addModifier(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("047c6331-d618-4cc8-99c0-328e42d33b5e"), "runner", 0.2D * idTag.getInt("lvl"), AttributeModifier.Operation.MULTIPLY_TOTAL));
                }
            }
        }
    }
}
