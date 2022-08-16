package org.infernalstudios.miningmaster.client.integration.jei;

import java.util.Arrays;

import com.mojang.datafixers.util.Pair;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.init.MMBlocks;
import org.infernalstudios.miningmaster.recipes.ForgingRecipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;

public class GemForgeRecipeCategory implements IRecipeCategory<ForgingRecipe> {
  public static final RecipeType<ForgingRecipe> RECIPE_TYPE =  RecipeType.create(MiningMaster.MOD_ID, ForgingRecipe.TYPE_ID.getPath(), ForgingRecipe.class);
  private static final String TITLE_TRANSLATION_KEY = MiningMaster.MOD_ID + ".jei.forging";
  private final Component titleTextComponent = Component.translatable(TITLE_TRANSLATION_KEY);
  private final IDrawable background;
  private final IDrawable icon;

  public GemForgeRecipeCategory(IGuiHelper guiHelper) {
    this.background = guiHelper.createDrawable(new ResourceLocation(MiningMaster.MOD_ID, "textures/gui/container/gem_forge.png"), 0, 185, 151, 54);
    this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MMBlocks.GEM_FORGE.get().asItem()));
  }

  @Override
  public RecipeType<ForgingRecipe> getRecipeType() {
    return RECIPE_TYPE;
  }

  @Override
  public Component getTitle() {
    return this.titleTextComponent;
  }

  @Override
  public IDrawable getBackground() {
    return this.background;
  }

  @Override
  public IDrawable getIcon() {
    return this.icon;
  }
  // These are hard-coded, not generated, because they don't follow a simple pattern.
  @SuppressWarnings("unchecked")
  private static final Pair<Integer, Integer>[] SLOTS = new Pair[] {
      // Slots 1 - 9
      Pair.of(1, 37),
      Pair.of(1, 19),
      Pair.of(1, 1),
      Pair.of(19, 1),
      Pair.of(37, 1),
      Pair.of(55, 1),
      Pair.of(73, 1),
      Pair.of(73, 19),
      Pair.of(73, 37),
      // Catalyst slot
      Pair.of(37, 27),
      // Output slot
      Pair.of(130, 19)
  };

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, ForgingRecipe recipe, IFocusGroup focusGroup) {
    NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();
    Ingredient catalyst = recipeIngredients.remove(recipeIngredients.size() - 1);
    ItemStack output = recipe.getDefaultedOutput();

    for (int i = 0; i < Math.min(recipeIngredients.size(), 9); i++) {
      builder
          .addSlot(RecipeIngredientRole.INPUT, SLOTS[i].getFirst(), SLOTS[i].getSecond())
          .addItemStacks(Arrays.asList(recipeIngredients.get(i).getItems()));
    }

    builder
        .addSlot(RecipeIngredientRole.CATALYST, SLOTS[9].getFirst(), SLOTS[9].getSecond())
        .addItemStacks(Arrays.asList(catalyst.getItems()));

    builder
        .addSlot(RecipeIngredientRole.OUTPUT, SLOTS[10].getFirst(), SLOTS[10].getSecond())
        .addItemStack(output);
  }
}