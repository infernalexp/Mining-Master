package org.infernalstudios.miningmaster.client.integration.jei;

import com.mojang.datafixers.util.Pair;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.init.MMBlocks;
import org.infernalstudios.miningmaster.recipes.ForgingRecipe;

import java.util.Arrays;

public class GemForgeRecipeCategory implements IRecipeCategory<ForgingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(MiningMaster.MOD_ID, "jei_gemforge");
    private static final String TITLE_TRANSLATION_KEY = MiningMaster.MOD_ID + ".jei.forging";

    private final Component titleTextComponent = new TranslatableComponent(TITLE_TRANSLATION_KEY);
    private final IDrawable background;
    private final IDrawable icon;

    public GemForgeRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(MiningMaster.MOD_ID, "textures/gui/container/gem_forge.png"), 0, 185, 151, 54);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(MMBlocks.GEM_FORGE.get().asItem()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends ForgingRecipe> getRecipeClass() {
        return ForgingRecipe.class;
    }

    @Override
    @Deprecated
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

    @Override
    public void setIngredients(ForgingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    // These are hard-coded, not generated, because they don't follow a simple pattern.
    @SuppressWarnings("unchecked")
    private static final Pair<Integer, Integer>[] SLOTS = new Pair[] {
        // Slots 1 - 9
        Pair.of(0, 36),
        Pair.of(0, 18),
        Pair.of(0, 0),
        Pair.of(18, 0),
        Pair.of(36, 0),
        Pair.of(54, 0),
        Pair.of(72, 0),
        Pair.of(72, 18),
        Pair.of(72, 36),
        // Catalyst slot
        Pair.of(36, 26),
        // Output slot
        Pair.of(128, 17)
    };

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ForgingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();
        Ingredient catalyst = recipeIngredients.remove(recipeIngredients.size() - 1);
        ItemStack output = recipe.getDefaultedOutput();

        for (int i = 0; i < Math.min(recipeIngredients.size(), 9); i++) {
            itemStacks.init(i, true, SLOTS[i].getFirst(), SLOTS[i].getSecond());
        }
        itemStacks.set(ingredients);
        
        itemStacks.init(9, true, SLOTS[9].getFirst(), SLOTS[9].getSecond());
        itemStacks.set(9, Arrays.asList(catalyst.getItems()));
        
        itemStacks.init(10, false, 129, 18);
        itemStacks.set(10, output);
    }
}
