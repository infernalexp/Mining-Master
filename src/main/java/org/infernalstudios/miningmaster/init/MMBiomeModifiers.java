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

import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.world.biome.modifiers.AddConfigurableFeaturesBiomeModifier;

public class MMBiomeModifiers {

    private static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MiningMaster.MOD_ID);

    public static final RegistryObject<Codec<AddConfigurableFeaturesBiomeModifier>> ADD_CONFIGURABLE_FEATURES = BIOME_MODIFIERS.register("add_configurable_features", () -> AddConfigurableFeaturesBiomeModifier.CODEC);

    public static void register(IEventBus eventBus) {
        BIOME_MODIFIERS.register(eventBus);
        MiningMaster.LOGGER.info("Mining Master: Biome Modifiers Registered!");
    }

}
