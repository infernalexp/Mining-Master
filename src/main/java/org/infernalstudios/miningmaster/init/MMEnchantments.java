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

package org.infernalstudios.miningmaster.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.enchantments.FreezingEnchantment;
import org.infernalstudios.miningmaster.enchantments.LeechingEnchantment;
import org.infernalstudios.miningmaster.enchantments.SmeltingEnchantment;

public class MMEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MiningMaster.MOD_ID);

    public static final RegistryObject<Enchantment> FREEZING = ENCHANTMENTS.register("freezing", () -> new FreezingEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> LEECHING = ENCHANTMENTS.register("leeching", () -> new LeechingEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> SMELTING = ENCHANTMENTS.register("smelting", () -> new SmeltingEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
        MiningMaster.LOGGER.info("Mining Master: Enchantments Registered!");
    }
}
