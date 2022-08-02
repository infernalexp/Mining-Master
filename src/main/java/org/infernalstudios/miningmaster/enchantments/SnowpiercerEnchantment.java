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

package org.infernalstudios.miningmaster.enchantments;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.infernalstudios.miningmaster.init.MMEnchantments;
import org.infernalstudios.miningmaster.init.MMTags;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class SnowpiercerEnchantment extends Enchantment {

    public SnowpiercerEnchantment(Rarity rarityIn, EquipmentSlot... slots) {
        super(rarityIn, EnchantmentCategory.ARMOR_LEGS, slots);
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return this.category.canEnchant(stack.getItem());
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if (!livingEntity.level.getBlockState(livingEntity.blockPosition().below()).is(MMTags.Blocks.SNOWPIERCER_BLOCKS) && !livingEntity.level.getBlockState(livingEntity.blockPosition()).is(MMTags.Blocks.SNOWPIERCER_BLOCKS)) {
            return;
        }

        ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
        ListTag nbtList = stack.getEnchantmentTags();

        for (int i = 0; i < nbtList.size(); i++) {
            CompoundTag idTag = nbtList.getCompound(i);
            if (idTag.getString("id").equals(MMEnchantments.SNOWPIERCER.getId().toString())) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 1));
            }
        }
    }

}
