package org.infernalstudios.miningmaster.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.infernalstudios.miningmaster.MiningMaster;

import javax.annotation.Nullable;
import java.util.List;

public class GemItem extends Item {
    public GemItem() {
        super(new Item.Properties().tab(MiningMaster.TAB));
    }



    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (Screen.hasShiftDown()) {
            tooltip.add(new TextComponent("\u00A7dCombine with an item in a smithing table to enchant!"));
        } else {
            tooltip.add(new TextComponent("\u00A78Hold Shift for Instructions"));
        }
    }
}
