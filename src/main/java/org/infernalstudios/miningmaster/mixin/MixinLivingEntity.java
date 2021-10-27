package org.infernalstudios.miningmaster.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import org.infernalstudios.miningmaster.init.MMEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    @Shadow private int jumpTicks;

    @Shadow protected abstract void jump();

    @Shadow public abstract ItemStack getItemStackFromSlot(EquipmentSlotType slotIn);

    private int knightJumpsUsed = 0;
    private boolean hasJumped = false;
    private int ticksSinceJump = 0;

    public MixinLivingEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Inject(method = "jump", at = @At(value = "HEAD"))
    private void MM_setHasJumped(CallbackInfo ci) {
        ticksSinceJump = 0;
        this.hasJumped = true;
    }

    @Inject(method = "livingTick", at = @At(value = "HEAD"))
    private void MM_countTicksFalling(CallbackInfo ci) {
        if (this.onGround) {
            this.ticksSinceJump = 0;
            this.hasJumped = false;
            this.knightJumpsUsed = 0;
        } else {
            this.ticksSinceJump++;
        }
    }

    @Inject(method = "livingTick", at = @At(target = "Lnet/minecraft/entity/LivingEntity;getFluidJumpHeight()D", value = "INVOKE", shift = At.Shift.AFTER))
    private void MM_knightJump(CallbackInfo ci) {
        if (!this.onGround) {
            boolean hasKnightJump = false;
            int level = 0;

            ItemStack stack = this.getItemStackFromSlot(EquipmentSlotType.LEGS);
            ListNBT nbtList = stack.getEnchantmentTagList();

            for (int i = 0; i < nbtList.size(); i++) {
                CompoundNBT idTag = nbtList.getCompound(i);
                if (idTag.getString("id").equals(MMEnchantments.KNIGHT_JUMP.getId().toString())) {
                    hasKnightJump = true;
                    level = idTag.getInt("lvl");
                }
            }

            if (this.knightJumpsUsed < level && hasKnightJump && (!this.hasJumped || ticksSinceJump >= 5)) {
                this.knightJumpsUsed++;
                this.jump();
                this.jumpTicks = 10;
            }
        }
    }
}
