package org.infernalstudios.miningmaster.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class GemItem extends Item {
    public GemItem() {
        super(new Item.Properties().group(ItemGroup.MATERIALS));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (Screen.hasShiftDown()) {
            tooltip.add(new StringTextComponent("\u00A7dCombine with an item in a smithing table to enchant!"));
        } else {
            tooltip.add(new StringTextComponent("\u00A78Hold Shift for Instructions"));
        }
    }
}
