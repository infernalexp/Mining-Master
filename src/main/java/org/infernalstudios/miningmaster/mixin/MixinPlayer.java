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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.infernalstudios.miningmaster.init.MMEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public abstract class MixinPlayer {

    @Inject(method = "attack", at = @At(target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", value = "INVOKE_ASSIGN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void MM_calculateEnchantEffects(Entity targetEntity, CallbackInfo ci, float f, float f1, float f2, boolean flag, boolean flag1, float i, boolean flag2, CriticalHitEvent hitResult, boolean flag3, double d0, float f4, boolean flag4, int j, Vec3 vec3, boolean flag5) {
        if (flag5) {
            ItemStack itemStack = ((Player) (Object) this).getMainHandItem();
            ListTag nbtList = itemStack.getEnchantmentTags();

            for (int k = 0; k < nbtList.size(); k++) {
                CompoundTag idTag = nbtList.getCompound(k);

                if (idTag.getString("id").equals(MMEnchantments.LEECHING.getId().toString())) {
                    applyLeechingEffects(idTag.getInt("lvl"), f);
                } else if (idTag.getString("id").equals(MMEnchantments.FREEZING.getId().toString()) && !(itemStack.getItem() instanceof BowItem || itemStack.getItem() instanceof CrossbowItem)) {
                    applyFreezingEffects(targetEntity, idTag.getInt("lvl"));
                }
            }
        }
    }

    private void applyFreezingEffects(Entity targetEntity, int level) {
        if (targetEntity instanceof LivingEntity livingTarget) {
            livingTarget.setTicksFrozen(livingTarget.getTicksFrozen() + livingTarget.getTicksRequiredToFreeze() + 120 * level);
        }
    }

    private void applyLeechingEffects(int level, float damageAmount) {
        ((Player) (Object) this).heal(damageAmount * 0.25F * level);
    }
}
