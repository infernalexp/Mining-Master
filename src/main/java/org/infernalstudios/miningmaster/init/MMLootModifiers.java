package org.infernalstudios.miningmaster.init;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.Smelt;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.miningmaster.MiningMaster;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class MMLootModifiers {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, MiningMaster.MOD_ID);

    public static final RegistryObject<GlobalLootModifierSerializer<SmeltingEnchantmentLootModifier>> SMELTING_ENCHANTMENT_LOOT_MODIFIER = LOOT_MODIFIERS.register("smelting_enchantment_loot_modifier", SmeltingEnchantmentLootSerializer::new);

    private static class SmeltingEnchantmentLootModifier extends LootModifier {

        /**
         * Constructs a LootModifier.
         *
         * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
         */
        protected SmeltingEnchantmentLootModifier(ILootCondition[] conditionsIn) {
            super(conditionsIn);
        }

        @Nonnull
        @Override
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
            for (ItemStack item : generatedLoot) {
                Optional<FurnaceRecipe> optional = context.getWorld().getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(item), context.getWorld());
                if (optional.isPresent()) {
                    ItemStack itemstack = optional.get().getRecipeOutput();
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
    }

    private static class SmeltingEnchantmentLootSerializer extends GlobalLootModifierSerializer<SmeltingEnchantmentLootModifier> {

        @Override
        public SmeltingEnchantmentLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditionsIn) {
            return new SmeltingEnchantmentLootModifier(conditionsIn);
        }

        @Override
        public JsonObject write(SmeltingEnchantmentLootModifier instance) {
            return null;
        }
    }

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIERS.register(eventBus);
        MiningMaster.LOGGER.info("Mining Master: Loot Modifiers Registered!");
    }
}
