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

package org.infernalstudios.miningmaster.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import org.infernalstudios.miningmaster.init.MMEnchantments;
import org.infernalstudios.miningmaster.init.MMTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {

    @Shadow public abstract void livingTick();

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At(target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", value = "INVOKE_ASSIGN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void MM_calculateLeechingHeal(Entity targetEntity, CallbackInfo ci, float f, float f1, float f2, boolean flag, boolean flag1, float i, boolean flag2, net.minecraftforge.event.entity.player.CriticalHitEvent hitResult, boolean flag3, double d0, float f4, boolean flag4, int j, Vector3d vector3d, boolean flag5) {
        if (flag5) {
            ItemStack itemStack = ((PlayerEntity) (Object) this).getHeldItemMainhand();
            ListNBT nbtList = itemStack.getEnchantmentTagList();

            for (int k = 0; k < nbtList.size(); k++) {
                CompoundNBT idTag = nbtList.getCompound(k);

                if (idTag.getString("id").equals(MMEnchantments.LEECHING.getId().toString())) {
                    applyLeechingEffects(idTag.getInt("lvl"), f);
                } else if (idTag.getString("id").equals(MMEnchantments.FREEZING.getId().toString())) {
                    applyFreezingEffects(targetEntity, idTag.getInt("lvl"), f);
                }
            }
        }
    }

    private void applyFreezingEffects(Entity targetEntity, int level, float damageAmount) {
        if (targetEntity instanceof LivingEntity) {
            LivingEntity livingTarget = (LivingEntity) targetEntity;
            livingTarget.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 60, level));
        }

        if (targetEntity.getType().isContained(MMTags.EntityTypes.FIRE_ENTITIES)) {
            targetEntity.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) (Object) this),damageAmount * 0.3F * level);
        }
    }

    private void applyLeechingEffects(int level, float damageAmount) {
        ((PlayerEntity) (Object) this).heal(damageAmount * 0.25F * level);
    }
}
