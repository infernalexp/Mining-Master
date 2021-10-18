package org.infernalstudios.miningmaster.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.infernalstudios.miningmaster.init.MMEnchantments;

public class LeechingEnchantment extends Enchantment {
    public LeechingEnchantment(Rarity rarityIn, EquipmentSlotType... slots) {
        super(rarityIn, EnchantmentType.WEAPON, slots);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || super.canApply(stack);
    }

    @SubscribeEvent
    public static void onEntityDamage(LivingDamageEvent event) {
        Entity source = event.getSource().getTrueSource();
        float amount = event.getAmount();

        if (source instanceof LivingEntity) {
            LivingEntity livingSource = (LivingEntity) source;

            for (ItemStack stack: livingSource.getHeldEquipment()) {
                ListNBT nbtList = stack.getEnchantmentTagList();

                for (int i = 0; i < nbtList.size(); i++) {
                    CompoundNBT idTag = nbtList.getCompound(i);

                    if (idTag.getString("id").equals(MMEnchantments.LEECHING.getId().toString())) {
                        ((LivingEntity) source).heal(amount * 0.25F * idTag.getInt("lvl"));
                    }
                }
            }
        }
    }
}
