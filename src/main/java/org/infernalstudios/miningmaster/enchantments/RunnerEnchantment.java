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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.infernalstudios.miningmaster.init.MMEnchantments;

import java.util.Random;
import java.util.UUID;

public class RunnerEnchantment extends Enchantment {

    public RunnerEnchantment(Rarity rarityIn, EquipmentSlot... slots) {
        super(rarityIn, EnchantmentCategory.ARMOR_FEET, slots);
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
        return 3;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return this.category.canEnchant(stack.getItem()) || stack.canApplyAtEnchantingTable(this);
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
    public static void onItemAttributeModifierCalculate(ItemAttributeModifierEvent event) {
        if (event.getSlotType().equals(EquipmentSlot.FEET)) {
            ItemStack itemStack = event.getItemStack();
            ListTag nbtList = itemStack.getEnchantmentTags();
            for (int i = 0; i < nbtList.size(); i++) {
                CompoundTag idTag = nbtList.getCompound(i);
                if (idTag.getString("id").equals(MMEnchantments.RUNNER.getId().toString())) {
                    event.addModifier(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("047c6331-d618-4cc8-99c0-328e42d33b5e"), "runner", 0.2D * idTag.getInt("lvl"), AttributeModifier.Operation.MULTIPLY_TOTAL));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();
        Player playerEntity = null;

        if (livingEntity instanceof Player) {
            playerEntity = (Player) livingEntity;
            if (playerEntity.isCreative()) {
                return;
            }
        }
        Random rand = new Random();

        if (playerEntity != null && playerEntity.isSprinting()) {
            ItemStack stack = event.getEntity().getItemBySlot(EquipmentSlot.FEET);
            ListTag nbtList = stack.getEnchantmentTags();

            for (int i = 0; i < nbtList.size(); i++) {
                CompoundTag idTag = nbtList.getCompound(i);
                if (idTag.getString("id").equals(MMEnchantments.RUNNER.getId().toString())) {
                    if (rand.nextInt(100) == 0) {
                        stack.hurtAndBreak(1, livingEntity, (onBroken) -> {
                            onBroken.broadcastBreakEvent(EquipmentSlot.FEET);
                        });
                    }
                }
            }
        }
    }

}
