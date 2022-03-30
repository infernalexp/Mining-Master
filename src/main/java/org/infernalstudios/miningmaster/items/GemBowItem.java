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
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class GemBowItem extends BowItem {
    private final Pair<Supplier<Enchantment>, Integer>[] enchantments;

    @SafeVarargs
    public GemBowItem(Properties builder,  Pair<Supplier<Enchantment>, Integer>... enchantments) {
        super(builder);
        this.enchantments = enchantments;
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

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity) entityLiving;
            boolean hasInfinity = playerentity.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = playerentity.findAmmo(stack);

            int ticksUsed = this.getUseDuration(stack) - timeLeft;
            ticksUsed = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, ticksUsed, !itemstack.isEmpty() || hasInfinity);

            if (ticksUsed < 0) {
                return;
            }

            if (!itemstack.isEmpty() || hasInfinity) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float velocity = getArrowVelocity(ticksUsed);
                if (!((double) velocity < 0.1D)) {
                    boolean flag1 = playerentity.abilities.isCreativeMode || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
                    if (!worldIn.isRemote) {
                        ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);

                        AbstractArrowEntity abstractArrowEntity = arrowitem.createArrow(worldIn, itemstack, playerentity);
                        abstractArrowEntity = customArrow(abstractArrowEntity);

                        abstractArrowEntity.setDirectionAndMovement(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, velocity * 4.5F, 1.0F);

                        // Reduce the damage to counteract increasing the arrow velocity
                        abstractArrowEntity.setDamage((abstractArrowEntity.getDamage() / 1.5D) + 0.1D);

                        if (velocity == 1.0F) {
                            abstractArrowEntity.setIsCritical(true);
                        }

                        int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                        if (powerLevel > 0) {
                            abstractArrowEntity.setDamage(abstractArrowEntity.getDamage() + (double) powerLevel * 0.5D + 0.5D);
                        }

                        int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                        if (punchLevel > 0) {
                            abstractArrowEntity.setKnockbackStrength(punchLevel);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            abstractArrowEntity.setFire(100);
                        }

                        stack.damageItem(1, playerentity, (player) -> {
                            player.sendBreakAnimation(playerentity.getActiveHand());
                        });
                        if (flag1 || playerentity.abilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            abstractArrowEntity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                        }

                        worldIn.addEntity(abstractArrowEntity);
                    }

                    worldIn.playSound(null, playerentity.getPosX(), playerentity.getPosY(), playerentity.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
                    if (!flag1 && !playerentity.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.inventory.deleteStack(itemstack);
                        }
                    }

                    playerentity.addStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }
}
