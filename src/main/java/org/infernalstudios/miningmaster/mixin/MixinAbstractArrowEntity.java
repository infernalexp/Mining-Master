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

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import org.infernalstudios.miningmaster.init.MMEnchantments;
import org.infernalstudios.miningmaster.init.MMTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrowEntity.class)
public class MixinAbstractArrowEntity {
    @Shadow private double damage;
    private int freezingLevel = 0;

    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;)V", at = @At("RETURN"))
    private void MM_setFreezingOnCreation(EntityType type, LivingEntity shooter, World worldIn, CallbackInfo ci) {
        int freezing = EnchantmentHelper.getMaxEnchantmentLevel(MMEnchantments.FREEZING.get(), shooter);
        if (freezing > 0) {
            this.freezingLevel = freezing;
        }
    }

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/AbstractArrowEntity;getIsCritical()Z"))
    private void MM_dealArrowFreezing(EntityRayTraceResult hitResult, CallbackInfo ci) {
        if (this.freezingLevel > 0) {
            if (hitResult.getEntity() instanceof LivingEntity) {
                LivingEntity livingTarget = (LivingEntity) hitResult.getEntity();
                livingTarget.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 60, this.freezingLevel));
            }

            if (hitResult.getEntity().getType().isContained(MMTags.EntityTypes.FIRE_ENTITIES)) {
                hitResult.getEntity().attackEntityFrom(DamageSource.causeArrowDamage((AbstractArrowEntity) (Object) this, hitResult.getEntity()),(float) this.damage * 0.3F * this.freezingLevel);
            }
        }
    }
}
