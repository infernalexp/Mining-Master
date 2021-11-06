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
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.infernalstudios.miningmaster.init.MMEnchantments;

import java.util.Random;
import java.util.UUID;

public class HeartfeltEnchantment extends Enchantment {

    public HeartfeltEnchantment(Rarity rarityIn, EquipmentSlotType... slots) {
        super(rarityIn, EnchantmentType.ARMOR, slots);
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
        return 2;
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

    @Override
    public void onUserHurt(LivingEntity user, Entity attacker, int level) {
        Iterable<ItemStack> equipment = user.getEquipmentAndArmor();
        for (ItemStack item : equipment) {
            ListNBT nbtList = item.getEnchantmentTagList();
            for (int i = 0; i < nbtList.size(); i++) {
                CompoundNBT idTag = nbtList.getCompound(i);
                if (idTag.getString("id").equals(MMEnchantments.HEARTFELT.getId().toString())) {
                    item.attemptDamageItem(idTag.getInt("lvl"), new Random(), null);
                }
            }
        }
        super.onUserHurt(user, attacker, level);
    }

    @SubscribeEvent
    public static void onItemAttributeModifierCalculate(ItemAttributeModifierEvent event) {
        ItemStack itemStack = event.getItemStack();
        EquipmentSlotType equipmentSlotType = null;
        if (itemStack.getItem() instanceof ArmorItem) {
            ArmorItem armorItem = (ArmorItem) itemStack.getItem();
            equipmentSlotType = armorItem.getEquipmentSlot();
        }

        if (equipmentSlotType != null && event.getSlotType().equals(equipmentSlotType)) {
            ListNBT nbtList = itemStack.getEnchantmentTagList();
            for (int i = 0; i < nbtList.size(); i++) {
                CompoundNBT idTag = nbtList.getCompound(i);
                if (idTag.getString("id").equals(MMEnchantments.HEARTFELT.getId().toString())) {
                    if (equipmentSlotType.equals(EquipmentSlotType.HEAD)) {
                        event.addModifier(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("ccac859c-0311-43d4-b254-572d7846a5be"), "heartfelt", 2.0D * idTag.getInt("lvl"), AttributeModifier.Operation.ADDITION));
                    } else if (equipmentSlotType.equals(EquipmentSlotType.CHEST)) {
                        event.addModifier(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("9dcfe3cf-7d9a-41c4-90de-84ff86b8b7c3"), "heartfelt", 2.0D * idTag.getInt("lvl"), AttributeModifier.Operation.ADDITION));
                    } else if (equipmentSlotType.equals(EquipmentSlotType.LEGS)) {
                        event.addModifier(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("24572f77-4a5c-44a1-b6fa-09fc5da661b8"), "heartfelt", 2.0D * idTag.getInt("lvl"), AttributeModifier.Operation.ADDITION));
                    } else {
                        event.addModifier(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("031a1eac-7726-46d6-87d7-cec65a66186b"), "heartfelt", 2.0D * idTag.getInt("lvl"), AttributeModifier.Operation.ADDITION));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUnequip(LivingEvent.LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity.getHealth() > livingEntity.getMaxHealth()) {
            livingEntity.setHealth(livingEntity.getMaxHealth());
        }
    }
}
