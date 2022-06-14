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

package org.infernalstudios.miningmaster.config.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.infernalstudios.miningmaster.MiningMaster;
import org.infernalstudios.miningmaster.config.MiningMasterConfig;
import org.infernalstudios.miningmaster.gen.features.RandomGemOreFeature;
import org.infernalstudios.miningmaster.gen.features.RandomNetherGemOreFeature;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends Screen {
    private OptionsList optionsRowList;

    public ConfigScreen() {
        super(Component.translatable(MiningMaster.MOD_ID + ".config.title"));
    }

    @SuppressWarnings("resource")
    @Override
    public void init() {
        optionsRowList = new OptionsList(minecraft, width, height, 24, height - 32, 25);

        // Gem Booleans
        optionsRowList.addBig(OptionInstance.createBoolean(MiningMaster.MOD_ID + ".config.option.fireRubyEnabled",
                OptionInstance.cachedConstantTooltip(Component.translatable(MiningMaster.MOD_ID + ".config.tooltip.fireRubyEnabled")),
                MiningMasterConfig.CONFIG.fireRubyEnabled.get(), MiningMasterConfig.CONFIG.fireRubyEnabled::set
        ));

        optionsRowList.addBig(OptionInstance.createBoolean(MiningMaster.MOD_ID + ".config.option.iceSapphireEnabled",
                OptionInstance.cachedConstantTooltip(Component.translatable(MiningMaster.MOD_ID + ".config.tooltip.iceSapphireEnabled")),
                MiningMasterConfig.CONFIG.iceSapphireEnabled.get(), MiningMasterConfig.CONFIG.iceSapphireEnabled::set
        ));

        optionsRowList.addBig(OptionInstance.createBoolean(MiningMaster.MOD_ID + ".config.option.spiritGarnetEnabled",
                OptionInstance.cachedConstantTooltip(Component.translatable(MiningMaster.MOD_ID + ".config.tooltip.spiritGarnetEnabled")),
                MiningMasterConfig.CONFIG.spiritGarnetEnabled.get(), MiningMasterConfig.CONFIG.spiritGarnetEnabled::set
        ));

        optionsRowList.addBig(OptionInstance.createBoolean(MiningMaster.MOD_ID + ".config.option.hastePeridotEnabled",
                OptionInstance.cachedConstantTooltip(Component.translatable(MiningMaster.MOD_ID + ".config.tooltip.hastePeridotEnabled")),
                MiningMasterConfig.CONFIG.hastePeridotEnabled.get(), MiningMasterConfig.CONFIG.hastePeridotEnabled::set
        ));

        optionsRowList.addBig(OptionInstance.createBoolean(MiningMaster.MOD_ID + ".config.option.luckyCitrineEnabled",
                OptionInstance.cachedConstantTooltip(Component.translatable(MiningMaster.MOD_ID + ".config.tooltip.luckyCitrineEnabled")),
                MiningMasterConfig.CONFIG.luckyCitrineEnabled.get(), MiningMasterConfig.CONFIG.luckyCitrineEnabled::set
        ));

        optionsRowList.addBig(OptionInstance.createBoolean(MiningMaster.MOD_ID + ".config.option.diveAquamarineEnabled",
                OptionInstance.cachedConstantTooltip(Component.translatable(MiningMaster.MOD_ID + ".config.tooltip.diveAquamarineEnabled")),
                MiningMasterConfig.CONFIG.diveAquamarineEnabled.get(), MiningMasterConfig.CONFIG.diveAquamarineEnabled::set
        ));

        optionsRowList.addBig(OptionInstance.createBoolean(MiningMaster.MOD_ID + ".config.option.heartRhodoniteEnabled",
                OptionInstance.cachedConstantTooltip(Component.translatable(MiningMaster.MOD_ID + ".config.tooltip.heartRhodoniteEnabled")),
                MiningMasterConfig.CONFIG.heartRhodoniteEnabled.get(), MiningMasterConfig.CONFIG.heartRhodoniteEnabled::set
        ));

        optionsRowList.addBig(OptionInstance.createBoolean(MiningMaster.MOD_ID + ".config.option.powerPyriteEnabled",
                OptionInstance.cachedConstantTooltip(Component.translatable(MiningMaster.MOD_ID + ".config.tooltip.powerPyriteEnabled")),
                MiningMasterConfig.CONFIG.powerPyriteEnabled.get(), MiningMasterConfig.CONFIG.powerPyriteEnabled::set
        ));

        optionsRowList.addBig(OptionInstance.createBoolean(MiningMaster.MOD_ID + ".config.option.kineticOpalEnabled",
                OptionInstance.cachedConstantTooltip(Component.translatable(MiningMaster.MOD_ID + ".config.tooltip.kineticOpalEnabled")),
                MiningMasterConfig.CONFIG.kineticOpalEnabled.get(), MiningMasterConfig.CONFIG.kineticOpalEnabled::set
        ));

        optionsRowList.addBig(OptionInstance.createBoolean(MiningMaster.MOD_ID + ".config.option.airMalachiteEnabled",
                OptionInstance.cachedConstantTooltip(Component.translatable(MiningMaster.MOD_ID + ".config.tooltip.airMalachiteEnabled")),
                MiningMasterConfig.CONFIG.airMalachiteEnabled.get(), MiningMasterConfig.CONFIG.airMalachiteEnabled::set
        ));

        // Common Gem Spawns
        optionsRowList.addBig(new OptionInstance<>(MiningMaster.MOD_ID + ".config.option.commonGemsPerChunk",
                OptionInstance.cachedConstantTooltip(Component.translatable(MiningMaster.MOD_ID + ".config.tooltip.commonGemsPerChunk")),
                Options::genericValueLabel, new OptionInstance.IntRange(0, 100),
                MiningMasterConfig.CONFIG.commonGemsPerChunk.get(), MiningMasterConfig.CONFIG.commonGemsPerChunk::set
                )
        );
/*

        // Rare Gem Spawns
        optionsRowList.addBig(new OptionInstance<Integer>(MiningMaster.MOD_ID + ".config.option.rareGemsPerChunk", 0, 100, 1,
                settings -> MiningMasterConfig.CONFIG.rareGemsPerChunk.get().doubleValue(), (settings, value) -> MiningMasterConfig.CONFIG.rareGemsPerChunk.set(value.intValue()),
                (settings, option) -> Component.translatable("options.generic_value", option.getCaption(), // getBaseMessageTranslation() is protected by default, use an access transformer to be able to use it
                        Component.literal(Double.toString((double) Math.round(option.get(settings) * 100) / 100))),
                (minecraft) -> Minecraft.getInstance().font.split(
                        Component.translatable(MiningMaster.MOD_ID + ".config.tooltip.rareGemsPerChunk"), 200)
                )
        );

        // Random Gem Spawns
        optionsRowList.addBig(new OptionInstance<Integer>(MiningMaster.MOD_ID + ".config.option.randomGemsPerChunk", 0, 100, 1,
                settings -> MiningMasterConfig.CONFIG.randomGemsPerChunk.get().doubleValue(), (settings, value) -> MiningMasterConfig.CONFIG.randomGemsPerChunk.set(value.intValue()),
                (settings, option) -> Component.translatable("options.generic_value", option.getCaption(), // getBaseMessageTranslation() is protected by default, use an access transformer to be able to use it
                        Component.literal(Double.toString((double) Math.round(option.get(settings) * 100) / 100))),
                (minecraft) -> Minecraft.getInstance().font.split(
                        Component.translatable(MiningMaster.MOD_ID + ".config.tooltip.randomGemsPerChunk"), 200)
                )
        );
*/

        addWidget(optionsRowList);

        addWidget(new Button((width - 200) / 2, height - 26, 200, 20, Component.translatable("gui.done"), button -> onClose()));
    }

    @Override
    public void onClose() {
        super.onClose();
        RandomNetherGemOreFeature.calculateEnabledOres();
        RandomGemOreFeature.calculateEnabledOres();
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);

        optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);

        List<FormattedCharSequence> list = OptionsSubScreen.tooltipAt(optionsRowList, mouseX, mouseY);
        if (list != null) {
            this.renderTooltip(matrixStack, list, mouseX, mouseY);
        }

        // The parameter names for this function are wrong. The three integers at the end should be x, y, color
        drawCenteredString(matrixStack, font, title, width / 2, 8, 0xFFFFFF);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
