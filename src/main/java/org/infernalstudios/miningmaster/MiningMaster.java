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

package org.infernalstudios.miningmaster;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infernalstudios.miningmaster.enchantments.LeechingEnchantment;
import org.infernalstudios.miningmaster.init.*;


@Mod(MiningMaster.MOD_ID)
public class MiningMaster {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "miningmaster";

    public MiningMaster() {
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MMBlocks.register(modEventBus);
        MMItems.register(modEventBus);
        MMEnchantments.register(modEventBus);
        MMRecipes.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(new MiningMasterEvents());
        MinecraftForge.EVENT_BUS.addListener(LeechingEnchantment::onEntityDamage);
    }
}
