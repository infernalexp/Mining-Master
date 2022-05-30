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

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.containers.GemForgeContainer;

public class MMContainerTypes {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, MiningMaster.MOD_ID);

    public static final RegistryObject<MenuType<GemForgeContainer>> GEM_FORGE_CONTAINER = CONTAINER_TYPES.register("gem_forge_container", () -> IForgeMenuType.create(((windowId, inv, data) -> new GemForgeContainer(windowId, inv))));

    public static void register(IEventBus eventBus) {
        CONTAINER_TYPES.register(eventBus);
        MiningMaster.LOGGER.info("Mining Master: Container Types Registered!");
    }
}
