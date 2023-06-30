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

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.infernalstudios.miningmaster.init.MMEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public class MixinAbstractArrow {
    private int freezingLevel = 0;
    private int floatingLevel = 0;

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;)V", at = @At("RETURN"))
    private void MM_setEnchantsOnCreation(EntityType entityType, LivingEntity shooter, Level level, CallbackInfo ci) {
        int freezing = EnchantmentHelper.getEnchantmentLevel(MMEnchantments.FREEZING.get(), shooter);
        int floating = EnchantmentHelper.getEnchantmentLevel(MMEnchantments.FLOATATION.get(), shooter);

        if (freezing > 0) {
            this.freezingLevel = freezing;
        }

        if (floating > 0) {
            this.floatingLevel = floating;
        }
    }

    @Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;doPostHurtEffects(Lnet/minecraft/world/entity/LivingEntity;)V"))
    private void MM_dealArrowEnchants(EntityHitResult hitResult, CallbackInfo ci) {
        if (this.freezingLevel > 0) {
            if (hitResult.getEntity() instanceof LivingEntity livingEntity && !livingEntity.level.isClientSide() && !livingEntity.isDeadOrDying()) {
                livingEntity.setTicksFrozen(Math.max(livingEntity.getTicksFrozen(), livingEntity.getTicksRequiredToFreeze() + 120 * this.freezingLevel));
            }
        }

        if (this.floatingLevel > 0) {
            if (hitResult.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 4 * this.floatingLevel));
            }
        }
    }
}
