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

package org.infernalstudios.miningmaster.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.infernalstudios.miningmaster.MiningMaster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class MixinPowderSnowBlock {

    @Inject(method = "canEntityWalkOnPowderSnow", at = @At("RETURN"), cancellable = true)
    private static void MM_snowpiercerWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity livingEntity) {
            ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
            ListTag enchantments = leggingsStack.getEnchantmentTags();

            for(int i = 0; i < enchantments.size(); ++i) {
                CompoundTag compoundtag = enchantments.getCompound(i);
                ResourceLocation tagEnchantment = EnchantmentHelper.getEnchantmentId(compoundtag);
                if (tagEnchantment != null && tagEnchantment.equals(new ResourceLocation(MiningMaster.MOD_ID, "snowpiercer"))) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
