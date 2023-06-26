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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.infernalstudios.miningmaster.init.MMEnchantments;

import java.util.Random;
import java.util.UUID;

public class HeartfeltEnchantment extends Enchantment {

    public HeartfeltEnchantment(Rarity rarityIn, EquipmentSlot... slots) {
        super(rarityIn, EnchantmentCategory.ARMOR, slots);
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
        return 2;
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
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity user = event.getEntityLiving();

        Iterable<ItemStack> equipment = user.getAllSlots();
        for (ItemStack item : equipment) {
            ListTag nbtList = item.getEnchantmentTags();
            for (int i = 0; i < nbtList.size(); i++) {
                CompoundTag idTag = nbtList.getCompound(i);
                if (idTag.getString("id").equals(MMEnchantments.HEARTFELT.getId().toString())) {
                    item.hurt(idTag.getInt("lvl"), new Random(), null);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemAttributeModifierCalculate(ItemAttributeModifierEvent event) {
        ItemStack itemStack = event.getItemStack();
        EquipmentSlot equipmentSlotType = null;
        if (itemStack.getItem() instanceof ArmorItem) {
            ArmorItem armorItem = (ArmorItem) itemStack.getItem();
            equipmentSlotType = armorItem.getSlot();
        }

        if (equipmentSlotType != null && event.getSlotType().equals(equipmentSlotType)) {
            ListTag nbtList = itemStack.getEnchantmentTags();
            for (int i = 0; i < nbtList.size(); i++) {
                CompoundTag idTag = nbtList.getCompound(i);
                if (idTag.getString("id").equals(MMEnchantments.HEARTFELT.getId().toString())) {
                    if (equipmentSlotType.equals(EquipmentSlot.HEAD)) {
                        event.addModifier(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("ccac859c-0311-43d4-b254-572d7846a5be"), "heartfelt", 2.0D * idTag.getInt("lvl"), AttributeModifier.Operation.ADDITION));
                    } else if (equipmentSlotType.equals(EquipmentSlot.CHEST)) {
                        event.addModifier(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("9dcfe3cf-7d9a-41c4-90de-84ff86b8b7c3"), "heartfelt", 2.0D * idTag.getInt("lvl"), AttributeModifier.Operation.ADDITION));
                    } else if (equipmentSlotType.equals(EquipmentSlot.LEGS)) {
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
