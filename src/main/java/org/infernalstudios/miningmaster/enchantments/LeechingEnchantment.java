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
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.infernalstudios.miningmaster.init.MMEnchantments;

public class LeechingEnchantment extends Enchantment {
    public LeechingEnchantment(Rarity rarityIn, EquipmentSlotType... slots) {
        super(rarityIn, EnchantmentType.WEAPON, slots);
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
        return 1;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || super.canApply(stack);
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
    public static void onEntityDamage(LivingDamageEvent event) {
        Entity source = event.getSource().getTrueSource();
        float amount = event.getAmount();

        if (source instanceof LivingEntity) {
            LivingEntity livingSource = (LivingEntity) source;

            for (ItemStack stack: livingSource.getHeldEquipment()) {
                ListNBT nbtList = stack.getEnchantmentTagList();

                for (int i = 0; i < nbtList.size(); i++) {
                    CompoundNBT idTag = nbtList.getCompound(i);

                    if (idTag.getString("id").equals(MMEnchantments.LEECHING.getId().toString())) {
                        ((LivingEntity) source).heal(amount * 0.25F * idTag.getInt("lvl"));
                    }
                }
            }
        }
    }
}
