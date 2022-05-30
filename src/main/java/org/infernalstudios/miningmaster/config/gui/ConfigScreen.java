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
import net.minecraft.client.CycleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ProgressOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
        super(new TranslatableComponent(MiningMaster.MOD_ID + ".config.title"));
    }

    @SuppressWarnings("resource")
    @Override
    public void init() {
        optionsRowList = new OptionsList(minecraft, width, height, 24, height - 32, 25);

        // Gem Booleans
        optionsRowList.addBig(CycleOption.createOnOff(MiningMaster.MOD_ID + ".config.option.fireRubyEnabled",
                new TranslatableComponent(MiningMaster.MOD_ID + ".config.tooltip.fireRubyEnabled"),
                settings -> MiningMasterConfig.CONFIG.fireRubyEnabled.get(), (settings, option, value) -> MiningMasterConfig.CONFIG.fireRubyEnabled.set(value)
        ));

        optionsRowList.addBig(CycleOption.createOnOff(MiningMaster.MOD_ID + ".config.option.iceSapphireEnabled",
                new TranslatableComponent(MiningMaster.MOD_ID + ".config.tooltip.iceSapphireEnabled"),
                settings -> MiningMasterConfig.CONFIG.iceSapphireEnabled.get(), (settings, option, value) -> MiningMasterConfig.CONFIG.iceSapphireEnabled.set(value)
        ));

        optionsRowList.addBig(CycleOption.createOnOff(MiningMaster.MOD_ID + ".config.option.spiritGarnetEnabled",
                new TranslatableComponent(MiningMaster.MOD_ID + ".config.tooltip.spiritGarnetEnabled"),
                settings -> MiningMasterConfig.CONFIG.spiritGarnetEnabled.get(), (settings, option, value) -> MiningMasterConfig.CONFIG.spiritGarnetEnabled.set(value)
        ));

        optionsRowList.addBig(CycleOption.createOnOff(MiningMaster.MOD_ID + ".config.option.hastePeridotEnabled",
                new TranslatableComponent(MiningMaster.MOD_ID + ".config.tooltip.hastePeridotEnabled"),
                settings -> MiningMasterConfig.CONFIG.hastePeridotEnabled.get(), (settings, option, value) -> MiningMasterConfig.CONFIG.hastePeridotEnabled.set(value)
        ));

        optionsRowList.addBig(CycleOption.createOnOff(MiningMaster.MOD_ID + ".config.option.luckyCitrineEnabled",
                new TranslatableComponent(MiningMaster.MOD_ID + ".config.tooltip.luckyCitrineEnabled"),
                settings -> MiningMasterConfig.CONFIG.luckyCitrineEnabled.get(), (settings, option, value) -> MiningMasterConfig.CONFIG.luckyCitrineEnabled.set(value)
        ));

        optionsRowList.addBig(CycleOption.createOnOff(MiningMaster.MOD_ID + ".config.option.diveAquamarineEnabled",
                new TranslatableComponent(MiningMaster.MOD_ID + ".config.tooltip.diveAquamarineEnabled"),
                settings -> MiningMasterConfig.CONFIG.diveAquamarineEnabled.get(), (settings, option, value) -> MiningMasterConfig.CONFIG.diveAquamarineEnabled.set(value)
        ));

        optionsRowList.addBig(CycleOption.createOnOff(MiningMaster.MOD_ID + ".config.option.heartRhodoniteEnabled",
                new TranslatableComponent(MiningMaster.MOD_ID + ".config.tooltip.heartRhodoniteEnabled"),
                settings -> MiningMasterConfig.CONFIG.heartRhodoniteEnabled.get(), (settings, option, value) -> MiningMasterConfig.CONFIG.heartRhodoniteEnabled.set(value)
        ));

        optionsRowList.addBig(CycleOption.createOnOff(MiningMaster.MOD_ID + ".config.option.powerPyriteEnabled",
                new TranslatableComponent(MiningMaster.MOD_ID + ".config.tooltip.powerPyriteEnabled"),
                settings -> MiningMasterConfig.CONFIG.powerPyriteEnabled.get(), (settings, option, value) -> MiningMasterConfig.CONFIG.powerPyriteEnabled.set(value)
        ));

        optionsRowList.addBig(CycleOption.createOnOff(MiningMaster.MOD_ID + ".config.option.kineticOpalEnabled",
                new TranslatableComponent(MiningMaster.MOD_ID + ".config.tooltip.kineticOpalEnabled"),
                settings -> MiningMasterConfig.CONFIG.kineticOpalEnabled.get(), (settings, option, value) -> MiningMasterConfig.CONFIG.kineticOpalEnabled.set(value)
        ));

        optionsRowList.addBig(CycleOption.createOnOff(MiningMaster.MOD_ID + ".config.option.airMalachiteEnabled",
                new TranslatableComponent(MiningMaster.MOD_ID + ".config.tooltip.airMalachiteEnabled"),
                settings -> MiningMasterConfig.CONFIG.airMalachiteEnabled.get(), (settings, option, value) -> MiningMasterConfig.CONFIG.airMalachiteEnabled.set(value)
        ));

        // Common Gem Spawns
        optionsRowList.addBig(new ProgressOption(MiningMaster.MOD_ID + ".config.option.commonGemsPerChunk", 0, 100, 1,
                settings -> MiningMasterConfig.CONFIG.commonGemsPerChunk.get().doubleValue(), (settings, value) -> MiningMasterConfig.CONFIG.commonGemsPerChunk.set(value.intValue()),
                (settings, option) -> new TranslatableComponent("options.generic_value", option.getCaption(), // getBaseMessageTranslation() is protected by default, use an access transformer to be able to use it
                        new TextComponent(Double.toString((double) Math.round(option.get(settings) * 100) / 100))),
                (minecraft) -> Minecraft.getInstance().font.split(
                        new TranslatableComponent(MiningMaster.MOD_ID + ".config.tooltip.commonGemsPerChunk"), 200)
                )
        );

        // Rare Gem Spawns
        optionsRowList.addBig(new ProgressOption(MiningMaster.MOD_ID + ".config.option.rareGemsPerChunk", 0, 100, 1,
                settings -> MiningMasterConfig.CONFIG.rareGemsPerChunk.get().doubleValue(), (settings, value) -> MiningMasterConfig.CONFIG.rareGemsPerChunk.set(value.intValue()),
                (settings, option) -> new TranslatableComponent("options.generic_value", option.getCaption(), // getBaseMessageTranslation() is protected by default, use an access transformer to be able to use it
                        new TextComponent(Double.toString((double) Math.round(option.get(settings) * 100) / 100))),
                (minecraft) -> Minecraft.getInstance().font.split(
                        new TranslatableComponent(MiningMaster.MOD_ID + ".config.tooltip.rareGemsPerChunk"), 200)
                )
        );

        // Random Gem Spawns
        optionsRowList.addBig(new ProgressOption(MiningMaster.MOD_ID + ".config.option.randomGemsPerChunk", 0, 100, 1,
                settings -> MiningMasterConfig.CONFIG.randomGemsPerChunk.get().doubleValue(), (settings, value) -> MiningMasterConfig.CONFIG.randomGemsPerChunk.set(value.intValue()),
                (settings, option) -> new TranslatableComponent("options.generic_value", option.getCaption(), // getBaseMessageTranslation() is protected by default, use an access transformer to be able to use it
                        new TextComponent(Double.toString((double) Math.round(option.get(settings) * 100) / 100))),
                (minecraft) -> Minecraft.getInstance().font.split(
                        new TranslatableComponent(MiningMaster.MOD_ID + ".config.tooltip.randomGemsPerChunk"), 200)
                )
        );

        addWidget(optionsRowList);

        addWidget(new Button((width - 200) / 2, height - 26, 200, 20, new TranslatableComponent("gui.done"), button -> onClose()));
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
