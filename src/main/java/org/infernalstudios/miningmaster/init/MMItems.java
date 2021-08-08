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

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.items.GemItem;

import java.util.function.Supplier;

public class MMItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MiningMaster.MOD_ID);

    public static final RegistryObject<Item> FIRE_RUBY = registerItem("fire_ruby", GemItem::new);
    public static final RegistryObject<Item> ICE_SAPPHIRE = registerItem("ice_sapphire", GemItem::new);
    public static final RegistryObject<Item> SPIRIT_GARNET = registerItem("spirit_garnet", GemItem::new);
    public static final RegistryObject<Item> HASTE_PERIDOT = registerItem("haste_peridot", GemItem::new);
    public static final RegistryObject<Item> LUCKY_CITRINE = registerItem("lucky_citrine", GemItem::new);


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        MiningMaster.LOGGER.info("Mining Master: Items Registered!");
    }

    public static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<? extends T> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }
}
