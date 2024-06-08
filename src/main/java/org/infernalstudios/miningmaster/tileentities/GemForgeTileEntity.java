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

package org.infernalstudios.miningmaster.tileentities;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.blocks.GemForgeBlock;
import org.infernalstudios.miningmaster.containers.GemForgeContainer;
import org.infernalstudios.miningmaster.init.MMRecipes;
import org.infernalstudios.miningmaster.init.MMSounds;
import org.infernalstudios.miningmaster.init.MMTags;
import org.infernalstudios.miningmaster.init.MMTileEntityTypes;
import org.infernalstudios.miningmaster.recipes.ForgingRecipe;

import javax.annotation.Nullable;

public class GemForgeTileEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {
    private static final int FORGE_TIME_TOTAL = 300;

    @Nullable
    protected Component customName;
    private final ItemStackHandler inventory = new ItemStackHandler(10){
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    private int forgeTime = 0;

    private boolean forgeActive;
    private boolean recipeValid;

    // I know this is sloppy, but Containers can only track Int Arrays
    protected final ContainerData forgeData = new ContainerData() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return GemForgeTileEntity.this.forgeActive ? 1 : 0;
                case 1:
                    return GemForgeTileEntity.this.recipeValid ? 1 : 0;
                case 2:
                    return GemForgeTileEntity.this.forgeTime;
                case 3:
                    return GemForgeTileEntity.this.FORGE_TIME_TOTAL;
                default:
                    return 0;
            }
        }

        public void set(int index, int k) {
            switch(index) {
                case 0:
                    GemForgeTileEntity.this.forgeActive = k == 1;
                    if (!GemForgeTileEntity.this.level.isClientSide()) {
                        GemForgeTileEntity.this.level.playSound(null, GemForgeTileEntity.this.worldPosition, MMSounds.GEM_FORGE_COOK.get(), SoundSource.BLOCKS, 1.0F, GemForgeTileEntity.this.level.getRandom().nextFloat() * 0.4F + 1.0F);
                    }
                    break;
                case 1:
                    GemForgeTileEntity.this.recipeValid = k == 1;
                case 2:
                    GemForgeTileEntity.this.forgeTime = k;
            }
        }

        public int getCount() {
            return 4;
        }
    };;

    private static final int[] SLOTS_UP = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
    private static final int[] SLOTS_DOWN = new int[]{9};
    private static final int[] SLOTS_HORIZONTAL = new int[]{9};

    public GemForgeTileEntity(BlockPos pos, BlockState state) {
        super(MMTileEntityTypes.GEM_FORGE_TILE_ENTITY.get(), pos, state);
    }

    public static void tickForge(Level level, BlockPos pos, BlockState state, GemForgeTileEntity gemForge) {
        boolean flag = gemForge.isForging();
        boolean flag1 = false;
        if (gemForge.isForging()) {
            ++gemForge.forgeTime;
        }

        if (!level.isClientSide) {
            ForgingRecipe recipe = level.getRecipeManager().getRecipeFor(MMRecipes.FORGING_RECIPE_TYPE, gemForge, level).orElse(null);
            gemForge.recipeValid = gemForge.canForge(recipe);

            if (gemForge.recipeValid && gemForge.forgeActive) {
                ++gemForge.forgeTime;

                if (gemForge.forgeTime >= FORGE_TIME_TOTAL) {
                    gemForge.forgeTime = 0;
                    gemForge.forge(recipe);
                    flag1 = true;
                }
            } else {
                gemForge.forgeActive = false;
                gemForge.forgeTime = 0;
            }

            if (flag != gemForge.isForging()) {
                flag1 = true;
                level.setBlock(pos, state.setValue(GemForgeBlock.LIT, gemForge.isForging()), 3);
            }
        }

        if (flag1) {
            gemForge.setChanged();
        }
    }

    private boolean isForging() {
        return this.forgeTime > 0;
    }

    @Override
    public void setCustomName(Component name) {
        this.customName = name;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        inventory.deserializeNBT(nbt.getCompound("inv"));
        CompoundTag compoundnbt = nbt.getCompound("RecipesUsed");

        this.forgeActive = nbt.getBoolean("ForgeActive");
        this.recipeValid = nbt.getBoolean("RecipeValid");

        for(String s : compoundnbt.getAllKeys()) {
            this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }

    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("inv", inventory.serializeNBT());
        CompoundTag compoundnbt = new CompoundTag();
        this.recipes.forEach((recipeId, craftedAmount) -> {
            compoundnbt.putInt(recipeId.toString(), craftedAmount);
        });
        compound.put("RecipesUsed", compoundnbt);
        compound.putBoolean("ForgeActive", this.forgeActive);
        compound.putBoolean("RecipeValid", this.recipeValid);
    }

    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new GemForgeContainer(id, inv, this.inventory, this.forgeData);
    }

    public Component getDisplayName() {
        return this.customName != null ? this.customName : Component.translatable(MiningMaster.MOD_ID + ':' + "container.gem_forge");
    }

    @Override
    protected Component getDefaultName() {
        return null;
    }

    protected boolean canForge(@Nullable ForgingRecipe recipeIn) {
        if (recipeIn != null) {
            ItemStack result = recipeIn.assemble(this, this.level.registryAccess());

            return !result.isEmpty();
        } else {
            return false;
        }
    }

    private void forge(@Nullable ForgingRecipe recipe) {
        if (recipe != null && this.canForge(recipe)) {
            ItemStack result = recipe.assemble(this, this.level.registryAccess());

            this.inventory.setStackInSlot(9, result.copy());

            if (!this.level.isClientSide) {
                this.setRecipeUsed(recipe);
            }

            for(int i = 0; i < 9; ++i) {
                this.removeItem(i, 1);
            }

            this.forgeActive = false;

            if (!GemForgeTileEntity.this.level.isClientSide()) {
                GemForgeTileEntity.this.level.playSound(null, GemForgeTileEntity.this.worldPosition, MMSounds.GEM_FORGE_COMPLETE.get(), SoundSource.BLOCKS, 1.0F, GemForgeTileEntity.this.level.getRandom().nextFloat() * 0.8F + 0.25F);
            }
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_DOWN;
        } else {
            return side == Direction.UP ? SLOTS_UP : SLOTS_HORIZONTAL;
        }
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return !stack.is(MMTags.Items.GEMS) && !stack.is(MMTags.Items.CATALYSTS);
    }

    public int getContainerSize() {
        return this.inventory.getSlots();
    }

    public boolean isEmpty() {
        for(int i = 0; i < 10; i++) {
            if (!this.inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    // Returns the stack in the given slot.
    public ItemStack getItem(int index) {
        return this.inventory.getStackInSlot(index);
    }

    // Removes up to a specified number of items from an inventory slot and returns them in a new stack.
    public ItemStack removeItem(int index, int count) {
        return !this.inventory.getStackInSlot(index).isEmpty() && count > 0 ? this.inventory.getStackInSlot(index).split(count) : ItemStack.EMPTY;
    }

    // Removes a stack from the given slot and returns it.
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack itemStack = this.inventory.getStackInSlot(index);
        this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        return itemStack;
    }

    // Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
    public void setItem(int index, ItemStack stack) {
        this.inventory.setStackInSlot(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }

    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            System.out.println(false);
            return false;
        } else {
            System.out.println(true);
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    public boolean canPlaceItem(int index, ItemStack stack) {
        if (this.getItem(index).getCount() != 0) {
            return false;
        } else if (index < 9) {
            return stack.is(MMTags.Items.GEMS);
        } else {
            return stack.is(MMTags.Items.CATALYSTS);
        }
    }

    public void clearContent() {
        for(int i = 0; i < 10; i++) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipes.addTo(resourcelocation, 1);
        }
    }

    @Nullable
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    public void fillStackedContents(StackedContents helper) {
        for(int i = 0; i < 10; i++) {
            helper.accountStack(this.inventory.getStackInSlot(i));
        }
    }

    LazyOptional<? extends IItemHandler>[] handlers =
            SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }
}
