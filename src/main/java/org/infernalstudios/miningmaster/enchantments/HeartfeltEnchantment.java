package org.infernalstudios.miningmaster.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.infernalstudios.miningmaster.init.MMEnchantments;

import java.util.Random;
import java.util.UUID;

public class HeartfeltEnchantment extends Enchantment {

    public HeartfeltEnchantment(Rarity rarityIn, EquipmentSlotType... slots) {
        super(rarityIn, EnchantmentType.ARMOR_CHEST, slots);
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
        return 5;
    }

    @Override
    public boolean canVillagerTrade() {
        return false;
    }

    @Override
    public boolean canGenerateInLoot() {
        return false;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }

    @Override
    public void onUserHurt(LivingEntity user, Entity attacker, int level) {
        Iterable<ItemStack> equipment = user.getEquipmentAndArmor();
        for (ItemStack item : equipment) {
            ListNBT nbtList = item.getEnchantmentTagList();
            for (int i = 0; i < nbtList.size(); i++) {
                CompoundNBT idTag = nbtList.getCompound(i);
                if (idTag.getString("id").equals(MMEnchantments.HEARTFELT.getId().toString())) {
                    item.attemptDamageItem(idTag.getInt("lvl"), new Random(), null);
                }
            }
        }
        super.onUserHurt(user, attacker, level);
    }

    @SubscribeEvent
    public static void onItemAttributeModifierCalculate(ItemAttributeModifierEvent event) {
        if (event.getSlotType().equals(EquipmentSlotType.CHEST)) {
            ItemStack itemStack = event.getItemStack();
            ListNBT nbtList = itemStack.getEnchantmentTagList();
            for (int i = 0; i < nbtList.size(); i++) {
                CompoundNBT idTag = nbtList.getCompound(i);
                if (idTag.getString("id").equals(MMEnchantments.HEARTFELT.getId().toString())) {
                    event.addModifier(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("ccac859c-0311-43d4-b254-572d7846a5be"), "heartfelt", 4.0D * idTag.getInt("lvl"), AttributeModifier.Operation.ADDITION));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUnequip(LivingEvent.LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity.getHealth() > livingEntity.getMaxHealth()) {
            livingEntity.setHealth(livingEntity.getMaxHealth());
        }
    }
}
