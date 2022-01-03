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

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.infernalstudios.miningmaster.access.LivingEntityAccess;
import org.infernalstudios.miningmaster.init.MMEnchantments;

import java.util.Random;

public class GraceEnchantment extends Enchantment {

    public GraceEnchantment(Rarity rarityIn, EquipmentSlotType... slots) {
        super(rarityIn, EnchantmentType.ARMOR_CHEST, slots);
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
        return 5;
    }

    @Override
    public boolean canVillagerTrade() {
        return false;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return this.type.canEnchantItem(stack.getItem());
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
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
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        PlayerEntity playerEntity = null;

        if (livingEntity instanceof PlayerEntity) {
            playerEntity = (PlayerEntity) livingEntity;
        }

        Random rand = new Random();

        ItemStack stack = livingEntity.getItemStackFromSlot(EquipmentSlotType.CHEST);
        ListNBT nbtList = stack.getEnchantmentTagList();

        if (livingEntity.isInWater()) {
            for (int i = 0; i < nbtList.size(); i++) {
                CompoundNBT idTag = nbtList.getCompound(i);
                if (idTag.getString("id").equals(MMEnchantments.GRACE.getId().toString())) {
                    if (livingEntity.isPotionActive(Effects.DOLPHINS_GRACE) && playerEntity != null && !playerEntity.isCreative() && rand.nextInt(100) == 0) {
                        stack.damageItem(1, livingEntity, (onBroken) -> {
                            onBroken.sendBreakAnimation(EquipmentSlotType.CHEST);
                        });
                    }
                    if (((LivingEntityAccess) livingEntity).getGraceRecharged()) {
                        livingEntity.addPotionEffect(new EffectInstance(Effects.DOLPHINS_GRACE, 120 * idTag.getInt("lvl")));
                        ((LivingEntityAccess) livingEntity).setGraceRecharged(false);
                    }
                }
            }
        } else if (!livingEntity.isInWater() && livingEntity.isOnGround() && !((LivingEntityAccess) livingEntity).getGraceRecharged()) {
            ((LivingEntityAccess) livingEntity).setGraceRecharged(true);
        }
    }

}
