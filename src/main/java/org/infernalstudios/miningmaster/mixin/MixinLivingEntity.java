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
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.infernalstudios.miningmaster.access.LivingEntityAccess;
import org.infernalstudios.miningmaster.init.MMEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements LivingEntityAccess {
    @Shadow protected abstract void jumpFromGround();

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slotIn);

    private int knightJumpsUsed = 0;

    @Unique
    private static final EntityDataAccessor<Boolean> GRACE_RECHARGED = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "aiStep", at = @At(value = "HEAD"))
    private void MM_countTicksFalling(CallbackInfo ci) {
        if (this.onGround) {
            this.knightJumpsUsed = 0;
        }
    }

    public MixinLivingEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public void useKnightJump() {
        if (!this.onGround) {
            boolean hasKnightJump = false;
            int level = 0;

            ItemStack stack = this.getItemBySlot(EquipmentSlot.LEGS);
            ListTag nbtList = stack.getEnchantmentTags();

            for (int i = 0; i < nbtList.size(); i++) {
                CompoundTag idTag = nbtList.getCompound(i);
                if (idTag.getString("id").equals(MMEnchantments.KNIGHT_JUMP.getId().toString())) {
                    hasKnightJump = true;
                    level = idTag.getInt("lvl");
                }
            }

            if (hasKnightJump && this.knightJumpsUsed < level) {
                this.knightJumpsUsed++;
                this.jumpFromGround();
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "defineSynchedData")
    private void MM_registerData(CallbackInfo ci) {
        ((LivingEntity) (Object) this).getEntityData().define(GRACE_RECHARGED, true);
    }

    @Inject(at = @At("RETURN"), method = "addAdditionalSaveData")
    private void MM_writeAdditionalData(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("GraceRecharged", ((LivingEntity) (Object) this).getEntityData().get(GRACE_RECHARGED));
    }

    @Inject(at = @At("RETURN"), method = "readAdditionalSaveData")
    private void MM_readAdditionalData(CompoundTag compound, CallbackInfo ci) {
        setGraceRecharged(compound.getBoolean("GraceRecharged"));
    }

    @Override
    public void setGraceRecharged(boolean isGraceRecharged) {
        ((LivingEntity) (Object) this).getEntityData().set(GRACE_RECHARGED, isGraceRecharged);
    }

    @Override
    public boolean getGraceRecharged() {
        return ((LivingEntity) (Object) this).getEntityData().get(GRACE_RECHARGED);
    }
}
