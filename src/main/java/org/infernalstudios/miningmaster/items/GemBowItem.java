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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

import net.minecraft.world.item.Item.Properties;

public class GemBowItem extends BowItem {
    private final Pair<Supplier<Enchantment>, Integer>[] enchantments;

    @SafeVarargs
    public GemBowItem(Properties builder,  Pair<Supplier<Enchantment>, Integer>... enchantments) {
        super(builder);
        this.enchantments = enchantments;
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

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player) {
            Player playerentity = (Player) entityLiving;
            boolean hasInfinity = playerentity.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack itemstack = playerentity.getProjectile(stack);

            int ticksUsed = this.getUseDuration(stack) - timeLeft;
            ticksUsed = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, ticksUsed, !itemstack.isEmpty() || hasInfinity);

            if (ticksUsed < 0) {
                return;
            }

            if (!itemstack.isEmpty() || hasInfinity) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float velocity = getPowerForTime(ticksUsed);
                if (!((double) velocity < 0.1D)) {
                    boolean flag1 = playerentity.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
                    if (!worldIn.isClientSide) {
                        ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);

                        AbstractArrow abstractArrowEntity = arrowitem.createArrow(worldIn, itemstack, playerentity);
                        abstractArrowEntity = customArrow(abstractArrowEntity);

                        abstractArrowEntity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot(), 0.0F, velocity * 4.5F, 1.0F);

                        // Reduce the damage to counteract increasing the arrow velocity
                        abstractArrowEntity.setBaseDamage((abstractArrowEntity.getBaseDamage() / 1.5D) + 0.1D);

                        if (velocity == 1.0F) {
                            abstractArrowEntity.setCritArrow(true);
                        }

                        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                        if (powerLevel > 0) {
                            abstractArrowEntity.setBaseDamage(abstractArrowEntity.getBaseDamage() + (double) powerLevel * 0.5D + 0.5D);
                        }

                        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                        if (punchLevel > 0) {
                            abstractArrowEntity.setKnockback(punchLevel);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                            abstractArrowEntity.setSecondsOnFire(100);
                        }

                        stack.hurtAndBreak(1, playerentity, (player) -> {
                            player.broadcastBreakEvent(playerentity.getUsedItemHand());
                        });
                        if (flag1 || playerentity.getAbilities().instabuild && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            abstractArrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        worldIn.addFreshEntity(abstractArrowEntity);
                    }

                    worldIn.playSound(null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (playerentity.getRandom().nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
                    if (!flag1 && !playerentity.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.getInventory().removeItem(itemstack);
                        }
                    }

                    playerentity.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }
}
