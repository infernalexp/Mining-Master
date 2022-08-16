package org.infernalstudios.miningmaster.client.integration.jei;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.init.MMBlocks;
import org.infernalstudios.miningmaster.init.MMRecipes;

import net.minecraft.client.Minecraft;
import org.infernalstudios.miningmaster.recipes.ForgingRecipe;

@JeiPlugin
public class MiningMasterJEIPlugin implements IModPlugin {
  private static final ResourceLocation UID = new ResourceLocation(MiningMaster.MOD_ID, "jei_main");

  @Override
  public ResourceLocation getPluginUid() {
    return UID;
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    registration.addRecipeCategories(new GemForgeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    Collection<Recipe<?>> recipes = Minecraft.getInstance().level.getRecipeManager().getRecipes();
    List<Recipe<?>> gemForgingRecipes = recipes.stream()
        .filter(recipe -> recipe.getType() == MMRecipes.FORGING_RECIPE_TYPE)
        .collect(ImmutableList.toImmutableList());
    registration.addRecipes(GemForgeRecipeCategory.RECIPE_TYPE, (List<ForgingRecipe>) (Object) gemForgingRecipes);
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(new ItemStack(MMBlocks.GEM_FORGE.get().asItem()), GemForgeRecipeCategory.RECIPE_TYPE);
  }
}