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
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.blocks.GemForgeBlock;
import org.infernalstudios.miningmaster.containers.GemForgeContainer;
import org.infernalstudios.miningmaster.init.MMRecipes;
import org.infernalstudios.miningmaster.init.MMSounds;
import org.infernalstudios.miningmaster.init.MMTags;
import org.infernalstudios.miningmaster.init.MMTileEntityTypes;
import org.infernalstudios.miningmaster.recipes.ForgingRecipe;

import javax.annotation.Nullable;

public class GemForgeTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity, IRecipeHolder, IRecipeHelperPopulator {
    private final int FORGE_TIME_TOTAL = 300;

    @Nullable
    protected ITextComponent customName;
    private final ItemStackHandler inventory = new ItemStackHandler(10){
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markDirty();
        }
    };

    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    private int forgeTime = 0;

    private boolean forgeActive;
    private boolean recipeValid;

    // I know this is sloppy, but Containers can only track Int Arrays
    protected final IIntArray forgeData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return GemForgeTileEntity.this.forgeActive ? 1 : 0;
                case 1:
                    return GemForgeTileEntity.this.recipeValid ? 1 : 0;
                default:
                    return 0;
            }
        }

        public void set(int index, int k) {
            switch(index) {
                case 0:
                    GemForgeTileEntity.this.forgeActive = k == 1;
                    if (!GemForgeTileEntity.this.world.isRemote()) {
                        GemForgeTileEntity.this.world.playSound(null, GemForgeTileEntity.this.pos, MMSounds.GEM_FORGE_COOK.get(), SoundCategory.BLOCKS, 1.0F, GemForgeTileEntity.this.world.getRandom().nextFloat() * 0.4F + 1.0F);
                    }
                    break;
                case 1:
                    GemForgeTileEntity.this.recipeValid = k == 1;
            }
        }

        public int size() {
            return 2;
        }
    };;

    private static final int[] SLOTS_UP = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
    private static final int[] SLOTS_DOWN = new int[]{9};
    private static final int[] SLOTS_HORIZONTAL = new int[]{9};

    public GemForgeTileEntity() {
        super(MMTileEntityTypes.GEM_FORGE_TILE_ENTITY.get());
    }

    public void tick() {
        boolean flag = this.isForging();
        boolean flag1 = false;
        if (this.isForging()) {
            ++this.forgeTime;
        }

        if (!this.world.isRemote) {
            ItemStack itemstack = this.inventory.getStackInSlot(9);
            if (!itemstack.isEmpty()) {
                ForgingRecipe recipe = this.world.getRecipeManager().getRecipe(MMRecipes.FORGING_RECIPE_TYPE, this, this.world).orElse(null);

                this.recipeValid = this.canForge(recipe);

                if (this.canForge(recipe) && this.forgeActive) {
                    ++this.forgeTime;

                    if (this.forgeTime >= FORGE_TIME_TOTAL) {
                        this.forgeTime = 0;
                        this.forge(recipe);
                        flag1 = true;
                    }
                } else {
                    this.forgeActive = false;
                    this.forgeTime = 0;
                }
            }

            if (flag != this.isForging()) {
                flag1 = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(GemForgeBlock.LIT, this.isForging()), 3);
            }
        }

        if (flag1) {
            this.markDirty();
        }
    }

    private boolean isForging() {
        return this.forgeTime > 0;
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    public void setCustomName(ITextComponent name) {
        this.customName = name;
    }

    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        inventory.deserializeNBT(nbt.getCompound("inv"));
        CompoundNBT compoundnbt = nbt.getCompound("RecipesUsed");

        this.forgeActive = nbt.getBoolean("ForgeActive");
        this.recipeValid = nbt.getBoolean("RecipeValid");

        for(String s : compoundnbt.keySet()) {
            this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }

    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("inv", inventory.serializeNBT());
        CompoundNBT compoundnbt = new CompoundNBT();
        this.recipes.forEach((recipeId, craftedAmount) -> {
            compoundnbt.putInt(recipeId.toString(), craftedAmount);
        });
        compound.put("RecipesUsed", compoundnbt);
        compound.putBoolean("ForgeActive", this.forgeActive);
        compound.putBoolean("RecipeValid", this.recipeValid);
        return compound;
    }

    protected Container createMenu(int id, PlayerInventory inv) {
        return new GemForgeContainer(id, inv, this.inventory, this.forgeData);
    }

    public ITextComponent getDisplayName() {
        return this.customName != null ? this.customName : new TranslationTextComponent(MiningMaster.MOD_ID + ':' + "container.gem_forge");
    }

    @Override
    protected ITextComponent getDefaultName() {
        return null;
    }

    protected boolean canForge(@Nullable ForgingRecipe recipeIn) {
        if (recipeIn != null) {
            ItemStack result = recipeIn.getCraftingResult(this);

            return !result.isEmpty();
        } else {
            return false;
        }
    }

    private void forge(@Nullable ForgingRecipe recipe) {
        if (recipe != null && this.canForge(recipe)) {
            ItemStack result = recipe.getCraftingResult(this);

            this.inventory.setStackInSlot(9, result.copy());

            if (!this.world.isRemote) {
                this.setRecipeUsed(recipe);
            }

            for(int i = 0; i < 9; ++i) {
                this.decrStackSize(i, 1);
            }

            this.forgeActive = false;

            if (!GemForgeTileEntity.this.world.isRemote()) {
                GemForgeTileEntity.this.world.playSound(null, GemForgeTileEntity.this.pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, GemForgeTileEntity.this.world.getRandom().nextFloat() * 0.8F + 0.25F);
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
    public int getInventoryStackLimit() {
        return 1;
    }

    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return !stack.getItem().isIn(MMTags.Items.GEMS) && !stack.getItem().isIn(MMTags.Items.CATALYSTS);
    }

    public int getSizeInventory() {
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
    public ItemStack getStackInSlot(int index) {
        return this.inventory.getStackInSlot(index);
    }

    // Removes up to a specified number of items from an inventory slot and returns them in a new stack.
    public ItemStack decrStackSize(int index, int count) {
        return !this.inventory.getStackInSlot(index).isEmpty() && count > 0 ? this.inventory.getStackInSlot(index).split(count) : ItemStack.EMPTY;
    }

    // Removes a stack from the given slot and returns it.
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemStack = this.inventory.getStackInSlot(index);
        this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        return itemStack;
    }

    // Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory.setStackInSlot(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
    }

    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            System.out.println(false);
            return false;
        } else {
            System.out.println(true);
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (this.getStackInSlot(index).getCount() != 0) {
            return false;
        } else if (index < 9) {
            return stack.getItem().isIn(MMTags.Items.GEMS);
        } else {
            return stack.getItem().isIn(MMTags.Items.CATALYSTS);
        }
    }

    public void clear() {
        for(int i = 0; i < 10; i++) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipes.addTo(resourcelocation, 1);
        }
    }

    @Nullable
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    public void fillStackedContents(RecipeItemHelper helper) {
        for(int i = 0; i < 10; i++) {
            helper.accountStack(this.inventory.getStackInSlot(i));
        }
    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
    protected void invalidateCaps() {
        super.invalidateCaps();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }
}
