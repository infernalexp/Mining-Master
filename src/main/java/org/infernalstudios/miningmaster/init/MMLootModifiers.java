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

package org.infernalstudios.miningmaster.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.infernalstudios.miningmaster.MiningMaster;

import javax.annotation.Nonnull;
import java.util.Optional;

public class MMLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MiningMaster.MOD_ID);

    public static final RegistryObject<Codec<SmeltingEnchantmentLootModifier>> SMELTING_ENCHANTMENT_LOOT_MODIFIER = LOOT_MODIFIERS.register("smelting_enchantment_loot_modifier", () -> SmeltingEnchantmentLootModifier.CODEC);
    public static final RegistryObject<Codec<StonebreakerEnchantmentLootModifier>> STONEBREAKER_ENCHANTMENT_LOOT_MODIFIER = LOOT_MODIFIERS.register("stonebreaker_enchantment_loot_modifier", () -> StonebreakerEnchantmentLootModifier.CODEC);

    private static class SmeltingEnchantmentLootModifier extends LootModifier {
        public static final Codec<SmeltingEnchantmentLootModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, SmeltingEnchantmentLootModifier::new));

        /**
         * Constructs a LootModifier.
         *
         * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
         */
        protected SmeltingEnchantmentLootModifier(LootItemCondition[] conditionsIn) {
            super(conditionsIn);
        }

        @Nonnull
        @Override
        protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
            for (ItemStack item : generatedLoot) {
                Optional<SmeltingRecipe> optional = context.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(item), context.getLevel());
                if (optional.isPresent()) {
                    ItemStack itemstack = optional.get().getResultItem();
                    if (!itemstack.isEmpty()) {
                        ItemStack itemstack1 = itemstack.copy();
                        itemstack1.setCount(item.getCount() * itemstack.getCount()); //Forge: Support smelting returning multiple
                        generatedLoot.add(itemstack1);
                        generatedLoot.remove(item);
                    }
                }
            }
            return generatedLoot;
        }


        @Override
        public Codec<? extends IGlobalLootModifier> codec() {
            return CODEC;
        }
    }

    private static class StonebreakerEnchantmentLootModifier extends LootModifier {
        public static final Codec<StonebreakerEnchantmentLootModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, StonebreakerEnchantmentLootModifier::new));

        /**
         * Constructs a LootModifier.
         *
         * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
         */
        protected StonebreakerEnchantmentLootModifier(LootItemCondition[] conditionsIn) {
            super(conditionsIn);
        }

        @Nonnull
        @Override
        protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
            generatedLoot.removeIf(item -> item.is(MMTags.Items.STONEBREAKER_ITEMS));
            return generatedLoot;
        }

        @Override
        public Codec<? extends IGlobalLootModifier> codec() {
            return CODEC;
        }
    }

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIERS.register(eventBus);
        MiningMaster.LOGGER.info("Mining Master: Loot Modifiers Registered!");
    }
}
